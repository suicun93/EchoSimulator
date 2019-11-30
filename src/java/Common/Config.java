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
            // Linux
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("/opt/tomcat/webapps/" + fileName))) {
                // Window
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

                writer.write(startTime + "," + endTime + "," + mode + "," + d3);
            }
        } catch (IOException e) {
            throw new Exception(Config.class.getName() + ", Save: " + e.getMessage());
        }
    }

    public static String load(String filename) throws Exception {
        try {
            // Linux
            byte[] encoded = Files.readAllBytes(Paths.get("/opt/tomcat/webapps/" + filename));
            // Window        
//            byte[] encoded = Files.readAllBytes(Paths.get(filename));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new Exception(Config.class.getName() + ", Load: " + e.getMessage());
        }
    }
}
