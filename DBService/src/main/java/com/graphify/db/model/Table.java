package com.graphify.db.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Sushant on 21-11-2016.
 */
@XmlRootElement
public class Table {

    private String name;
    private List<Column> columns;
    private List<Constraint> constraints;
    private List<Index> indexes;

    public Table() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    @Override
    public String toString() {
        return "\nTable{" +
                "name='" + name + '\'' +
                ", columns=" + columns +
                ", constraints=" + constraints +
                ", indexes=" + indexes +
                '}';
    }
}
