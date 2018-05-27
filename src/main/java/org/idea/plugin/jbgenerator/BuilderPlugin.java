package org.idea.plugin.jbgenerator;

import com.intellij.psi.PsiClass;
import org.idea.plugin.jbgenerator.entity.Field;

import java.util.List;

public interface BuilderPlugin {

    void process(PsiClass targetClass, List<Field> fields);

}
