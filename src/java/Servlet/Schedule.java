/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Common.Config;
import Common.DebugLog;
import Main.EchoController;
import Model.MyBattery;
import Model.MyElectricVehicle;
import Model.MySolar;
import com.sonycsl.echo.eoj.device.DeviceObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hoang-trung-duc
 */
public class Schedule extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;

        try {
            String paramsRequest = getParam(request);
            out = response.getWriter();

            String[] params = paramsRequest.split("\\,");
            if (params.length != 5) {
                out.print("Not enough param: " + paramsRequest);
                return;
            }
            String deviceName = params[0];
            String startTime = params[1];
            String endTime = params[2];
            String mode = params[3];
            String d3 = params[4];
            setup(deviceName, startTime, endTime, mode, d3);
            out.print("success");
        } catch (Exception ex) {
            DebugLog.log(ex);
            if (out != null) {
                out.print(Schedule.class.getName() + " " + ex.getMessage());
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request
     * @return
     * @throws java.io.IOException
     */
    public static String getParam(HttpServletRequest request) throws IOException {
        String body;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void setup(String deviceName, String startTime, String endTime, String mode, String d3) throws Exception {
        // Save config and schedule
        DeviceObject device;
        if (deviceName.equalsIgnoreCase(MyBattery.name)) {
            Config.save(deviceName + ".txt", startTime, endTime, mode, d3);
            device = EchoController.BATTERY;
            ((MyBattery) device).schedule();
            return;
        }
        if (deviceName.equalsIgnoreCase(MyElectricVehicle.name)) {
            Config.save(deviceName + ".txt", startTime, endTime, mode, d3);
            device = EchoController.EV;
            ((MyElectricVehicle) device).schedule();
            return;
        }
        if (deviceName.equalsIgnoreCase(MySolar.name)) {
            Config.save(deviceName + ".txt", startTime, endTime, mode, d3);
            device = EchoController.SOLAR;
            ((MySolar) device).schedule();
            return;
        }
        throw new Exception("No device");
    }
}
