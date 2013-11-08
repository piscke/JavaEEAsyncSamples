/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import piscke.business.component.RecisaoComponent;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Future;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author leandro.piscke
 */
@WebServlet(urlPatterns = {"/DemitirAssincrono"})
public class DemitirAssincrono extends HttpServlet {

    @EJB
    private RecisaoComponent recisao;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            HttpSession session = request.getSession(true);
            Future<String> future = (Future<String>) session.getAttribute("future");

            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DemitirAssincrono</title>");
            out.println("</head>");
            out.println("<body>");
            if (request.getParameter("s") != null && future != null) {
                out.println("<h1>Status</h1>");
                out.println("<h4>" + future.toString()+ "</h4>");

            } else if (request.getParameter("c") != null && future != null) {
                future.cancel(true);
                out.println("<h1>O processo foi cancelado!</h1>");
            } else {
                long funcionarioId = Long.parseLong(request.getQueryString());
                future = recisao.demitirAssincrono(funcionarioId);
                session.setAttribute("future", future);

                out.println("<h1>Foi disparado a demissão do funcionario " + request.getQueryString() + " de forma assíncrona!</h1>");
            }
            out.println("<a href='.\\'>Home</a>");
            out.println("<a href='.\\DemitirAssincrono?s'>Status</a>");
            out.println("<a href='.\\DemitirAssincrono?c'>Cancelar</a>");
            out.println("</body>");
            out.println("</html>");
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
