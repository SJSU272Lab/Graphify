package com.graphify.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphify.db.client.ibm.graph.IBMGraphClient;
import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.ibm.graph.Property;
import com.graphify.db.model.mysql.Constraint;
import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Table;
import com.graphify.db.resource.DBServiceResource;
import com.graphify.db.rule.engine.ForeignKeyBasedStrategy;
import com.graphify.db.rule.engine.Strategy;
import com.graphify.db.util.CSVUtil;
import com.graphify.db.util.Mapper;
import com.graphify.db.util.ServiceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sushant on 23-11-2016.
 */
public class ConvertTest {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/expense?user=root&password=admin&autoReconnect=true&useSSL=false";
    private static final String DB_SCHEMA = "expense";
    private static final ClassLoader CLASS_LOADER = ConvertTest.class.getClassLoader();

    public static void main(String[] args) throws JsonProcessingException {
        String graphName = "testgraphschema7";
        GraphSchema graphSchema = convert();
        System.out.print(ConvertTest.class.getName() + " Converted schema\n " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(graphSchema));
        createGraphAndSchema(graphSchema, graphName);
        StringBuffer commandString = new StringBuffer();
        addDataUsingMapper(commandString);
        System.out.print(ConvertTest.class.getName() + " Add data Gremlin\n" + commandString);
        addData(graphName, commandString.toString());
    }

    private static void addDataUsingMapper(StringBuffer commandString) {
        DBServiceResource dbService = new DBServiceResource();
        Schema schema = (Schema) dbService.getSchema(DB_URL, DB_SCHEMA).getEntity();
        //System.out.println(test.getClass().getCanonicalName() + " " + schema);
        IBMGraphClient client = new IBMGraphClient();
        GraphSchema graphSchema = client.getGraphSchema("testgraphschema7");
        //System.out.println(ConvertTest.class.getCanonicalName() + " " +graphSchema);
        Mapper mapper = new Mapper(schema);

        for (String tableName : mapper.getMapData().keySet()) {
            Table table = ServiceUtil.getMySQLTableByName(tableName, schema);
            //System.out.println(test.getClass().getCanonicalName() + " Got table " + table.getName());
            Boolean needsEdge = false;
            List<Constraint> foreignConstraints = ServiceUtil.getForeignKeys(table);
            if (foreignConstraints.size() > 0) {
                needsEdge = true;
            }

            if (table != null) {
                CLASS_LOADER.getResource("./mysql/").getPath();
                String[] columnIndexArray = CSVUtil.getHeader(CLASS_LOADER.getResource("./mysql/").getPath(), tableName);

                Map<String, Integer> columnIndex = new HashMap<>();
                for (int i = 0; i < columnIndexArray.length; i++) {
                    columnIndex.put(columnIndexArray[i].toLowerCase(), i);
                }

                List<String[]> tableContent = CSVUtil.getContent(CLASS_LOADER.getResource("./mysql/").getPath(), tableName);
                for (String[] row : tableContent) {
                    String vertexName = addVertex(mapper.getTableVertexMap().get(tableName), row, columnIndex, mapper.getMapData().get(tableName), graphSchema, table, commandString);
                    //System.out.println(ConvertTest.class.getCanonicalName() + " needsEdge " +needsEdge);
                    if (needsEdge) {
                        addEdge(foreignConstraints, vertexName, row, mapper.getEdges(), schema, columnIndex, commandString);
                    }
                }
            }
        }
    }

    public static void createGraphAndSchema(GraphSchema graphSchema, String graphName) {
        IBMGraphClient client = new IBMGraphClient();
        client.createNewGraph(graphName);
        client.createGraphSchema(graphSchema, graphName);
    }

    public static void addData(String graphName, String command) {
        IBMGraphClient client = new IBMGraphClient();
        client.addData(graphName, command);
    }

