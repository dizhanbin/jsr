package org.ifelse2.dart.jsr;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectCoreUtil;

public class ProjectUtil {



    public static Project fromFile(String path){


       return  ProjectCoreUtil.theOnlyOpenProject();

    }
}
