package com.graphify.db.client;

import com.graphify.db.model.mysql.Validate;
import com.graphify.db.service.DBService;
import com.graphify.db.service.impl.DBServiceImpl;

/**
 * Created by Sushant on 01-12-2016.
 */
public class LocalClient {
    private static final DBService DB_SERVICE = new DBServiceImpl();

    public Validate validate(String dbUrl, String fileLocation, String schemaName) {
        Validate validate = null;
        try {
            validate =  DB_SERVICE.validateSchema(dbUrl, fileLocation, schemaName);
            System.out.println(LocalClient.class.getCanonicalName() + "   " +validate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validate;
    }
}