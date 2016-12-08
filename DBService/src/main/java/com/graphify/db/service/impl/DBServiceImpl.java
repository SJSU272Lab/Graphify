package com.graphify.db.service.impl;

import com.graphify.db.client.ibm.graph.IBMGraphClient;
import com.graphify.db.dao.DBDao;
import com.graphify.db.dao.impl.MySQLDaoImpl;
import com.graphify.db.dao.util.DaoUtil;
import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.ibm.graph.Property;
import com.graphify.db.model.mysql.Constraint;
import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Table;
import com.graphify.db.model.mysql.Validate;
import com.graphify.db.resource.DBServiceResource;
import com.graphify.db.rule.engine.ForeignKeyBasedStrategy;
import com.graphify.db.rule.engine.Strategy;
import com.graphify.db.service.DBService;
import com.graphify.db.util.CSVUtil;
import com.graphify.db.util.Mapper;
import com.graphify.db.util.ServiceUtil;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sushant on 22-11-2016.
 */
public class DBServiceImpl implements DBService {

    private static final DBDao dbDao = new MySQLDaoImpl();
    private static final Logger LOGGER = Logger.getLogger(DBServiceImpl.class);
    @Override
    public Schema getDBSchema(String url, String schemaName) {
        Schema schema = dbDao.getSchema(DaoUtil.getConnection(url), schemaName);
        //LOGGER.info(schema);
        return schema;
    }

    @Override
    public Validate validateSchema(String url, String schemaName, String fileLocation) {
        Schema schema = dbDao.getSchema(DaoUtil.getConnection(url), schemaName);
        Validate validate = new Validate();

        for (Table table : schema.getTables()) {
            String[] header = CSVUtil.getHeaderFromSource(fileLocation, table.getName());
            if (header == null) {
                //LOGGER.info(DBService.class.getCanonicalName() + " Got null ");
                validate.setMessage("Could not find valid CSV for table '" + table.getName() + "'");
                validate.setValid(false);
                return validate;
            }
            if (header.length == table.getColumns().size()) {
                for (int i = 0; i < header.length; i++) {
                    if (!ServiceUtil.hasColumn(table, header[i])) {
                        validate.setMessage("CSV File for table " + table.getName() + " has unexpected column '" + header[i] + "'");
                        validate.setValid(false);
                        return validate;
                    }
                }
            } else {
                validate.setMessage("CSV File for table '" + table.getName() + "' has missing/invalid column(s) than expected");
                validate.setValid(false);
                return validate;
            }
        }
        validate.setMessage("Done");
        validate.setValid(true);

        return validate;


    }

    @Override
    public GraphSchema convertDB(String url, String schemaName) {
        Strategy strategy = new ForeignKeyBasedStrategy();
        DBServiceResource dbService = new DBServiceResource();
        Schema schema = (Schema) dbService.getSchema(url, schemaName).getEntity();
        GraphSchema graphSchema = strategy.convert(schema);
        return graphSchema;
    }

    @Override
    public GraphSchema convertAndAdd(String url, String schemaName, String fileLocation) {
        GraphSchema graphSchema = convertDB(url, schemaName);
        String graphName = Long.toString(new Date().getTime());
        createGraphAndSchema(graphSchema, graphName);
        String command = addDataUsingMapper(url, schemaName, fileLocation, graphName);
        addData(graphName, command);
        graphSchema.setGraphName(graphName);
        return graphSchema;
    }

    private void addData(String graphName, String command) {
        IBMGraphClient client = new IBMGraphClient();
        client.addData(graphName, command);
    }

    private void createGraphAndSchema(GraphSchema graphSchema, String graphName) {
        IBMGraphClient client = new IBMGraphClient();
        client.createNewGraph(graphName);
        client.createGraphSchema(graphSchema, graphName);
    }


