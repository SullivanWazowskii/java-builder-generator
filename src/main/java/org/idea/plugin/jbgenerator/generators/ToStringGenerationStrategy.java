package org.idea.plugin.jbgenerator.generators;

import com.intellij.psi.PsiClass;
import org.jetbrains.java.generate.GenerateToStringWorker;
import org.jetbrains.java.generate.config.ReplacePolicy;
import org.jetbrains.java.generate.template.TemplatesManager;
import org.jetbrains.java.generate.template.toString.ToStringTemplatesManager;

import java.util.Arrays;

public class ToStringGenerationStrategy {

    public static void generateToString(PsiClass targetClass) {
        GenerateToStringWorker worker = new GenerateToStringWorker(targetClass, null, true);
        TemplatesManager instance = ToStringTemplatesManager.getInstance();
        worker.execute(Arrays.asList(targetClass.getAllFields()), instance.getDefaultTemplate(), ReplacePolicy.getInstance());
    }
}
