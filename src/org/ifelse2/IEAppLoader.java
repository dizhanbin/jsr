package org.ifelse2;

import com.google.gson.Gson;
import com.intellij.ide.ApplicationLoadListener;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.project.*;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.ifelse2.dart.jsr.*;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class IEAppLoader implements ApplicationLoadListener {


    public static Map<String, MProject> mprojects = new HashMap<>();

    public static MProject getMProject(Project project){

        return mprojects.get(project.getName());

    }

    public static MProject getMProjectorCreate(Project project) {

        if( !mprojects.containsKey(project.getName()) ){

            MProject    mProject = new MProject();
            mprojects.put(project.getName(), mProject);
            initProject(project);

            return mProject;

        }else {

            MProject mProject = mprojects.get(project.getName());

            return mProject;
        }
    }

    @Override
    public void beforeApplicationLoaded(@NotNull Application application, @NotNull String configPath) {


        ProjectManager.getInstance().addProjectManagerListener(new ProjectManagerListener() {
            @Override
            public void projectOpened(Project project) {



                if( !mprojects.containsKey(project.getName()) ){

                    if( Data.isJsrProject(project) ) {

                        MProject mProject = Data.read(project);
                        Log.i("file://%s", Data.getDataPath(project));
                        mProject.printAll("     ");
                        mprojects.put(project.getName(),mProject);
                        //initProject(project);
                    }


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

                        if( Data.isJsrProject(project) ) {

                            Project project = ProjectUtil.guessProjectForFile(event.getFile());

                            String basepath = project.getBasePath();

                            MProject mProject = mprojects.get(project.getName());

                            if (mProject.indexOf(filepath.replace(basepath, "")) > -1) {

                                Log.i("file://%s", filepath);
                                Data.onFileChanged(project, filepath);

                            }

                        }




                    }

                    @Override
                    public void fileCreated(@NotNull VirtualFileEvent event) {



                    }


                });

            }




        });



    }

    private static void initProject(Project project) {

        String basepath = project.getBasePath();


        String path = basepath+"/iedata/jsr/dart_class.tpl";
        if( !new File(path).exists())
        FileUtil.copyFromRes("/res/dart_class.tpl",path);

        path = basepath+"/iedata/jsr/dart_class_factory.tpl";
        if( !new File(path).exists())
        FileUtil.copyFromRes("/res/dart_class_factory.tpl",path);

        path = basepath+"/iedata/jsr/R.groovy";
        if( !new File(path).exists())
        FileUtil.copyFromRes("/res/R.groovy",path);

        path = basepath+"/lib/jsr/jsr.dart";
        if( !new File(path).exists())
        FileUtil.copyFromRes("/res/jsr.dart",path);

        path = basepath+"/lib/jsr/sqlo.dart";
        if( !new File(path).exists())
        FileUtil.copyFromRes("/res/sqlo.dart",path);






    }
}
