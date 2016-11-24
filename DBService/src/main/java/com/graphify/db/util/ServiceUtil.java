package com.graphify.db.util;

import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.ibm.graph.Property;
import com.graphify.db.model.mysql.Constraint;
import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Table;

import java.util.ArrayList;
import java.util.List;


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

}
