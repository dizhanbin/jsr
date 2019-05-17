package org.ifelse2.dart.jsr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.io.IOUtil;
import org.ifelse2.IEAppLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {


    public static String getDataR(Project project) {

        String basepath = project.getBasePath();
        String jsrpath = basepath + "/iedata/jsr/R.groovy";
        return jsrpath;


    }

    public static String getDataPath(Project project) {

        String basepath = project.getBasePath();
        String jsrpath = basepath + "/iedata/jsr/jsr.data";
        return jsrpath;

    }


    public static MProject read(Project project) {


        String jsrpath = getDataPath(project);

        Gson gson = new Gson();

        if (new File(jsrpath).exists()) {

            try {
                DataInputStream dis = new DataInputStream(new FileInputStream(jsrpath));

                String json = com.intellij.util.io.IOUtil.readUTF(dis);


                MProject mproject = gson.fromJson(json, MProject.class);

                dis.close();

                return mproject;


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;


    }

    public static boolean save(Project project) {

        String jsrpath = getDataPath(project);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MProject mProject = IEAppLoader.getMProjectorCreate(project);
        String jsonstr = gson.toJson(mProject);

        try {

            File file = new File(jsrpath);
            if( !file.exists() )
                file.getParentFile().mkdirs();


            DataOutputStream dis = new DataOutputStream(new FileOutputStream(jsrpath));
            IOUtil.writeUTF(dis, jsonstr);
            VirtualFile vf = VirtualFileManager.getInstance().refreshAndFindFileByUrl("file://" + jsrpath);
            //FileEditor fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(vf);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    public static boolean refreshFile(Project project) {


        MProject mProject = IEAppLoader.getMProject(project);


        List<String> mClasses = new ArrayList<>();
        for (String filepath : mProject.getFiles()) {

            MFile mFile = MFile.fromDartFile(project, project.getBasePath() + filepath);
            for (MClass mc : mFile.classes) {

                mClasses.add(mc.name);
            }

        }

        try {

            GroovyUtil.run(project, "refreshClass", mProject.getFiles(), mClasses);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    public static void onFileChanged(Project project, String filepath) {

        MFile mFile = MFile.fromDartFile(project, filepath);
        try {

            GroovyUtil.run(project, "onDartChanged", mFile);
            Data.refreshFile(project);
            VirtualFileManager.getInstance().syncRefresh();

            Log.i(" [√] file:%s", mFile.filepath);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void onFileChanged(Project project) {

        Log.i("");
        MProject mProject = IEAppLoader.getMProject(project);

        try {
            for (String filepath : mProject.getFiles()) {

                MFile mFile = MFile.fromDartFile(project,project.getBasePath() + filepath);
                GroovyUtil.run(project, "onDartChanged", mFile);

                Log.i(" [√] file:%s", mFile.filepath);

            }
            Data.refreshFile(project);


            VirtualFileManager.getInstance().syncRefresh();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isJsrProject(Project project) {

        return new File(getDataPath(project)).exists();
    }
}
