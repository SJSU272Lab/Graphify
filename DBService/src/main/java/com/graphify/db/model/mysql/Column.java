package com.graphify.db.model.mysql;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 21-11-2016.
 */
@XmlRootElement
public class Column {
    private String name;
    private String type;
    private Boolean isNullable;

    public Column() {
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

    public Boolean getNullable() {
        return isNullable;
    }

    public void setNullable(Boolean nullable) {
        isNullable = nullable;
    }

    @Override
    public String toString() {
        return "\nColumn{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", isNullable=" + isNullable +
                '}';
    }
}
