package org.idea.plugin.jbgenerator.generators;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.apache.commons.lang3.StringUtils;
import org.idea.plugin.jbgenerator.entity.Field;
import org.idea.plugin.jbgenerator.settings.BuilderGenerationSettings;

import java.util.List;
import java.util.stream.Collectors;

import static org.idea.plugin.jbgenerator.utils.PsiAdderReplacePolicy.addMethod;
import static org.idea.plugin.jbgenerator.utils.PsiAdderReplacePolicy.addMethods;

public class BuilderGenerationStrategy {

    private final Project project;
    private final PsiElementFactory psiElementFactory;
    private final CommentGenerationStrategy commentGenerationStrategy;

    public BuilderGenerationStrategy(Project project) {
        this.project = project;
        this.psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        this.commentGenerationStrategy = new CommentGenerationStrategy(project);
    }

    public PsiClass createBuilderClass(BuilderGenerationSettings builderGenerationSettings) {

        PsiClass targetClass = builderGenerationSettings.getClazz();

        String builderClassName = builderGenerationSettings.getOptions().getBuilderClassName();
        String builderSetterPrefix = builderGenerationSettings.getOptions().getBuilderSetterPrefix();

        PsiClass builderClass = createClass(builderClassName);

        List<Field> fields = builderGenerationSettings.getFields();

        for (Field fieldMember : fields) {
            builderClass.add(createField(fieldMember));
        }

        //generate builder `with` methods
        List<PsiMethod> builderSetters = createBuilderSetters(fields, builderClassName, builderSetterPrefix);
        addMethods(builderClass, builderSetters);

        //generate build
        PsiMethod buildMethod = createBuilderBuildMethod(targetClass, fields, builderClass);
        addMethod(builderClass, buildMethod);

        commentGenerationStrategy.setStringComment(builderClass, "Билдер для {@link " + targetClass.getQualifiedName() + "}");
        return builderClass;
    }

    /**
     * Создает статический метод инициализации биллдера.
     *
     * @return Метод инициализации билдера.
     */
    public PsiMethod createBuilderMethod(BuilderGenerationSettings builderGenerationSettings) {
        String builderClassName = builderGenerationSettings.getOptions().getBuilderClassName();
        String builderMethodName = builderGenerationSettings.getOptions().getBuilderMethodName();

        PsiType builderType = psiElementFactory.createTypeFromText(builderClassName, null);
        PsiMethod builderMethod = psiElementFactory.createMethod(builderMethodName, builderType);

        PsiUtil.setModifierProperty(builderMethod, PsiModifier.STATIC, true);
        PsiUtil.setModifierProperty(builderMethod, PsiModifier.PUBLIC, true);

        PsiStatement newStatement = psiElementFactory.createStatementFromText(
                String.format("return new %s();", builderType.getPresentableText()), builderMethod);

        PsiCodeBlock newBuilderMethodBody = builderMethod.getBody();
        newBuilderMethodBody.add(newStatement);

        commentGenerationStrategy.setStringComment(builderMethod, "Создает новый объект билдера для " +
                "{@link " + builderGenerationSettings.getClazz().getQualifiedName() + "}");
        return builderMethod;
    }

    private PsiField createField(Field field) {
        return psiElementFactory.createField(field.getName(), field.getType());
    }


    private PsiClass createClass(String builderClassName) {
        PsiClass builderClass = psiElementFactory.createClass(builderClassName);

        PsiUtil.setModifierProperty(builderClass, PsiModifier.STATIC, true);
        PsiUtil.setModifierProperty(builderClass, PsiModifier.FINAL, true);
        PsiUtil.setModifierProperty(builderClass, PsiModifier.PUBLIC, true);

        return builderClass;
    }


    private List<PsiMethod> createBuilderSetters(List<Field> fields,
                                                 String builderClassName,
                                                 String builderSetterPrefix) {
        final PsiType builderType = psiElementFactory.createTypeFromText(builderClassName, null);
        return fields.stream()
                .map(field -> generateBuilderSetter(builderType, field, builderSetterPrefix))
                .collect(Collectors.toList());
    }


    private PsiMethod generateBuilderSetter(PsiType builderType,
                                            Field field,
                                            String builderSetterPrefix) {

        String fieldName = field.getName();
        String methodName = String.format("%s%s", builderSetterPrefix, StringUtils.capitalize(fieldName));

        PsiParameter setterParameter = psiElementFactory.createParameter(fieldName, field.getType());

        PsiMethod setterMethod = psiElementFactory.createMethod(methodName, builderType);
        setterMethod.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);
        setterMethod.getParameterList().add(setterParameter);

        addSetterBodyStatements(fieldName, setterMethod);


        return setterMethod;
    }

    private void addSetterBodyStatements(String fieldName, PsiMethod setterMethod) {
        PsiCodeBlock setterMethodBody = setterMethod.getBody();

        String textStatement = String.format("this.%s = %s;", fieldName, fieldName);
        setterMethodBody.add(psiElementFactory.createStatementFromText(textStatement, setterMethod));
        setterMethodBody.add(psiElementFactory.createStatementFromText("return this;", setterMethod));
    }


    private PsiMethod createBuilderBuildMethod(PsiClass clazz,
                                               List<Field> fields,
                                               PsiClass builderClass) {

        PsiType clazzType = psiElementFactory.createTypeFromText(clazz.getName(), null);
        PsiMethod buildMethod = psiElementFactory.createMethod("build", clazzType);

        buildMethod.getModifierList().setModifierProperty(PsiModifier.PUBLIC, true);


        PsiStatement buildMethodStatement = createBuildMethodStatement(fields, clazz, builderClass);
        buildMethod.getBody().add(buildMethodStatement);

        return buildMethod;
    }

    private PsiStatement createBuildMethodStatement(List<Field> fields,
                                                    PsiClass clazz,
                                                    PsiClass builderClass) {

        StringBuilder builderMethodStatementText = new StringBuilder();

        builderMethodStatementText.append("return new ").append(clazz.getName()).append("(");

        fields.forEach(psiFieldMember ->
                builderMethodStatementText
                        .append(psiFieldMember.getPsiFieldMember().getElement().getName())
                        .append(","));

        builderMethodStatementText.deleteCharAt(builderMethodStatementText.length() - 1);
        builderMethodStatementText.append(");");

        return psiElementFactory.createStatementFromText(builderMethodStatementText.toString(), builderClass);
    }

}
