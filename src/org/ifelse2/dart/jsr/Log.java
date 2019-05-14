
package org.ifelse2.dart.jsr;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectCoreUtil;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

public class Log {


    private static final String WIN_TAG = "jsr";

    public static void i(String format, Object ... args){

        System.out.println(  String.format(format,args) );

        consoleError(ProjectCoreUtil.theOnlyOpenProject(),String.format(format,args));

    }
    public static void consoleError(Project project, String format, Object ... args) {

        if( project == null )
            return;

        ConsoleViewImpl console = getConsoleView(project);
        String msg = String.format(format,args);
        console.print(msg, ConsoleViewContentType.NORMAL_OUTPUT);
        console.print("\n", ConsoleViewContentType.NORMAL_OUTPUT);
        console.requestScrollingToEnd();

    }

    static  ConsoleViewImpl consoleView;
    public static ConsoleViewImpl getConsoleView(Project project) {


        ToolWindowManager manager = ToolWindowManager.getInstance(project);
        ToolWindow window = manager.getToolWindow(WIN_TAG);

        if( consoleView == null ) {

            consoleView = new ConsoleViewImpl(project, true);

            try {

                manager.unregisterToolWindow(WIN_TAG);
                window = manager.registerToolWindow(WIN_TAG, consoleView.getComponent(), ToolWindowAnchor.BOTTOM);
                window.setIcon(Icons.getLogo());

            } catch (Exception ee) {
                ee.printStackTrace();
            }

        }

        // window.setTitle(WIN_TAG);
        if( window != null ) {
            window.show(null);
        }

        return consoleView;


    }

}
