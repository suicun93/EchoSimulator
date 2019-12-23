/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import static Common.DebugLog.ACTUAL_DEVICE;
import Model.MyDeviceObject.Operation;
import java.io.IOException;
import static Common.GPIOManager.Command.SET;
import static Common.GPIOManager.Command.GET;
import static Common.GPIOManager.Command.CHANGE_PIN_STATE_OUT;

/**
 * Class controls GPIO pin 0.
 *
 * @author hoang-trung-duc
 */
public class GPIOManager {

    private final static int OFF_VALUE = 0;
    private final static int ON_VALUE = 1;

    public static enum PinState {
        ON(ON_VALUE),
        OFF(OFF_VALUE);

        public static PinState from(int state) {
            return state == ON.value ? ON : OFF;
        }

        public static PinState from(byte[] operation) {
            return operation[0] == Operation.ON.value
                    ? ON : OFF;
        }

        // <editor-fold defaultstate="collapsed" desc="// Skip this">
        public final int value;

        private PinState(int state) {
            this.value = state;
        }

        @Override
        public String toString() {
            return "" + this.value;
        }
        // </editor-fold>
    }

    public static enum Command {
        CHANGE_PIN_STATE_OUT("gpio -g mode 17 out"),
        SET("gpio -g write 17 "),
        GET("gpio -g read 17");
        // <editor-fold defaultstate="collapsed" desc="// Skip this">

        public final String value;

        private Command(String command) {
            this.value = command;
        }

        @Override
        public String toString() {
            return value;
        }
        // </editor-fold>
    }

    /**
     * Get <b>pinState</b> from Pin 0 in Rasp Pi.
     *
     * @return pinState: <b>PinState</b>
     * @throws IOException
     */
    public PinState get() throws IOException {
        String output = execute(GET);
        int mode = Integer.parseInt(output.charAt(0) + "");
        return PinState.from(mode);
    }

    /**
     * Set <b>pinState</b> to Pin 0 in Rasp Pi.
     *
     * @param pinState: <b>PinState</b>
     * @throws IOException
     */
    public void set(PinState pinState) throws IOException {
        execute(SET, pinState);
    }

    /**
     * Set default pin mode is Write. execute(Command.CHANGE_PIN_STATE_OUT);
     *
     * @throws IOException
     */
    public void changePinModeToWrite() throws IOException {
        execute(CHANGE_PIN_STATE_OUT);
    }

    // <editor-fold defaultstate="collapsed" desc="// Execute command in Linux/Ubuntu">
    private static String execute(Command cmd) throws java.io.IOException {
        if (!ACTUAL_DEVICE) {
            DebugLog.log("Debugging: " + cmd);
            return "1";
        }
        Process proc = Runtime.getRuntime().exec(cmd + "");
        java.io.InputStream is = proc.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String val;
        if (s.hasNext()) {
            val = s.next();
        } else {
            val = "";
        }
        return val;
    }

    private static String execute(Command cmd, PinState pinState) throws java.io.IOException {
        if (!ACTUAL_DEVICE) {
            DebugLog.log("Debugging: " + cmd);
            return "1";
        }
        Process proc = Runtime.getRuntime().exec(cmd + "" + pinState);
        java.io.InputStream is = proc.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String val;
        if (s.hasNext()) {
            val = s.next();
        } else {
            val = "";
        }
        return val;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="// Instance/ Singleton design pattern">
    private GPIOManager() {
    }

    private static GPIOManager instance = null;

    public static GPIOManager getInstance() {
        if (instance == null) {
            instance = new GPIOManager();
        }
        return instance;
    }
    // </editor-fold>
}
