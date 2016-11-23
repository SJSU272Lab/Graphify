package com.graphify.db.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 21-11-2016.
 */
@XmlRootElement
public class Constraint {
    private String name;
    private String type;
    private String column;
    private String refTable;
    private String refColumn;

    public Constraint() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getRefTable() {
        return refTable;
    }

    public void setRefTable(String refTable) {
        this.refTable = refTable;
    }

    public String getRefColumn() {
        return refColumn;
    }

    public void setRefColumn(String refColumn) {
        this.refColumn = refColumn;
    }

    @Override
    public String toString() {
        return "\nConstraint{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", column='" + column + '\'' +
                ", refTable='" + refTable + '\'' +
                ", refColumn='" + refColumn + '\'' +
                '}';
    }
}
