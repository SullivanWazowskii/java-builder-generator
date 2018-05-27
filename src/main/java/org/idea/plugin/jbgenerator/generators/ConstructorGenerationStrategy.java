package org.idea.plugin.jbgenerator.generators;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.idea.plugin.jbgenerator.entity.Field;
import org.idea.plugin.jbgenerator.settings.ClassGenerationOptions;
import org.idea.plugin.jbgenerator.settings.ClassGenerationSettings;

import javax.annotation.Nonnull;
import java.util.List;

import static org.idea.plugin.jbgenerator.generators.AnnotationGenerationStrategy.*;
import static org.idea.plugin.jbgenerator.generators.ImportGenerationStrategy.*;

public class ConstructorGenerationStrategy {

    private final Project project;
    private final PsiElementFactory psiElementFactory;
    private final ImportGenerationStrategy importGenerationStrategy;

    public ConstructorGenerationStrategy(Project project) {
        this.project = project;
        this.psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        this.importGenerationStrategy = new ImportGenerationStrategy(project);
    }

    @Nonnull
    public PsiMethod createConstructor(ClassGenerationSettings classGenerationInfo) {

        PsiClass targetClass = classGenerationInfo.getClazz();
        List<Field> fields = classGenerationInfo.getFields();
        ClassGenerationOptions creationOption = classGenerationInfo.getOptions();

        PsiMethod constructor = createEmptyPrivateConstructor(classGenerationInfo.getClazz());

        for (Field field : fields) {
            setModifiersIfAbsents(field);
            constructor.getParameterList().add(createParameter(field, creationOption));
            constructor.getBody().add(createAssignStatement(field, creationOption));
        }

        // TODO: add import only if need
        if (creationOption.isJacksonEnabled()) {
            constructor.getModifierList().addAnnotation(getJsonCreator());

            importGenerationStrategy.addImport(targetClass, JSON_PROPERTY);
            importGenerationStrategy.addImport(targetClass, JSON_CREATOR);
        }

        if (creationOption.isRequireNonNullEnabled()) {
            importGenerationStrategy.addImport(targetClass, JAVAX_ANNOTATION_NONNULL);
            importGenerationStrategy.addImport(targetClass, JAVAX_ANNOTATION_NULLABLE);
            importGenerationStrategy.addStaticMethodImport(targetClass, OBJECTS_REQUIRE_NON_NULL);
        }

        if (creationOption.isConstructorPropertiesEnabled()) {
            constructor.getModifierList().addAnnotation(getConstructorProperties(fields));
        }

        return constructor;
    }

    private PsiMethod createEmptyPrivateConstructor(PsiClass clazz) {
        PsiMethod constructor = psiElementFactory.createConstructor(clazz.getName());
        constructor.getModifierList().setModifierProperty(PsiModifier.PRIVATE, true);
        return constructor;
    }

    private void setModifiersIfAbsents(Field field) {
        PsiModifierList modifierList = field.getModifierList();

        if (!modifierList.hasModifierProperty(PsiModifier.FINAL)) {
            modifierList.setModifierProperty(PsiModifier.FINAL, true);
        }

        if (!modifierList.hasModifierProperty(PsiModifier.PRIVATE)) {
            modifierList.setModifierProperty(PsiModifier.PRIVATE, true);
        }
    }

    private PsiParameter createParameter(Field field, ClassGenerationOptions options) {
        PsiParameter parameter = psiElementFactory.createParameter(field.getName(), field.getType());
        PsiModifierList modifierList = parameter.getModifierList();

        if (options.isJacksonEnabled()) {
            modifierList.addAnnotation(getJsonProperty(field.getName()));
        }

        if (options.isRequireNonNullEnabled() && !field.isPrimitiveType() && !field.isNullable()) {
            modifierList.addAnnotation(getNonnull());
        }
        if (options.isRequireNonNullEnabled() && !field.isPrimitiveType() && field.isNullable()) {
            modifierList.addAnnotation(getNullable());
        }

        return parameter;
    }

    private PsiStatement createAssignStatement(Field field, ClassGenerationOptions creationOption) {
        String fieldName = field.getName();

        String statementText = creationOption.isRequireNonNullEnabled() && !field.isPrimitiveType() && !field.isNullable()
                ? String.format("this.%1$s = requireNonNull(%1$s,\"%1$s\");", fieldName)
                : String.format("this.%1$s = %1$s;", fieldName);

        return psiElementFactory.createStatementFromText(statementText, null);
    }

}
