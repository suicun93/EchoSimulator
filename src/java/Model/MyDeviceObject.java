/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Common.Convert;
import com.sonycsl.echo.eoj.device.DeviceObject;

/**
 *
 * @author hoang-trung-duc
 */
public abstract class MyDeviceObject extends DeviceObject {

    protected final boolean turnOn = true;
    protected final boolean turnOff = false;

    public static enum Operation {
        ON((byte) 0x30),
        OFF((byte) 0x31);

        public static Operation from(byte value) {
            return value == ON.value ? ON : OFF;
        }
        // <editor-fold defaultstate="collapsed" desc="//Skip this">
        public final byte value;

        Operation(byte operation) {
            this.value = operation;
        }
        // </editor-fold>
    }

    public String getAddressStr() {
        return getNode().getAddressStr();
    }

    public String getEOJ() {
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(String.format("%02x", getClassGroupCode()));
        sb.append(String.format("%02x", getClassCode()));
        sb.append(String.format("%02x", getInstanceCode()));
        return new String(sb);
    }

    // ecl_profile 0x80,0x31,0xff,0x31,0xfd,0x1900,0xfe,0x1900
    public String getProperty(byte[] EPCs) throws Exception {
        StringBuilder sbuilder = new StringBuilder();
        for (byte EPC : EPCs) {

            byte[] res = getProperty(EPC);
            if (res == null) {
                throw new Exception(Convert.byteToHex(EPC) + " not found");
            }
            sbuilder.append("0x").append(String.format("%02x", EPC)).append(",");
            sbuilder.append("0x");
            for (byte re : res) {
                sbuilder.append(String.format("%02x", re));
            }
            sbuilder.append(",");
        }
        sbuilder = sbuilder.deleteCharAt(sbuilder.length() - 1);
        return new String(sbuilder);
    }
}
