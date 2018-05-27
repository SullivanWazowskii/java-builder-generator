package org.idea.plugin.jbgenerator.settings;

import com.intellij.psi.PsiClass;
import org.idea.plugin.jbgenerator.entity.Field;

import java.util.List;

public class ClassGenerationSettings {

    private final PsiClass clazz;
    private final List<Field> fields;
    private final ClassGenerationOptions options;

    public ClassGenerationSettings(PsiClass clazz,
                                   List<Field> fields,
                                   ClassGenerationOptions options) {
        this.clazz = clazz;
        this.fields = fields;
        this.options = options;
    }

    public PsiClass getClazz() {
        return clazz;
    }

    public List<Field> getFields() {
        return fields;
    }

    public ClassGenerationOptions getOptions() {
        return options;
    }
}
