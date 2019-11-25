/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author hoang-trung-duc
 */
public class Convert {

    // Log array of hex to console
    public static void printHexArray(byte[] arg) {
        for (int i = 0; i < arg.length; ++i) {
            System.out.println(Integer.toHexString(arg[i] & 0xff));
        }
    }

    // Byte to hex string
    public static String byteToHex(byte arg) {
        return Integer.toHexString(arg & 0xff);
    }

    // Integer to Byte Hex
    public static byte intToByte(int number) {
        return Byte.parseByte(Integer.toHexString(number), 16);
    }

    // Byte Hex to Integer
    public static int byteToInt(byte number) {
        return Integer.parseInt(Byte.toString(number), 10);
    }

    // get curent date time
    public static void curentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }

    public static byte[] hexStringToByteArray(String s) {
        if (s.contains("0x")) {
            s = s.replace("0x", "");
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

//    public static byte[] hexStringToByteArray(String hex) {
//        return Byte.pa
//    }
    public static void main(String[] args) {

        printHexArray(hexStringToByteArray("0x05"));
    }
}