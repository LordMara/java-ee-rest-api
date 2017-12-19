package com.codecool.krk.oni.servlet;

import com.codecool.krk.oni.dao.SalesmanDao;
import com.codecool.krk.oni.exception.DaoException;
import com.codecool.krk.oni.model.Salesman;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/salesmen/*"})
public class SalesmanServlet extends HttpServlet {

    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Salesman salesman = null;
        try {
            SalesmanDao salesmanDao = new SalesmanDao();
            salesman = salesmanDao.getSalesman(1);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        JSONArray array = new JSONArray();
        array.put(salesman.toJSON());
        array.put(salesman.toJSON());

        response.getWriter().write(array.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Salesman salesman = new Salesman("Maciej", 20000, 1984);
        try {
            SalesmanDao salesmanDao = new SalesmanDao();
            salesmanDao.save(salesman);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        response.getWriter().write("salesman");
    }

    protected void doPut( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Salesman salesman = new Salesman(1,"John Smith", 20000, 1984);
        try {
            SalesmanDao salesmanDao = new SalesmanDao();
            salesmanDao.update(salesman);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        response.getWriter().write("salesman");
    }

    protected void doDelete( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            SalesmanDao salesmanDao = new SalesmanDao();
            salesmanDao.delete(5);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        response.getWriter().write("salesman");
    }
}