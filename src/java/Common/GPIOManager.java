/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import static Common.DebugLog.ACTUAL_DEVICE;
import static Common.DebugLog.ACTUAL_PINSTATE;
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

    public static enum PinState {
        ON(1),
        OFF(0);

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

    public static enum Port {
        GPIO0(17),
        GPIO1(18);
        // <editor-fold defaultstate="collapsed" desc="// Skip this">
        public final int value;

        private Port(int port) {
            this.value = port;
        }

        @Override
        public String toString() {
            return this.value + " ";
        }
        // </editor-fold>
    }

    public static enum Command {
        CHANGE_PIN_STATE_OUT("gpio -g mode "),
        SET("gpio -g write "),
        GET("gpio -g read ");
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
     * @param port<b>Port</b><br>
     * @return pinState: <b>PinState</b>
     * @throws IOException
     */
    public PinState getState(Port port) throws IOException {
        return executeGet(port);
    }

    /**
     * Set <b>pinState</b> to Pin 0 in Rasp Pi.
     *
     * @param port      <b>Port</b><br>
     * pinState: <b>PinState</b>
     * @param pinState
     *
     * @throws IOException
     */
    public void setState(Port port, PinState pinState) throws IOException {
        changePinModeToWrite(port);
        executeSet(port, pinState);
    }

    /**
     * Set default pin mode is Write. execute(Command.CHANGE_PIN_STATE_OUT);
     *
     * @throws IOException
     */
    private void changePinModeToWrite(Port port) throws IOException {
        if (ACTUAL_DEVICE) {
            DebugLog.log("Debugging: " + "" + CHANGE_PIN_STATE_OUT + port + "out");
            return;
        }
        Runtime.getRuntime().exec("" + CHANGE_PIN_STATE_OUT + port + "out");
    }

    // <editor-fold defaultstate="collapsed" desc="// Execute command in Linux/Ubuntu">
    private static PinState executeGet(Port port) throws java.io.IOException {
        if (ACTUAL_DEVICE) {
            DebugLog.log("Debugging: " + GET + port);
            return ACTUAL_PINSTATE;
        }
        Process proc = Runtime.getRuntime().exec("" + GET + port);
        java.io.InputStream is = proc.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String output;
        if (s.hasNext()) {
            output = s.next();
        } else {
            output = "";
        }
        int mode = Integer.parseInt(output.charAt(0) + "");
        return PinState.from(mode);
    }

    private static void executeSet(Port port, PinState pinState) throws java.io.IOException {
        if (ACTUAL_DEVICE) {
            DebugLog.log("Debugging: " + SET + port + pinState);
            ACTUAL_PINSTATE = pinState;
            return;
        }
        Runtime.getRuntime().exec("" + SET + port + pinState);
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
