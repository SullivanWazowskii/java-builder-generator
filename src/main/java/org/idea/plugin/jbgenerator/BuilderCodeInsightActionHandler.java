package org.idea.plugin.jbgenerator;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.LanguageCodeInsightActionHandler;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiUtil;
import org.idea.plugin.jbgenerator.entity.Field;
import org.idea.plugin.jbgenerator.settings.BuilderGenerationOptions;
import org.idea.plugin.jbgenerator.settings.ClassGenerationOptions;
import org.idea.plugin.jbgenerator.ui.BuilderOption;

import java.util.ArrayList;
import java.util.List;

import static org.idea.plugin.jbgenerator.ui.BuilderDialog.selectFieldsAndOptions;

public class BuilderCodeInsightActionHandler implements LanguageCodeInsightActionHandler {

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Override
    public void invoke(Project project, Editor editor, PsiFile psiFile) {
        PsiClass[] classes = ((PsiJavaFile) psiFile).getClasses();
        PsiClass clazz = classes[0];
        PsiField[] allFields = clazz.getAllFields();
        if (allFields.length == 0) {
            return;
        }

        List<PsiFieldMember> fields = new ArrayList<>();
        for (PsiField psiField : allFields) {
            if (psiField.getModifierList() != null && psiField.getModifierList().hasModifierProperty(PsiModifier.STATIC)) {
                continue;
            }
            fields.add(new PsiFieldMember(psiField));
        }


        PsiClass targetClass = PsiUtil.getTopLevelClass(fields.get(0).getElement());
        if (targetClass == null) {
            return;
        }

        List<Field> selectedFields = selectFieldsAndOptions(fields, project);
        if (selectedFields.isEmpty()) {
            return;
        }

        WriteCommandAction.runWriteCommandAction(project, () -> {

            // Default options
            BuilderGenerationOptions builderGenerationOptions = BuilderGenerationOptions.builder().build();

            ClassGenerationOptions classGenerationOptions = ClassGenerationOptions.builder()
                    .withIsJacksonEnabled(isOptionEnabled(BuilderOption.IS_JACKSON_ENABLED))
                    .withIsRequireNonNullEnabled(isOptionEnabled(BuilderOption.REQUIRE_NON_NULL_IN_CONSTRUCTOR))
                    .withIsConstructorPropertiesEnabled(isOptionEnabled(BuilderOption.REQUIRE_CONSTRUCTOR_PROPERTIES_IN_CONSTRUCTOR))
                    .withIsAddSwaggerAnnotationsEnabled(isOptionEnabled(BuilderOption.ADD_SWAGGER_API_ANNOTATIONS))
                    .build();

            BuilderPlugin builderPlugin = DefaultBuilderPlugin.builder()
                    .withProject(project)
                    .withClassGenerationOptions(classGenerationOptions)
                    .withBuilderGenerationOptions(
                            isOptionEnabled(BuilderOption.ADD_BUILDER) ? builderGenerationOptions : null)
                    .build();

            builderPlugin.process(targetClass, selectedFields);

            JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiFile);
            CodeStyleManager.getInstance(project).reformat(targetClass);
        });
    }

    private boolean isOptionEnabled(BuilderOption option) {
        return PropertiesComponent.getInstance().getBoolean(option.getProperty());
    }

    @Override
    public boolean isValidFor(Editor editor, PsiFile psiFile) {
        return true;
    }
}
