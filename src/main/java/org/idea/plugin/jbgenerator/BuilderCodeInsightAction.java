package org.idea.plugin.jbgenerator;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class BuilderCodeInsightAction extends BaseCodeInsightAction {
    private final BuilderCodeInsightActionHandler handler = new BuilderCodeInsightActionHandler();

    @Override
    public void actionPerformed(AnActionEvent e) {
        super.actionPerformed(e);
    }

    @NotNull
    @Override
    protected CodeInsightActionHandler getHandler() {
        return handler;
    }

    @Override
    protected boolean isValidForFile(Project project, Editor editor, PsiFile file) {
        return true;
    }
}
