package com.graphify.db.model.ibm.graph;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 22-11-2016.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class Edge {
    private String name;
    private String multiplicity;
    private Boolean directed;

    public Edge() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    public Boolean getDirected() {
        return directed;
    }

    public void setDirected(Boolean directed) {
        this.directed = directed;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "name='" + name + '\'' +
                ", multiplicity='" + multiplicity + '\'' +
                ", directed=" + directed +
                '}';
    }
}
