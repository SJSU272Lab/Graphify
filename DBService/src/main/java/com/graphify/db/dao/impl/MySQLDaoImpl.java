package com.graphify.db.dao.impl;

import com.graphify.db.dao.DBDao;
import com.graphify.db.dao.util.DaoUtil;
import com.graphify.db.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sushant on 21-11-2016.
 */
public class MySQLDaoImpl implements DBDao {

    private static final String GET_TABLES = "SELECT \n" +
            "    T.TABLE_NAME\n" +
            "FROM\n" +
            "    INFORMATION_SCHEMA.TABLES T\n" +
            "WHERE\n" +
            "    T.TABLE_SCHEMA = ?";
    private static final String GET_COLUMNS = "SELECT \n" +
            "    C.COLUMN_NAME, C.DATA_TYPE, C.IS_NULLABLE\n" +
            "FROM\n" +
            "    INFORMATION_SCHEMA.COLUMNS C\n" +
            "WHERE\n" +
            "    C.TABLE_SCHEMA = ?\n" +
            "        AND C.TABLE_NAME = ?";

    private static final String GET_CONSTRAINTS = "SELECT \n" +
            "    TC.CONSTRAINT_NAME,\n" +
            "    TC.CONSTRAINT_TYPE,\n" +
            "    KCU.COLUMN_NAME,\n" +
            "    KCU.REFERENCED_TABLE_NAME,\n" +
            "    KCU.REFERENCED_COLUMN_NAME\n" +
            "FROM\n" +
            "    INFORMATION_SCHEMA.TABLE_CONSTRAINTS TC,\n" +
            "    INFORMATION_SCHEMA.KEY_COLUMN_USAGE KCU\n" +
            "WHERE\n" +
            "    TC.TABLE_NAME = KCU.TABLE_NAME\n" +
            "        AND TC.CONSTRAINT_SCHEMA = KCU.CONSTRAINT_SCHEMA\n" +
            "        AND TC.CONSTRAINT_NAME = KCU.CONSTRAINT_NAME\n" +
            "        AND TC.TABLE_NAME = ?\n" +
            "        AND TC.CONSTRAINT_SCHEMA = ?";

    private static final String GET_INDEXES = "SELECT \n" +
            "    S.COLUMN_NAME, S.CARDINALITY, S.INDEX_NAME, S.NON_UNIQUE\n" +
            "FROM\n" +
            "    INFORMATION_SCHEMA.STATISTICS S\n" +
            "WHERE\n" +
            "    S.TABLE_SCHEMA = ?\n" +
            "        AND S.TABLE_NAME = ?";


    @Override
    public Schema getSchema(Connection connection, String schemaName) {
        //Using PreparedStatement as it will prevent from SQL injection attack
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Schema schema = new Schema();
        schema.setName(schemaName);
        try {
            statement = connection.prepareStatement(GET_TABLES);
            statement.setString(1, schemaName);
            if (statement.execute()) {
                resultSet = statement.getResultSet();
                List<Table> tables = new ArrayList<>();
                while (resultSet.next()) {
                    Table table = new Table();
                    table.setName(resultSet.getString("TABLE_NAME"));
                    tables.add(table);
                }
                schema.setTables(tables);
                statement.close();
                for (Table table : tables) {
                    statement = connection.prepareStatement(GET_COLUMNS);
                    statement.setString(1, schemaName);
                    statement.setString(2, table.getName());
                    if (statement.execute()) {
                        resultSet = statement.getResultSet();
                        List<Column> columns = new ArrayList<>();
                        while (resultSet.next()) {
                            Column column = new Column();
                            column.setName(resultSet.getString("COLUMN_NAME"));
                            column.setType(resultSet.getString("DATA_TYPE"));
                            column.setNullable(resultSet.getString("IS_NULLABLE").equals("YES") ? true : false);
                            columns.add(column);
                        }
                        table.setColumns(columns);
                    }
                    statement.close();
                    statement = connection.prepareStatement(GET_CONSTRAINTS);
                    statement.setString(1, table.getName());
                    statement.setString(2, schemaName);
                    if (statement.execute()) {
                        resultSet = statement.getResultSet();
                        List<Constraint> constraints = new ArrayList<>();
                        while (resultSet.next()) {
                            Constraint constraint = new Constraint();
                            constraint.setName(resultSet.getString("CONSTRAINT_NAME"));
                            constraint.setType(resultSet.getString("CONSTRAINT_TYPE"));
                            constraint.setColumn(resultSet.getString("COLUMN_NAME"));
                            constraint.setRefTable(resultSet.getString("REFERENCED_TABLE_NAME"));
                            constraint.setRefColumn(resultSet.getString("REFERENCED_COLUMN_NAME"));
                            constraints.add(constraint);
                        }
                        table.setConstraints(constraints);
                    }
                    statement.close();
                    statement = connection.prepareStatement(GET_INDEXES);
                    statement.setString(1, schemaName);
                    statement.setString(2, table.getName());
                    if (statement.execute()) {
                        resultSet = statement.getResultSet();
                        List<Index> indices = new ArrayList<>();
                        while (resultSet.next()) {
                            Index index= new Index();
                            index.setColumn(resultSet.getString("COLUMN_NAME"));
                            index.setCardinality(resultSet.getInt("CARDINALITY"));
                            index.setIndexName(resultSet.getString("INDEX_NAME"));
                            index.setUnique(resultSet.getInt("NON_UNIQUE") == 0 ? true : false);
                            indices.add(index);
                        }
                        table.setIndexes(indices);
                    }
                    statement.close();
                }
            }
            return schema;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException sqlEx) {
                } // ignore

            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqlEx) {
                } // ignore

            }
        }
        return null;
    }

    public static void main(String[] args) {
        Connection connection = DaoUtil.getConnection("jdbc:mysql://localhost/expense?" +
                "user=root&password=admin");
        MySQLDaoImpl impl = new MySQLDaoImpl();
        Schema schema = impl.getSchema(connection, "expense");
        System.out.println(schema);

    }
}
