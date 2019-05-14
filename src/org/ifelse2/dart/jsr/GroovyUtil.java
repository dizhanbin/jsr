
package org.ifelse2.dart.jsr;

import com.intellij.openapi.project.Project;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GroovyUtil {





    public static void run(Project project,String method,MFile mFile) throws IOException, IllegalAccessException, InstantiationException {


        ClassLoader parent = GroovyUtil.class.getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);
        String path = Data.getDataR(project);
        Class groovyClass = loader.parseClass(new File(path));
        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
        groovyObject.setProperty("project",project);
        groovyObject.invokeMethod(method, mFile);


    }




    public static void run(Project project, String method,List<String> files, List<String> mClasses) throws Exception {


        ClassLoader parent = GroovyUtil.class.getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);

        String path = Data.getDataR(project);
        Class groovyClass = loader.parseClass(new File(path));


        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();

        groovyObject.setProperty("project",project);
        groovyObject.setProperty("files",files);

        groovyObject.invokeMethod(method, mClasses);

    }



    public static void run(Project project, String filepath, String method, Map properties) throws Exception {


        ClassLoader parent = GroovyUtil.class.getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);

        Class groovyClass = loader.parseClass(new File(filepath));


        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();

        groovyObject.setProperty("project",project);

        groovyObject.invokeMethod(method, properties);

    }



    public static Object get(Project project, String filepath, String method) throws Exception {


        ClassLoader parent = GroovyUtil.class.getClassLoader();
        GroovyClassLoader loader = new GroovyClassLoader(parent);

        Class groovyClass = loader.parseClass(new File(filepath));

        GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();

        groovyObject.setProperty("project",project);

        //groovyObject.invokeMethod(method, point);
        return groovyObject.invokeMethod(method,null);

    }



}
