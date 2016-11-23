package com.graphify.db.service.impl;

import com.graphify.db.dao.DBDao;
import com.graphify.db.dao.impl.MySQLDaoImpl;
import com.graphify.db.dao.util.DaoUtil;
import com.graphify.db.model.Schema;
import com.graphify.db.service.DBService;

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
}