    public static GraphSchema convert() {
        Strategy strategy = new ForeignKeyBasedStrategy();
        DBServiceResource dbService = new DBServiceResource();
        Schema schema = (Schema) dbService.getSchema(DB_URL, DB_SCHEMA).getEntity();
        GraphSchema graphSchema = strategy.convert(schema);
        //System.out.println(graphSchema);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(graphSchema);
            //System.out.println(ConvertTest.class.getCanonicalName() + "\n" +jsonInString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return graphSchema;
    }

    /*public static void addData() {
        DBServiceResource dbService = new DBServiceResource();
        Schema schema = (Schema) dbService.getSchema().getEntity();
        //System.out.println(test.getClass().getCanonicalName() + " " + schema);
        IBMGraphClient client = new IBMGraphClient();
        GraphSchema graphSchema = client.getGraphSchema("graphify");

        Map<String, String> tableVertexMap = new HashMap<>();
        tableVertexMap.put("status", "status");
        tableVertexMap.put("expense_category", "expense_category");
        tableVertexMap.put("entity", "entity");
        tableVertexMap.put("expense", "expense");


        //This is if you wish to transform the Table to Edge
        //Not yet required for implemented strategy based on foreign key
        //Map<String, String> tableEdgeMap = new HashMap<>();


        //Ordering matters when dealing with tables with foreign keys
        //as we need to have the vertexes created before you create edges
        Map<String, Map<String, String>> mapData = new LinkedHashMap<>();
        Map<String, String> statusCoulmnMap = new HashMap<>();
        statusCoulmnMap.put("STATUS_ID", "id");
        statusCoulmnMap.put("STATUS_DESCR", "descr");

        Map<String, String> expCatColMap = new HashMap<>();
        expCatColMap.put("EXPENSE_CATEGORY_ID", "id");
        expCatColMap.put("EXP_CAT_DESCR", "descr");

        Map<String, String> entityColumnMap = new HashMap<>();
        entityColumnMap.put("ENTITY_ID", "id");
        entityColumnMap.put("NAME", "name");
        entityColumnMap.put("EMAIL", "email");

        Map<String, String> expenseColumnMap = new HashMap<>();
        expenseColumnMap.put("EXPENSE_ID", "id");
        expenseColumnMap.put("ITEM_DESCR", "descr");
        //expenseColumnMap.put("ITEM_LINK", "item_link");
        expenseColumnMap.put("ESTIMATED_COST", "estimated_cost");
        //expenseColumnMap.put("SUBMIT_DATE", "submit_date");

        mapData.put("status", statusCoulmnMap);
        mapData.put("expense_category", expCatColMap);
        mapData.put("entity", entityColumnMap);
        mapData.put("expense", expenseColumnMap);

        Map<String, String> edgeNames = new HashMap<>();
        edgeNames.put("expense_ibfk_1", "initiated");
        edgeNames.put("expense_ibfk_2", "of_category");
        edgeNames.put("expense_ibfk_3", "has_status");

        for (String tableName : mapData.keySet()) {
            Table table = ServiceUtil.getMySQLTableByName(tableName, schema);
            //System.out.println(test.getClass().getCanonicalName() + " Got table " + table.getName());
            Boolean needsEdge = false;
            List<Constraint> foreignConstraints = ServiceUtil.getForeignKeys(table);

            if (foreignConstraints.size() > 0) {
                needsEdge = true;
            }

            if (table != null) {
                String[] columnIndexArray = CSVUtil.getHeader(tableName);

                Map<String, Integer> columnIndex = new HashMap<>();
                for (int i = 0; i < columnIndexArray.length; i++) {
                    columnIndex.put(columnIndexArray[i], i);
                }

                List<String[]> tableContent = CSVUtil.getContent(tableName);
                for (String[] row : tableContent) {
                    String vertexName = addVertex(tableVertexMap.get(tableName), row, columnIndex, mapData.get(tableName), graphSchema, table);
                    //System.out.println(ConvertTest.class.getCanonicalName() + " needsEdge " +needsEdge);
                    if (needsEdge) {
                        addEdge(foreignConstraints, vertexName, row, edgeNames, schema, columnIndex);
                    }
                }


            }
        }
    }*/

