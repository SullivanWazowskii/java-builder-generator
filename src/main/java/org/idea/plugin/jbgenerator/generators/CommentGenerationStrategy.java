package org.idea.plugin.jbgenerator.generators;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;

public class CommentGenerationStrategy {


    private final Project project;
    private final PsiElementFactory psiElementFactory;

    public CommentGenerationStrategy(Project project) {
        this.project = project;
        this.psiElementFactory = JavaPsiFacade.getInstance(project).getElementFactory();
    }

    public void setStringComment(PsiMethod method, String strComment) {
        StringBuilder str = new StringBuilder("/**\n")
                .append("* ").append(strComment).append("\n")
                .append("* @return  new Builder() ")
                .append("*/");

        PsiComment comment = psiElementFactory.createCommentFromText(str.toString(), null);
        PsiDocComment doc = method.getDocComment();

        if (doc != null) {
            doc.replace(comment);
        } else {
            method.addBefore(comment, method.getFirstChild());
        }
    }

    public void setStringComment(PsiClass clazz, String strComment) {

        StringBuilder str = new StringBuilder("/**\n")
                .append("* ")
                .append(strComment)
                .append("*/");

        PsiComment comment = psiElementFactory.createCommentFromText(str.toString(), null);
        PsiDocComment doc = clazz.getDocComment();

        if (doc != null) {
            doc.replace(comment);
        } else {
            clazz.addBefore(comment, clazz.getFirstChild());
        }
    }
}
