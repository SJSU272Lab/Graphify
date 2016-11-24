package com.graphify.db.service;

import com.graphify.db.model.mysql.Schema;

/**
 * Created by Sushant on 22-11-2016.
 */
public interface DBService {

    Schema getDBSchema(String url, String schemaName);
}
