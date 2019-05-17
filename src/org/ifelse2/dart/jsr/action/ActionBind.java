package org.ifelse2.dart.jsr.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.ifelse2.IEAppLoader;
import org.ifelse2.dart.jsr.Data;
import org.ifelse2.dart.jsr.FileUtil;
import org.ifelse2.dart.jsr.Log;
import org.ifelse2.dart.jsr.MProject;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public class ActionBind extends AnAction {




    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {


        VirtualFile virtualFile = anActionEvent.getData(PlatformDataKeys.VIRTUAL_FILE);

        if (virtualFile != null) {



            String vfile = virtualFile.getPath();

            if (!vfile.endsWith(".dart")) {

                Messages.showMessageDialog("not dart file", "Error", Messages.getErrorIcon());
                return;

            }
            if( !Data.isJsrProject(anActionEvent.getProject()) ){
              IEAppLoader.getMProjectorCreate(anActionEvent.getProject());
            }

            String basepath = anActionEvent.getProject().getBasePath();
            MProject mProject = IEAppLoader.getMProjectorCreate(anActionEvent.getProject());
            mProject.addFile(vfile.replace(basepath, ""));
            Data.save(anActionEvent.getProject());
            Data.onFileChanged(anActionEvent.getProject(),vfile);







        }


    }



}
