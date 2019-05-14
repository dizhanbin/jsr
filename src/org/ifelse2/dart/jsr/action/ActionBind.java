package org.ifelse2.dart.jsr.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.ide.generation.BaseDartGenerateHandler;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.psi.impl.DartVarDeclarationListImpl;
import org.ifelse2.IEAppLoader;
import org.ifelse2.dart.jsr.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActionBind extends AnAction {




    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {


        VirtualFile virtualFile = anActionEvent.getData(PlatformDataKeys.VIRTUAL_FILE);

        if (virtualFile != null) {



            String vfile = virtualFile.getPath();
            Log.i("select file:%s", virtualFile.getPath());
            if (!vfile.endsWith(".dart")) {

                Messages.showMessageDialog("not dart file", "Error", Messages.getErrorIcon());
                return;

            }
            String basepath = anActionEvent.getProject().getBasePath();
            MProject mProject = IEAppLoader.getMProject(anActionEvent.getProject());
            mProject.addFile(vfile.replace(basepath, ""));
            Data.save(anActionEvent.getProject());


            Data.onFileChanged(anActionEvent.getProject(),vfile);


        }


    }



}
