package org.ifelse2.dart.jsr;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.psi.impl.DartSuperclassImpl;
import com.jetbrains.lang.dart.psi.impl.DartVarDeclarationListImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MFile {

    public String filepath;

    public List<MClass> classes;


    public void add(MClass mClass) {

        if (classes == null)
            classes = new ArrayList<>();
        classes.add(mClass);

    }

    public static MFile fromDartFile(Project project, String filepath) {


        MFile mFile = new MFile();
        mFile.filepath = filepath;


        VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + filepath);

//        if( virtualFile == null )
//            Log.i("fromdartfile: %s not found",filepath);
//        else
//            Log.i("fromdartfile: %s ",virtualFile);


        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);

        DartFile dartFile = (DartFile) psiFile;

        Collection<DartClassDefinition> dartClasses = PsiTreeUtil.findChildrenOfAnyType(dartFile, false, DartClassDefinition.class);

        for (DartClassDefinition dartClass : dartClasses) {


            MClass mClass = new MClass();

            //DartTypeParameters typeparams = ;

            DartSuperclass dtps   = dartClass.getSuperclass();

            //Log.i("mfile darttype:%s :%s  name:%s type:%s",dtps,dtps.getText(),dtps.getName() ,dtps.getType().getText());


            mClass.name = dartClass.getComponentName().getText();

            if (dartClass.getFirstChild() instanceof DartMetadata) {

                DartMetadata dartMetadata = (DartMetadata) dartClass.getFirstChild();
                PsiElement dartmeta_first = dartMetadata.getFirstChild();

                if (dartmeta_first.getText().equals("@")) {
                    if ("jsr".equals(dartmeta_first.getNextSibling().getText()))
                        mClass.jsr = true;
                }

            }
            mClass.fields = new ArrayList<>();


            List<DartComponent> fields = dartClass.getFields();
            for (DartComponent dc : fields) {

                DartVarDeclarationListImpl dc_dlist = (DartVarDeclarationListImpl) dc.getContext();
                String type = dc_dlist.getVarAccessDeclaration().getType().getText();

                DartType dartType = dc_dlist.getVarAccessDeclaration().getType();
                String typeT = null;
                int index = type.indexOf('<');
                if( index >-1 ){
                    typeT = type.substring(index+1,type.length()-1);
                }

                if( type.startsWith("List") )
                    type = "List";
                else if( type.startsWith("Map"))
                    type = "Map";

                String name = dc.getName();

                MField mField = new MField();
                mField.name = name;
                mField.type = type;
                mField.typeT = typeT;


                mClass.fields.add(mField);

            }

            mFile.add(mClass);


        }
        return mFile;
    }
}
