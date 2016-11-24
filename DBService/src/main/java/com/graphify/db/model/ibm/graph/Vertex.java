package com.graphify.db.model.ibm.graph;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 22-11-2016.
 */
@XmlRootElement
public class Vertex {
    private String name;

    public Vertex() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "name='" + name + '\'' +
                '}';
    }
}
