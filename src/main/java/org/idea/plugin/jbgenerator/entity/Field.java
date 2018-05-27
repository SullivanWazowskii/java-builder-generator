package org.idea.plugin.jbgenerator.entity;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;

public class Field {

    private final PsiFieldMember psiFieldMember;
    private boolean isNullable;

    public Field(PsiFieldMember psiFieldMember) {
        this.psiFieldMember = psiFieldMember;
    }

    public PsiFieldMember getPsiFieldMember() {
        return psiFieldMember;
    }

    public PsiType getType() {
        return psiFieldMember.getElement().getType();
    }

    public PsiModifierList getModifierList() {
        return psiFieldMember.getElement().getModifierList();
    }

    public boolean isPrimitiveType() {
        return getType() instanceof PsiPrimitiveType;
    }

    public boolean isOptional() {
        return !isPrimitiveType() && isNullable;
    }

    public String getName() {
        return psiFieldMember.getElement().getName();
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    @Override
    public String toString() {
        return psiFieldMember.getElement().getName() + ":" + psiFieldMember.getElement().getType().getPresentableText();
    }
}
