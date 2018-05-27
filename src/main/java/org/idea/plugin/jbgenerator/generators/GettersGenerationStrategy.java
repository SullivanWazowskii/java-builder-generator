package org.idea.plugin.jbgenerator.generators;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.idea.plugin.jbgenerator.entity.Field;
import org.idea.plugin.jbgenerator.settings.ClassGenerationSettings;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.idea.plugin.jbgenerator.generators.AnnotationGenerationStrategy.getJsonProperty;
import static org.idea.plugin.jbgenerator.generators.AnnotationGenerationStrategy.getNonnull;


public class GettersGenerationStrategy {

    private final Project project;
    private final PsiElementFactory psiElementFactory;
    private final PsiFileFactory psiFileFactory;

    public GettersGenerationStrategy(Project project) {
        this.project = project;
        this.psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        this.psiFileFactory = PsiFileFactory.getInstance(project);
    }

    public List<PsiMethod> createGetters(ClassGenerationSettings classGenerationInfo) {
        List<Field> fields = classGenerationInfo.getFields();

        return fields.stream()
                .map(field -> getGetter(classGenerationInfo, field))
                .collect(Collectors.toList());
    }

    private PsiMethod getGetter(ClassGenerationSettings classGenerationInfo, Field field) {

        boolean requireNonNullEnabled = classGenerationInfo.getOptions().isRequireNonNullEnabled();
        boolean jacksonEnabled = classGenerationInfo.getOptions().isJacksonEnabled();

        String fieldName = field.getName();
        String getterName = "get" + capitalize(fieldName);

        PsiType returnType = requireNonNullEnabled && field.isOptional() ? getOptionalType(field) : field.getType();
        PsiMethod method = psiElementFactory.createMethod(getterName, returnType);

        method.getBody().add(createGetterStatement(field, method, requireNonNullEnabled));

        if (jacksonEnabled) {
            method.getModifierList().addAnnotation(getJsonProperty(fieldName));
        }

        if (requireNonNullEnabled && !field.isPrimitiveType() && !field.isNullable()) {
            method.getModifierList().addAnnotation(getNonnull());
        }

        return method;
    }

    private PsiType getOptionalType(Field field) {
        PsiJavaFile dummyJavaFile = createDummyJavaFile(
                String.format("public class My{ public Optional<%s> get%s {return Optional.ofNullable(null);}}",
                        field.getType().getPresentableText(), capitalize(field.getName())));
        return dummyJavaFile.getClasses()[0].getFields()[0].getType();
    }

    private PsiStatement createGetterStatement(Field field, PsiMethod method, boolean requireNonNullEnabled) {
        String fieldName = field.getName();
        String statement = requireNonNullEnabled && field.isOptional()
                ? String.format("return Optional.ofNullable(%s);", fieldName)
                : String.format("return %s;", fieldName);

        return psiElementFactory.createStatementFromText(statement, method);
    }

    private PsiJavaFile createDummyJavaFile(String text) {
        String fileName = "_Dummy_." + JavaFileType.INSTANCE.getDefaultExtension();
        return (PsiJavaFile) psiFileFactory.createFileFromText(fileName, JavaFileType.INSTANCE, text);
    }

}
