/*
 * This source file was generated by the Gradle 'init' task
 */
package chief.of.graph;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

import chief.of.graph.containers.MainFrame;

public class App {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        Runnable initFrame = MainFrame::new;
        SwingUtilities.invokeAndWait(initFrame);
    }
}
