package script;

import com.intellij.openapi.project.Project;
import org.ifelse2.dart.jsr.MFile;
import org.ifelse2.dart.jsr.MClass;
import org.ifelse2.dart.jsr.MField;

import groovy.text.VLTemplateEngine;


/*
public class MFile {
    public String filepath;
    public List<MClass> classes;
}

public class MClass {
    public String name;
    public boolean jsr;
    public List<MField> fields;
}
public class MField {
    public String type;
    public String name;
    public String typeT;
}

 */

class R {


    Project project;
    List<String> files;

    void onDartChanged(MFile mfile){

        String filename = new File(mfile.filepath).getName();

        Map binding = new HashMap();
        binding.put("classes", mfile.classes);
        binding.put("filepath", mfile.filepath.replace(project.getBasePath(),""));

        String tplpath = project.getBasePath() + "/iedata/jsr/dart_class.tpl";

        def tpl = new File(tplpath);
        def engine = new VLTemplateEngine();
        def template = engine.createTemplate(tpl).make(binding);


        String dir = project.getBasePath() + "/lib/jsr/";
        save(dir,"R_"+filename,template.toString(),true);

        

    }

    void refreshClass(List<MClass> mClasses){


        Map binding = new HashMap();

        binding.put("files", files);
        binding.put("classname","classname");
        binding.put("classes",mClasses);


        String tplpath = project.getBasePath() + "/iedata/jsr/dart_class_factory.tpl";
        def tpl = new File(tplpath);
        def engine = new VLTemplateEngine();
        def template = engine.createTemplate(tpl).make(binding);


        String dir = project.getBasePath() + "/lib/jsr/";
        save(dir,"Class.dart",template.toString(),true);



    }

    void createDart(MClass mClass,String filename){


        Map binding = new HashMap();

        binding.put("name", mClass.name);
        binding.put("fields", mClass.fields);
        binding.put("jsr",mClass.jsr);

        String tplpath = project.getBasePath() + "/iedata/jsr/dart_class.tpl";



        def tpl = new File(tplpath);
        def engine = new VLTemplateEngine();
        def template = engine.createTemplate(tpl).make(binding);


        String dir = project.getBasePath() + "/lib/jsr/";
        save(dir,"R_"+filename,template.toString(),true);




    }



    void save(String dir,String filename,String txt,boolean replace){


        def filedir = new File(dir);
        if( !filedir.exists() )
            filedir.mkdirs();

        String srcpath = dir+"/"+filename;

        def file = new File(srcpath);

        if (file.exists() )
        {
            if( replace )
                file.delete()
            else
                return;
        }
        def printWriter = file.newPrintWriter() //
        printWriter.write(txt)
        printWriter.flush()
        printWriter.close()

        //log("save ->"+srcpath);

    }



}
