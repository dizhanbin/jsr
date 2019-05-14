package org.ifelse2;

import com.google.gson.Gson;
import com.intellij.ide.ApplicationLoadListener;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.ifelse2.dart.jsr.Data;
import org.ifelse2.dart.jsr.Log;
import org.ifelse2.dart.jsr.MFile;
import org.ifelse2.dart.jsr.MProject;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;

public class IEAppLoader implements ApplicationLoadListener {


    public static Map<String, MProject> mprojects = new HashMap<>();

    public static MProject getMProject(Project project){

        return mprojects.get(project.getName());

    }

    @Override
    public void beforeApplicationLoaded(@NotNull Application application, @NotNull String configPath) {


        ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerListener() {
            @Override
            public void projectOpened(Project project) {



                if( !mprojects.containsKey(project.getName()) ){

                    MProject mProject = Data.read(project);
                    if( mProject == null )
                        mProject = new MProject();
                    mprojects.put(project.getName(),mProject);


                    Log.i("file://%s",Data.getDataPath(project));
                    mProject.printAll("     ");


                }




                VirtualFileManager.getInstance().addVirtualFileListener(new VirtualFileListener() {


                    @Override
                    public void contentsChanged(@NotNull VirtualFileEvent event) {

                        String filename = event.getFileName();
                        String filepath = event.getFile().getPath();

                        if( "workspace.xml,R.java".indexOf( event.getFileName() ) >-1 )
                        {
                            return;
                        }

                        Project project = ProjectUtil.guessProjectForFile(event.getFile());

                        String basepath = project.getBasePath();

                        MProject mProject = mprojects.get(project.getName());

                        if( mProject.indexOf(filepath.replace(basepath,"")) > -1 ){

                            Log.i("dart bean changed:%s",filepath);
                            Data.onFileChanged(project,filepath);

                        }




                    }

                    @Override
                    public void fileCreated(@NotNull VirtualFileEvent event) {



                    }


                });

            }




        });



    }
}
