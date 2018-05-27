package org.idea.plugin.jbgenerator.settings;

import com.intellij.psi.PsiClass;
import org.idea.plugin.jbgenerator.entity.Field;

import java.util.List;

public class BuilderGenerationSettings {

    private final PsiClass clazz;
    private final List<Field> fields;
    private final BuilderGenerationOptions options;

    public BuilderGenerationSettings(PsiClass clazz,
                                     List<Field> fields,
                                     BuilderGenerationOptions options) {
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

    public BuilderGenerationOptions getOptions() {
        return options;
    }
}
