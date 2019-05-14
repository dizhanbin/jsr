package org.ifelse2.dart.jsr;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class Icons {

    public static Icon getIcon(String path){
        return IconLoader.getIcon(path, Icons.class);
    }

    public static Icon getLogo(){

        return IconLoader.getIcon("/icons/logo.png", Icons.class);
    }


}
