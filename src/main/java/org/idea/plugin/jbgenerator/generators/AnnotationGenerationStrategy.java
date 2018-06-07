package org.idea.plugin.jbgenerator.generators;

import com.intellij.psi.PsiAnnotationOwner;
import org.idea.plugin.jbgenerator.entity.Field;

import java.util.List;

public class AnnotationGenerationStrategy {


    public static String getJsonCreator() {
        return "com.fasterxml.jackson.annotation.JsonCreator";
    }

    public static String getJsonProperty(String fieldName) {
        return "com.fasterxml.jackson.annotation.JsonProperty(\"" + fieldName + "\")";
    }

    public static String getNonnull() {
        return "Nonnull";
    }

    public static String getNullable() {
        return "Nullable";
    }

    public static String getApiModel() {
        return "ApiModel(description = \"\")";
    }

    public static String getApiModelProperty(boolean isOptional) {
        return "ApiModelProperty(value = \"\",  example = \"\", required =\"" + Boolean.toString(!isOptional) + "\" )";
    }


    public static String getConstructorProperties(List<Field> fields) {
        StringBuilder annotationString = new StringBuilder("java.beans.ConstructorProperties({");
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            String name = fields.get(i).getName();

            annotationString.append("\"")
                    .append(name)
                    .append((i < size - 1) ? "\"," : "\"");

        }
        annotationString.append("})");
        return annotationString.toString();
    }

    public static void addAnnotation(PsiAnnotationOwner psiAnnotationOwner,
                                     String annotationText) {
        psiAnnotationOwner.addAnnotation(annotationText);
    }
}
