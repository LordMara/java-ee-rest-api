package com.codecool.krk.oni.service;

import com.codecool.krk.oni.dao.SalesmanDao;
import com.codecool.krk.oni.exception.DaoException;
import com.codecool.krk.oni.exception.NoCompleteDataProvideException;
import com.codecool.krk.oni.exception.NoSuchSalesmanException;
import com.codecool.krk.oni.model.Salesman;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalesmanService {
    private SalesmanDao salesmanDao;
    private ObjectMapper objectMapper = new ObjectMapper();

    public SalesmanService() throws DaoException {
        this.salesmanDao = new SalesmanDao();
    }

    public String getSalesman(String idString) throws NumberFormatException, NoSuchSalesmanException, DaoException, JsonProcessingException {
        String content;

        if (idString == null) {
            content = getAllSalesmenJSON();
        } else {
            content = getSalesmanJSON(Integer.parseInt(idString));
        }
        return content;
    }

    public void postSalesman(String json) throws ClassCastException,
            NoCompleteDataProvideException, DaoException , IOException{
        Map<String, Object> jsonMap = objectMapper.readValue(json,
                new TypeReference<Map<String,Object>>(){});

        if (!jsonMap.containsKey("name") || !jsonMap.containsKey("salary") || !jsonMap.containsKey("birthYear")) {
            throw new NoCompleteDataProvideException("No all date for new salesman provided");
        }
        // Wrong exception throw
        Salesman salesman = new Salesman((String) jsonMap.get("name"), (Integer) jsonMap.get("salary"),
                (Integer) jsonMap.get("birthYear"));
        salesmanDao.save(salesman);
    }

    public void putSalesman(String idString, String name, String salary, String birthYear) throws NumberFormatException,
            NoSuchSalesmanException, NoCompleteDataProvideException, DaoException {
        if (idString == null) {
            throw new NoSuchSalesmanException("Salesman id not specified");
        }

        if (name == null || salary == null || birthYear == null) {
            throw new NoCompleteDataProvideException("No all date for update salesman provided");
        }

        Salesman salesman = getSalesman(Integer.parseInt(idString));

        updateSalesman(salesman, name, salary, birthYear);
        salesmanDao.update(salesman);
    }

    public void deleteSalesman(String idString) throws NumberFormatException, NoSuchSalesmanException, DaoException {
        if (idString == null) {
            throw new NoSuchSalesmanException("Salesman id not specified");
        }
        Integer id = Integer.parseInt(idString);
        getSalesman(id);
        this.salesmanDao.delete(id);
    }

    private String getAllSalesmenJSON() throws DaoException, JsonProcessingException {
        List<Salesman> salesmen = new ArrayList<>();
        for (Salesman salesman: this.salesmanDao.getAllSalesmen()) {
            salesmen.add(salesman);
        }
        return this.objectMapper.writeValueAsString(salesmen);

    }

    private String getSalesmanJSON(Integer id) throws DaoException, NoSuchSalesmanException, JsonProcessingException {
        Salesman salesman = getSalesman(id);
        return this.objectMapper.writeValueAsString(salesman);
    }

    private Salesman getSalesman(Integer id) throws DaoException, NoSuchSalesmanException {
        Salesman salesman = this.salesmanDao.getSalesman(id);

        if (salesman == null) {
            throw new NoSuchSalesmanException(String.format("No salesman with id %d in database", id));
        }
        return salesman;
    }

    private void updateSalesman(Salesman salesman, String name, String salary, String birthYear) throws NumberFormatException {
        salesman.setName(name);
        salesman.setSalary(Integer.parseInt(salary));
        salesman.setBirthYear(Integer.parseInt(birthYear));
    }
}
