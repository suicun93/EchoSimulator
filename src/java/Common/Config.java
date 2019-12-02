/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

/**
 *
 * @author hoang-trung-duc
 */
public class Config {

    private static final boolean runOnLinux = true;

    public static String getLink() {
        if (runOnLinux) {
            return "/opt/tomcat/webapps/";
        } else {
            return "";
        }
    }

    public static void save(String fileName, String content) throws Exception {

        try {
            // Save config
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(getLink() + fileName))) {
                writer.write(content);
            }
        } catch (Exception e) {
            throw new Exception(Config.class.getName() + ", Save: " + e.getMessage());
        }
    }

    public static void save(String fileName, String startTime, String endTime, String mode, String d3) throws Exception {
        try {
            // Validate mode
            if (!mode.equalsIgnoreCase("0x41") && !mode.equalsIgnoreCase("0x44") && !mode.equalsIgnoreCase("0x42")) {
                throw new Exception("Wrong mode");
            }

            // Validate d3
            int d3Int = Integer.parseInt(d3);
            if (d3Int <= 0) {
                throw new Exception("Wrong instantaneous.");
            }

            // Validate time
            String[] timeStart = startTime.split("\\:");
            int hour = Integer.parseInt(timeStart[0]);
            int minute = Integer.parseInt(timeStart[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            String[] timeEnd = endTime.split("\\:");
            hour = Integer.parseInt(timeEnd[0]);
            minute = Integer.parseInt(timeEnd[1]);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            // Save config
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(getLink() + fileName))) {
                writer.write(startTime + "," + endTime + "," + mode + "," + d3);
            }
        } catch (IOException e) {
            throw new Exception(Config.class.getName() + ", Save Failed: " + e.getMessage());
        }
    }

    public static String load(String filename) throws Exception {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(getLink() + filename));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Exception(Config.class.getName() + ", Load Failed: " + e.getMessage());
        }
    }
}
