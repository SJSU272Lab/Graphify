package com.graphify.db.dao;

import com.graphify.db.model.Schema;
import com.graphify.db.model.Table;

import java.sql.Connection;
import java.util.List;

/**
 * Created by Sushant on 21-11-2016.
 */
public interface DBDao {

    Schema getSchema(Connection connection, String schemaName);

}
