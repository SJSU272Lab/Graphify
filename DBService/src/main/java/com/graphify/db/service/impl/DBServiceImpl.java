package com.graphify.db.service.impl;

import com.graphify.db.dao.DBDao;
import com.graphify.db.dao.impl.MySQLDaoImpl;
import com.graphify.db.dao.util.DaoUtil;
import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Table;
import com.graphify.db.model.mysql.Validate;
import com.graphify.db.service.DBService;
import com.graphify.db.util.CSVUtil;
import com.graphify.db.util.ServiceUtil;
import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Sushant on 22-11-2016.
 */
public class DBServiceImpl implements DBService {

    private static final DBDao dbDao = new MySQLDaoImpl();

    @Override
    public Schema getDBSchema(String url, String schemaName) {
        Schema schema = dbDao.getSchema(DaoUtil.getConnection(url), schemaName);
        //System.out.println(schema);
        return schema;
    }

    @Override
    public Validate validateSchema(String url, String schemaName, String fileLocation) {
        Schema schema = dbDao.getSchema(DaoUtil.getConnection(url), schemaName);
        Validate validate = new Validate();

        for (Table table : schema.getTables()) {
            String filename = fileLocation + "/" + table.getName() + ".csv";
            String[] header = CSVUtil.getHeaderFromSource(filename);
            if (header == null) {
                //System.out.println(DBService.class.getCanonicalName() + " Got null ");
                validate.setMessage("Could not find valid CSV for table '" + table.getName() + "'");
                validate.setValid(false);
                return validate;
            }
            if (header.length == table.getColumns().size()) {
                for (int i = 0; i < header.length; i++) {
                    if (!ServiceUtil.hasColumn(table, header[i])) {
                        validate.setMessage("CSV File for table " + table.getName() + " has unexpected column '" + header[i] + "'");
                        validate.setValid(false);
                        return validate;
                    }
                }
            } else {
                validate.setMessage("CSV File for table '" + table.getName() + "' has missing/invalid column(s) than expected");
                validate.setValid(false);
                return validate;
            }
        }
        validate.setMessage("Done");
        validate.setValid(true);

        return validate;


    }

    public static void main(String[] args) {
        DBServiceImpl dbService = new DBServiceImpl();
        String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin";
        Validate validate = dbService.validateSchema(url, "expense", "D:/DevEnv/Fall16-Team12/DBService/src/main/resources/mysql");
        System.out.println(DBServiceImpl.class.getCanonicalName() + validate);

    }
}
