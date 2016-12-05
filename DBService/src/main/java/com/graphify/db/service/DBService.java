package com.graphify.db.service;

import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Validate;

/**
 * Created by Sushant on 22-11-2016.
 */
public interface DBService {

    Schema getDBSchema(String url, String schemaName);

    Validate validateSchema(String url, String schemaName, String fileLocation);

    GraphSchema convertDB(String url, String schemaName);

    GraphSchema convertAndAdd(String url, String schemaName, String fileLocation);
}
