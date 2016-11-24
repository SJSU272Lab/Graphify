package com.graphify.db.dao;

import com.graphify.db.model.mysql.Schema;

import java.sql.Connection;

/**
 * Created by Sushant on 21-11-2016.
 */
public interface DBDao {

    Schema getSchema(Connection connection, String schemaName);

}
