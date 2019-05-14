package org.ifelse2.dart.jsr.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.ifelse2.dart.jsr.Data;
import org.jetbrains.annotations.NotNull;

public class ActionBindAll extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        Data.onFileChanged(anActionEvent.getProject());


    }

}