    private static void addEdge(List<Constraint> foreignConstraints, String vertexName, String[] row, Map<String, String> edgeNames, Schema schema, Map<String, Integer> columnIndex, StringBuffer commandString) {
        for (Constraint constraint : foreignConstraints) {
            //System.out.println(ConvertTest.class.getCanonicalName() + " For vertex " + vertexName);
            String edgeName = edgeNames.get(constraint.getName());
            //System.out.println(ConvertTest.class.getCanonicalName() + " For edge name " + edgeName);
            StringBuffer command = new StringBuffer();
            if (edgeName != null && !edgeName.isEmpty()) {
                command.append(vertexName).append(".addEdge('").append(edgeName).append("',");
                if (isForeignPrimaryOfSource(constraint, schema)) {
                    //System.out.println(ConvertTest.class.getCanonicalName() +   " Row data " + row[columnIndex.get(constraint.getColumn())]);
                    command.append(constraint.getRefTable().toLowerCase()).append("_").append(row[columnIndex.get(constraint.getColumn().toLowerCase())]).append(");");
                }
            }
            //System.out.println(command);
            commandString.append(command).append("\n");
        }
    }

    private static boolean isForeignPrimaryOfSource(Constraint constraint, Schema schema) {
        boolean result = false;
        String refTable = constraint.getRefTable();
        String refColumn = constraint.getRefColumn();
        //TODO: Check if above two are valid
        Table table = ServiceUtil.getMySQLTableByName(refTable, schema);
        if (table != null) {
            List<Constraint> primaries = ServiceUtil.getPrimaries(table);
            for (Constraint primary : primaries) {
                //Just to be sure applying toLower
                if (refColumn.toLowerCase().equals(primary.getColumn().toLowerCase())) {
                    //System.out.println(ConvertTest.class.getCanonicalName() + " Got primary ");
                    return true;
                }
            }
        }
        return result;
    }

    private static String addVertex(String lableName, String[] row, Map<String, Integer> columnIndex, Map<String, String> columnMap, GraphSchema graphSchema, Table table, StringBuffer commandString) {
        StringBuffer result = new StringBuffer();
        String varPattern = getVarPattern(table);
        StringBuffer varName = new StringBuffer();

        result.append("graph.addVertex(T.label, " + "'" + lableName + "'");
        for (String column : columnMap.keySet()) {
            String propertyName = columnMap.get(column);
            Property property = ServiceUtil.getPropertyByName(propertyName, graphSchema);
            String propertyDataType = property.getDataType();
            //System.out.println( ConvertTest.class.getCanonicalName() + " Got index of column " + column + " as "+ columnIndex.get(column));
            String value = row[columnIndex.get(column)];
            //TODO: Date also needs special attention, ticket raised with IBM about it

            if ("NULL".equals(value))   //This is how MySQL represents null when data is exported
            {
                continue;               //Property value cannot be null in IBM Graph
            } else if ("String".equals(propertyDataType)) {
                result.append(", '" + propertyName + "', " + "'" + value + "'");
            } else {
                result.append(", '" + propertyName + "', " + value);
            }
            if (varPattern.toLowerCase().contains("_" + column.toLowerCase())) {
                varName.append(varPattern.replace(column.toLowerCase(), value));
            }
        }
        result.append(");");
        String command = "def " + varName + " = " + result;
        //System.out.println(command);
        commandString.append(command).append("\n");
        return varName.toString();
    }

    public static String getVarPattern(Table table) {
        List<Constraint> constraints = ServiceUtil.getPrimaries(table);
        String primaries = "";
        if (constraints != null && constraints.size() > 0) {
            for (Constraint constraint : constraints) {
                primaries += "_" + constraint.getColumn().toLowerCase();
            }
        }
        String varPattern = table.getName() + primaries;
        return varPattern;
    }
}