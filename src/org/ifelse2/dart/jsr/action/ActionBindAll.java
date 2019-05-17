package org.ifelse2.dart.jsr.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.ifelse2.dart.jsr.Data;
import org.ifelse2.dart.jsr.Log;
import org.jetbrains.annotations.NotNull;

public class ActionBindAll extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {

        if( !Data.isJsrProject(anActionEvent.getProject()) ) {

            Log.i("pop menu -->ifelse -->add to class");
            return;
        }

        Data.onFileChanged(anActionEvent.getProject());


    }


}
