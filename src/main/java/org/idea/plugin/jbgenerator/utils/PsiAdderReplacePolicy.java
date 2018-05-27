package org.idea.plugin.jbgenerator.utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PsiAdderReplacePolicy {

    public static void addMethods(PsiClass clazz, List<PsiMethod> methods) {
        methods.forEach(method -> {
            PsiMethod[] methodsByName = clazz.findMethodsByName(method.getName(), false);
            Arrays.stream(methodsByName).forEach(foundedMethod -> foundedMethod.delete());
            clazz.add(method);
        });
    }

    public static void addMethod(PsiClass clazz, PsiMethod methods) {
        addMethods(clazz, Collections.singletonList(methods));
    }

    public static void addClass(PsiClass clazz, PsiClass psiClass) {
        PsiClass innerClass = clazz.findInnerClassByName(psiClass.getName(), false);

        if (innerClass != null) {
            innerClass.delete();
        }

        clazz.add(psiClass);
    }
}
