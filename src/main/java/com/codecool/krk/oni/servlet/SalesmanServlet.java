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
        String idString = request.getParameter("id");

        response.setContentType("application/json");

        try {
            SalesmanDao salesmanDao = new SalesmanDao();
            String content;
            if (idString == null) {
                content = getAllSalemenJSON(salesmanDao);
            } else {
                content = getSalemanJSON(salesmanDao, Integer.valueOf(idString));
            }

            if (content != null) {
                response.getWriter().write(content);
            } else {
                send404(response, "Error 404: There is no salesman of such id");
            }

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String salary = request.getParameter("salary");
        String birthYear = request.getParameter("birth_year");

        if (name == null || salary == null || birthYear == null) {
            send400(response, "400: No complete data to add new salesman");
        } else {
            Salesman salesman = new Salesman(name, Integer.valueOf(salary), Integer.valueOf(birthYear));

            try {
                SalesmanDao salesmanDao = new SalesmanDao();
                salesmanDao.save(salesman);
                send200(response, "200: Add new salesman to database");
            } catch (DaoException e) {
                e.printStackTrace();
            }
        }
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
        String idString = request.getParameter("id");

        try {
            SalesmanDao salesmanDao = new SalesmanDao();
            if (getSaleman(salesmanDao, Integer.valueOf(idString)) != null) {
                salesmanDao.delete(Integer.valueOf(idString));
                send200(response, String.format("200: Saleman with id: %s deleted", idString));
            } else {
                send404(response, "404: No such salesman in database");
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    private String getAllSalemenJSON(SalesmanDao salesmanDao) throws DaoException {
        JSONArray array = new JSONArray();
        for (Salesman salesman: salesmanDao.getAllSalesmen()) {
            array.put(salesman.toJSON());
        }
        return array.toString();
    }

    private String getSalemanJSON(SalesmanDao salesmanDao, Integer id) throws DaoException {
        String content = null;
        Salesman salesman = getSaleman(salesmanDao, id);
        if (salesman != null) {
            content = salesman.toJSON().toString();
        }

        return content;
    }

    private Salesman getSaleman(SalesmanDao salesmanDao, Integer id) throws DaoException{
        return salesmanDao.getSalesman(id);
    }

    private void send404(HttpServletResponse response, String message) throws IOException {
        response.setStatus(404);
        response.setContentType("text/plain");
        response.getWriter().write(message);
    }

    private void send200(HttpServletResponse response, String message) throws IOException {
        response.setStatus(200);
        response.setContentType("text/plain");
        response.getWriter().write(message);
    }

    private void send400(HttpServletResponse response, String message) throws IOException {
        response.setStatus(400);
        response.setContentType("text/plain");
        response.getWriter().write(message);
    }
}