package com.graphify.db.model.ibm.graph;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Sushant on 22-11-2016.
 */
@XmlRootElement
public class GraphSchema {
    private List<Property> propertyKeys;
    private List<Vertex> vertexLabels;
    private List<Edge> edgeLabels;
    private List<Index> vertexIndexes;
    private List<Index> edgeIndexes;

    public GraphSchema() {
    }

    public List<Property> getPropertyKeys() {
        return propertyKeys;
    }

    public void setPropertyKeys(List<Property> propertyKeys) {
        this.propertyKeys = propertyKeys;
    }

    public List<Vertex> getVertexLabels() {
        return vertexLabels;
    }

    public void setVertexLabels(List<Vertex> vertexLabels) {
        this.vertexLabels = vertexLabels;
    }

    public List<Edge> getEdgeLabels() {
        return edgeLabels;
    }

    public void setEdgeLabels(List<Edge> edgeLabels) {
        this.edgeLabels = edgeLabels;
    }

    public List<Index> getVertexIndexes() {
        return vertexIndexes;
    }

    public void setVertexIndexes(List<Index> vertexIndexes) {
        this.vertexIndexes = vertexIndexes;
    }

    public List<Index> getEdgeIndexes() {
        return edgeIndexes;
    }

    public void setEdgeIndexes(List<Index> edgeIndexes) {
        this.edgeIndexes = edgeIndexes;
    }

    @Override
    public String toString() {
        return "GraphSchema{" +
                "propertyKeys=" + propertyKeys +
                ", vertexLabels=" + vertexLabels +
                ", edgeLabels=" + edgeLabels +
                ", vertexIndexes=" + vertexIndexes +
                ", edgeIndexes=" + edgeIndexes +
                '}';
    }
}
