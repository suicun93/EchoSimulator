/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

/**
 *
 * @author hoang-trung-duc
 */
public class DebugLog {

    public final static boolean DEBUG = true;
    public final static boolean ACTUAL_DEVICE = false;
//    public final static boolean ACTUAL_DEVICE = true;
    public static GPIOManager.PinState ACTUAL_PINSTATE = GPIOManager.PinState.OFF;

    public static void log(String message) {
        if (DEBUG) {
            StackTraceElement t = new Exception().getStackTrace()[2];
            String fullClassName = t.getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = t.getMethodName();
            int lineNumber = t.getLineNumber();
            System.out.println(className + "." + methodName + "():line " + lineNumber + ", message: " + message);
        }
    }

    public static void log(Exception e) {
        if (DEBUG) {
            StackTraceElement t = e.getStackTrace()[0];
            String fullClassName = t.getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = t.getMethodName();
            int lineNumber = t.getLineNumber();
            System.out.println(className + "." + methodName + "():line " + lineNumber + ", message: " + e.getMessage());
        }
    }
}
