/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

/**
 *
 * @author hoang-trung-duc
 */
public class Config {

    public static void save(String fileName, String time, String mode, String d3) throws Exception {
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
        String[] time2 = time.split("\\:");
        int hour = Integer.parseInt(time2[0]);
        int minute = Integer.parseInt(time2[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // Save config
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("/opt/tomcat/webapps/Simulator/" + fileName))) {
            writer.write(time + "," + mode + "," + d3);
        }
    }

    public static String load(String filename) throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get("/opt/tomcat/webapps/Simulator/" + filename));
        return new String(encoded, StandardCharsets.UTF_8);
    }
}
