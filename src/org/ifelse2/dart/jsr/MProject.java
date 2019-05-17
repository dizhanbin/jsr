package org.ifelse2.dart.jsr;


import java.util.ArrayList;
import java.util.List;

public class MProject {


    List<String> files = new ArrayList<>();

    public void addFile(String path){

        if( !files.contains(path) ){
            files.add(path);
        }

    }


    public void printAll(String sep) {


        if( files != null )
        for(String f:files){

            Log.i("%s%s",sep,f);

        }

    }



    public List<String> getFiles(){


        return files;
    }


    public int indexOf(String filepath) {
        if( files == null )
            return -1;
        return files.indexOf(filepath);
    }
}
