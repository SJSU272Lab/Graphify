package com.graphify.db.model.ibm.graph;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 22-11-2016.
 */
@XmlRootElement
public class Property {
    private String name;
    private String dataType;
    private String cardinality;

    public Property() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    @Override
    public String toString() {
        return "Property{" +
                "name='" + name + '\'' +
                ", dataType='" + dataType + '\'' +
                ", cardinality='" + cardinality + '\'' +
                '}';
    }
}
