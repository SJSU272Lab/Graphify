package com.graphify.db.util;

import com.graphify.db.dao.impl.MySQLDaoImpl;
import com.graphify.db.dao.util.DaoUtil;
import com.graphify.db.model.mysql.Column;
import com.graphify.db.model.mysql.Constraint;
import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Table;

import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Sushant on 23-11-2016.
 */
public class Mapper {

    private Schema schema;
    private Map<String, String> tableVertexMap;
    private Map<String, Map<String, String>> mapData;
    private Map<String, String> edges;


    public Mapper(Schema schema) {
        this.schema = schema;
        tableVertexMap = new HashMap<>();
        //Ordering matters when dealing with tables with foreign keys
        //as we need to have the vertexes created before you create edges
        mapData = new LinkedHashMap<>();
        edges = new HashMap<>();
        initMapper();
    }

    private void initMapper() {
        Map<String, Boolean> tableMappingDone = new HashMap<>();
        for (Table table : schema.getTables()) {
            tableMappingDone.put(table.getName(), false);
            tableVertexMap.put(table.getName().toLowerCase(), table.getName().toLowerCase());
        }
        for (Table table : schema.getTables()) {
            mapTable(table, tableMappingDone);
        }
        mapEdges(schema);
    }

    private void mapEdges(Schema schema) {
        for(Constraint constraint: ServiceUtil.getForeignKeys(schema)) {
            edges.put(constraint.getName(), "refers_"+constraint.getRefTable().toLowerCase());
        }
    }

    private void mapTable(Table table, Map<String, Boolean> tableMappingDone) {
        if (!ServiceUtil.hasForeignKeys(table) && !tableMappingDone.get(table.getName())) {
            mapColumns(table, tableMappingDone);
        } else {
            mapDependentTables(tableMappingDone, table);
        }
    }

    private void mapColumns(Table table, Map<String, Boolean> tableMappingDone) {
        Map<String, String> columnMap = new HashMap<>();
        for (Column column : table.getColumns()) {
            if (!ServiceUtil.isForeignKey(column.getName(), table)) {
                columnMap.put(column.getName().toLowerCase(), column.getName().toLowerCase());
            }
        }
        mapData.put(table.getName().toLowerCase(), columnMap);
        tableMappingDone.put(table.getName(), true);
    }

    private void mapDependentTables(Map<String, Boolean> tableMappingDone, Table table) {
        for (Constraint constraint : ServiceUtil.getForeignKeys(table)) {
            String refTable = constraint.getRefTable();
            if (!tableMappingDone.get(refTable)) {
                mapTable(ServiceUtil.getMySQLTableByName(refTable, schema), tableMappingDone);
            }
        }
        mapColumns(table, tableMappingDone);
    }

    public static void main(String[] args) {
        Connection connection = DaoUtil.getConnection("jdbc:mysql://localhost/expense?" +
                "user=root&password=admin");
        MySQLDaoImpl impl = new MySQLDaoImpl();
        Schema schema = impl.getSchema(connection, "expense");
        Mapper mapper = new Mapper(schema);
        System.out.println(mapper);
    }

    @Override
    public String toString() {
        return "Mapper{" +
                "schema=" + schema +
                ", tableVertexMap=" + tableVertexMap +
                ", mapData=" + mapData +
                ", edges=" + edges +
                '}';
    }

    public Map<String, String> getTableVertexMap() {
        return tableVertexMap;
    }

    public Map<String, Map<String, String>> getMapData() {
        return mapData;
    }

    public Map<String, String> getEdges() {
        return edges;
    }
}
