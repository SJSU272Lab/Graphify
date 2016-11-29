package com.graphify.db.util;

import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.ibm.graph.Property;
import com.graphify.db.model.mysql.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Sushant on 23-11-2016.
 */
public class ServiceUtil {

    public static Table getMySQLTableByName(String tableName, Schema schema) {
        if (tableName == null || schema == null || schema.getTables() == null || schema.getTables().size() == 0) {
            //System.out.println(ServiceUtil.class.getCanonicalName() + " Returning null!! ");
            return null;
        }
        List<Table> tables = schema.getTables();
        //System.out.println(ServiceUtil.class.getCanonicalName() + " Got " + tables);
        for (Table table : tables) {
            //System.out.println(ServiceUtil.class.getCanonicalName() + " Got " + table);
            if (tableName.toLowerCase().equals(table.getName().toLowerCase())) {
                return table;
            }
        }
        return null; //Not found
    }

    public static List<Constraint> getPrimaries(Table table) {
        if (table == null || table.getColumns() == null || table.getColumns().size() == 0
                || table.getConstraints() == null || table.getConstraints().size() == 0) {
            return null;
        }
        List<Constraint> constraints = new ArrayList<>();
        for (Constraint constraint : table.getConstraints()) {
            //MySQL sets it as "PRIMARY KEY", so it is safe to use like this, unless they change this.
            if ("PRIMARY KEY".equals(constraint.getType())) {
                constraints.add(constraint);
            }
        }
        return constraints; //Not found
    }

    public static List<String> getPrimaryColumns(Table table) {
        if (table == null || table.getColumns() == null || table.getColumns().size() == 0
                || table.getConstraints() == null || table.getConstraints().size() == 0) {
            return null;
        }
        List<String> primaryColumns = new ArrayList<>();
        for (Constraint constraint : table.getConstraints()) {
            //MySQL sets it as "PRIMARY KEY", so it is safe to use like this, unless they change this.
            if ("PRIMARY KEY".equals(constraint.getType())) {
                primaryColumns.add(constraint.getColumn().toLowerCase());
            }
        }
        return primaryColumns; //Not found
    }

    public static List<Constraint> getForeignKeys(Table table) {
        if (table == null || table.getConstraints() == null || table.getConstraints().size() == 0) {
            return null;
        }
        List<Constraint> constraints = new ArrayList<>();
        for (Constraint constraint : table.getConstraints()) {
            //MySQL sets it as "FOREIGN KEY", so it is safe to use like this, unless they change this.
            if ("FOREIGN KEY".equals(constraint.getType())) {
                constraints.add(constraint);
            }
        }
        return constraints; //Not found
    }


    public static Property getPropertyByName(String propertyName, GraphSchema graphSchema) {
        if (graphSchema == null || graphSchema.getPropertyKeys() == null || graphSchema.getPropertyKeys().size() == 0) {
            return null;
        }
        List<Property> propertyKeys = graphSchema.getPropertyKeys();
        for (Property property : propertyKeys) {
            if (propertyName.equals(property.getName())) {
                return property;
            }
        }
        return null; //Not found
    }

    public static boolean hasForeignKeys(Table table) {
        if (table == null || table.getConstraints() == null || table.getConstraints().size() == 0) {
            return false;
        }
        for (Constraint constraint : table.getConstraints()) {
            //MySQL sets it as "FOREIGN KEY", so it is safe to use like this, unless they change this.
            if ("FOREIGN KEY".equals(constraint.getType())) {
                return true;
            }
        }
        return false; //Not found
    }

    public static boolean isForeignKey(String name, Table table) {
        if (table == null || table.getConstraints() == null || table.getConstraints().size() == 0) {
            return false;
        }
        for (Constraint constraint : table.getConstraints()) {
            //MySQL sets it as "FOREIGN KEY", so it is safe to use like this, unless they change this.
            if ("FOREIGN KEY".equals(constraint.getType())) {
                if (constraint.getColumn().equals(name)) {
                    return true;
                }
            }
        }
        return false; //Not found
    }

    public static boolean hasColumn(Table table, String columnName) {
        for (Column column : table.getColumns()) {
            if (column.getName().toLowerCase().equals(columnName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getMySQLToIBMDataType(String dataType) {
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("TINYINT", "Integer");
        typeMap.put("SMALLINT", "Integer");
        typeMap.put("MEDIUMINT", "Integer");
        typeMap.put("INT", "Integer");
        typeMap.put("BIGINT", "Integer");
        typeMap.put("TINYINT", "Integer");
        typeMap.put("DECIMAL", "Float");
        typeMap.put("FLOAT", "Float");
        typeMap.put("REAL", "Float");
        typeMap.put("DOUBLE PRECISION", "Float");
        //Doesn't currently support Date, using String for now
        //PS: This can't be used as Data for computations
        typeMap.put("DATE", "String");
        typeMap.put("TIME", "String");
        typeMap.put("DATETIME", "String");
        typeMap.put("TIMESTAMP", "String");
        typeMap.put("YEAR", "String");
        typeMap.put("CHAR", "String");
        typeMap.put("VARCHAR", "String");
        typeMap.put("BINARY", "String");
        typeMap.put("VARBINARY", "String");
        typeMap.put("TINYBLOB", "String");
        typeMap.put("BLOB", "String");
        typeMap.put("MEDIUMBLOB", "String");
        typeMap.put("LONGBLOB", "String");
        typeMap.put("TEXT", "String");
        typeMap.put("TINYTEXT", "String");
        typeMap.put("TEXT", "String");
        typeMap.put("MEDIUMTEXT", "String");
        typeMap.put("LONGTEXT", "String");
        typeMap.put("ENUM", "String");
        //Not sure about how to handle SET
        //I suppose we can safely use it as String in Graph
        typeMap.put("SET", "String");
        typeMap.put("JSON", "String");

        return typeMap.get(dataType);
    }

    public static boolean hasProperty(String propertyName, GraphSchema graphSchema) {
        for (Property property : graphSchema.getPropertyKeys()) {
            if (propertyName.equals(property.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForeignKeyConstraint(Constraint constraint) {
        return "FOREIGN KEY".equals(constraint.getType());
    }

    public static boolean isPrimaryKeyIndex(Index index) {
        return "PRIMARY".equals(index.getIndexName());
    }

    public static boolean hasPriamryKeys(Table table) {
        if (table == null || table.getConstraints() == null || table.getConstraints().size() == 0) {
            return false;
        }
        for (Constraint constraint : table.getConstraints()) {
            //MySQL sets it as "PRIMARY KEY", so it is safe to use like this, unless they change this.
            if ("PRIMARY KEY".equals(constraint.getType())) {
                return true;
            }
        }
        return false; //Not found
    }

    public static List<Constraint> getForeignKeys(Schema schema) {
        List<Constraint> constraints = new ArrayList<>();
        for (Table table : schema.getTables()) {
            constraints.addAll(ServiceUtil.getForeignKeys(table));
        }
        return constraints;
    }
}
