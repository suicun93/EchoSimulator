/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Common.Config;
import Main.EchoController;
import Model.JSONConvertable;
import com.sonycsl.echo.eoj.device.DeviceObject;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author hoang-trung-duc
 */
public class GetAllItems extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
        PrintWriter out = null;
        try {
            out = response.getWriter();

            StringBuilder responseString = new StringBuilder("{\n"
                    + "   \"success\":\"OK\",\n");
            responseString.append("   \"devices\":{\n");
            if (!EchoController.listDevice().isEmpty()) {
                EchoController.listDevice().forEach((DeviceObject deviceObject) -> {
                    if (deviceObject instanceof JSONConvertable) {
                        // Write to response
                        responseString.append(((JSONConvertable) deviceObject).toJSON());
                    }
                    responseString.append("      ,\n");
                });
                responseString.deleteCharAt(responseString.length() - 1);
                responseString.deleteCharAt(responseString.length() - 1);
            }
            responseString.append("   }\n"
                    + "}");

            out.print(responseString.toString());

        } catch (IOException ex) {
            System.out.println(GetAllItems.class.getName() + " " + ex.getMessage());
            if (out != null) {
                out.print("{\n"
                        + "\"success\":\"" + ex.getMessage() + "\"\n"
                        + "}");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

}
