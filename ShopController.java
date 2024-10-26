/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Categories;
import entity.Products;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Vector;
import model.DAOCategories;
import model.DAOProducts;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ShopController", urlPatterns = {"/ShopURL"})
public class ShopController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        DAOProducts daoP = new DAOProducts();
        DAOCategories daoC = new DAOCategories();
        try (PrintWriter out = response.getWriter()) {
            String service = request.getParameter("service");

            if (service == null) {
                service = "listAllProducts";
            }
            if (service.equals("withCategories")) {
                String sqlC = "SELECT * FROM Categories";
                Vector<Categories> vectorC = daoC.getCategory(sqlC);
                request.setAttribute("vectorC", vectorC);
                request.getRequestDispatcher("shop.jsp").forward(request, response);

            }
           

            if (service.equals("listAllProWithCate")) {
                String cidParam = request.getParameter("cidParam");
                if(cidParam != null && cidParam.isEmpty()){
                int cid = Integer.parseInt(request.getParameter("cid"));
                String sqlProduct = "SELECT * FROM Products WHERE CategoryID=" + cid;
                Vector<Products> vectorPC = daoP.getProduct(sqlProduct);
                request.setAttribute("vectorPC", vectorPC);
               }
                else{
                String sqlP = "SELECT * FROM Products";
                Vector<Products> vectorP = daoP.getProduct(sqlP);
                request.setAttribute("vectorP", vectorP);
                 
                }
                 request.getRequestDispatcher("shop.jsp").forward(request, response);
                  

            }
            if (service.equals("pagination")) {
                // lay so luong san pham tren moi trang
                int itemPerPage = 6;

                // lay trang hien tai tu request khong thi mac dinh la 1
                int currentPage = 1;
                if (request.getParameter("page") != null) {
                    currentPage = Integer.parseInt(request.getParameter("page"));
                }

                int totalItems = daoP.countTotalProducts();//16

                // tinh toan so trang
                int totalPages = (int) Math.ceil((double) totalItems / itemPerPage);

                // tinh toan offset de lay san pham cho trang hien tai
                int offset = (currentPage - 1) * itemPerPage;

                Vector<Products> vectorPC = daoP.getProductsByPage(offset, itemPerPage);

                request.setAttribute("totalPages", totalPages);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("vectorPC", vectorPC);
                request.getRequestDispatcher("shop.jsp").forward(request, response);
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
