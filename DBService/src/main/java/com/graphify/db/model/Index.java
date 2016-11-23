package com.graphify.db.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Sushant on 21-11-2016.
 */
@XmlRootElement
public class Index {
    private String indexName;
    private String column;
    private Integer cardinality;
    private Boolean isUnique;

    public Index() {
    }

    public Boolean getUnique() {
        return isUnique;
    }

    public void setUnique(Boolean unique) {
        isUnique = unique;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Integer getCardinality() {
        return cardinality;
    }

    public void setCardinality(Integer cardinality) {
        this.cardinality = cardinality;
    }

    @Override
    public String toString() {
        return "\nIndex{" +
                "indexName='" + indexName + '\'' +
                ", column='" + column + '\'' +
                ", cardinality=" + cardinality +
                ", isUnique=" + isUnique +
                '}';
    }
}
