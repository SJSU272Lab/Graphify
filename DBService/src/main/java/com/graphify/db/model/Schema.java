package com.graphify.db.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Sushant on 21-11-2016.
 */
@XmlRootElement
public class Schema {
    private String name;
    private List<Table> tables;

    public Schema() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "name='" + name + '\'' +
                ", tables=" + tables +
                '}';
    }
}