    private String addDataUsingMapper(String url, String schemaName, String fileLocation, String graphName) {
        StringBuffer commandString = new StringBuffer();
        Schema schema = getDBSchema(url, schemaName);
        IBMGraphClient client = new IBMGraphClient();
        GraphSchema graphSchema = client.getGraphSchema(graphName);
        Mapper mapper = new Mapper(schema);

        for (String tableName : mapper.getMapData().keySet()) {
            Table table = ServiceUtil.getMySQLTableByName(tableName, schema);
            //LOGGER.info(test.getClass().getCanonicalName() + " Got table " + table.getName());
            Boolean needsEdge = false;
            List<Constraint> foreignConstraints = ServiceUtil.getForeignKeys(table);

            if (foreignConstraints.size() > 0) {
                needsEdge = true;
            }

            if (table != null) {
                String[] columnIndexArray = CSVUtil.getHeader(fileLocation, tableName);

                Map<String, Integer> columnIndex = new HashMap<>();
                for (int i = 0; i < columnIndexArray.length; i++) {
                    columnIndex.put(columnIndexArray[i].toLowerCase(), i);
                }

                List<String[]> tableContent = CSVUtil.getContent(fileLocation, tableName);
                for (String[] row : tableContent) {
                    String vertexName = addVertex(mapper.getTableVertexMap().get(tableName), row, columnIndex, mapper.getMapData().get(tableName), graphSchema, table, commandString);
                    //LOGGER.info(ConvertTest.class.getCanonicalName() + " needsEdge " +needsEdge);
                    if (needsEdge) {
                        addEdge(foreignConstraints, vertexName, row, mapper.getEdges(), schema, columnIndex, commandString);
                    }
                }
            }
        }
        return commandString.toString();
    }

    private void addEdge(List<Constraint> foreignConstraints, String vertexName, String[] row, Map<String, String> edgeNames, Schema schema, Map<String, Integer> columnIndex, StringBuffer commandString) {
        for (Constraint constraint : foreignConstraints) {
            //LOGGER.info(ConvertTest.class.getCanonicalName() + " For vertex " + vertexName);
            String edgeName = edgeNames.get(constraint.getName());
            //LOGGER.info(ConvertTest.class.getCanonicalName() + " For edge name " + edgeName);
            StringBuffer command = new StringBuffer();
            if (edgeName != null && !edgeName.isEmpty()) {
                command.append(vertexName).append(".addEdge('").append(edgeName).append("',");
                if (isForeignPrimaryOfSource(constraint, schema)) {
                    //LOGGER.info(ConvertTest.class.getCanonicalName() +   " Row data " + row[columnIndex.get(constraint.getColumn())]);
                    command.append(constraint.getRefTable().toLowerCase()).append("_").append(row[columnIndex.get(constraint.getColumn().toLowerCase())]).append(");");
                }
            }
            //LOGGER.info(command);
            commandString.append(command).append("\n");
        }
    }

    private boolean isForeignPrimaryOfSource(Constraint constraint, Schema schema) {
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
                    //LOGGER.info(ConvertTest.class.getCanonicalName() + " Got primary ");
                    return true;
                }
            }
        }
        return result;
    }

    private String addVertex(String lableName, String[] row, Map<String, Integer> columnIndex, Map<String, String> columnMap, GraphSchema graphSchema, Table table, StringBuffer commandString) {
        StringBuffer result = new StringBuffer();
        String varPattern = getVarPattern(table);
        StringBuffer varName = new StringBuffer();

        result.append("graph.addVertex(T.label, " + "'" + lableName + "'");
        for (String column : columnMap.keySet()) {
            String propertyName = columnMap.get(column);
            Property property = ServiceUtil.getPropertyByName(propertyName, graphSchema);
            String propertyDataType = property.getDataType();
            //LOGGER.info( ConvertTest.class.getCanonicalName() + " Got index of column " + column + " as "+ columnIndex.get(column));
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
        //LOGGER.info(command);
        commandString.append(command).append("\n");
        return varName.toString();
    }

    public String getVarPattern(Table table) {
        List<Constraint> constraints = ServiceUtil.getPrimaries(table);
        String primaries = "";
        if (constraints != null && constraints.size() > 0) {
            for (Constraint constraint : constraints) {
                primaries += "_" + constraint.getColumn().toLowerCase();
            }
        }
        String varPattern = table.getName().toLowerCase() + primaries;
        return varPattern;
    }


    public static void main(String[] args) {
        DBServiceImpl dbService = new DBServiceImpl();
        String url = "jdbc:mysql://localhost:3306/expense?user=root&password=admin";
        Validate validate = dbService.validateSchema(url, "expense", "D:/DevEnv/Fall16-Team12/DBService/src/main/resources/mysql");
        LOGGER.info(DBServiceImpl.class.getCanonicalName() + validate);

    }
}