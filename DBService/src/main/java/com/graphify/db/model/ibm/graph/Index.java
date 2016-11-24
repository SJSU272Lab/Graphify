package com.graphify.db.model.ibm.graph;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Sushant on 22-11-2016.
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class Index {
    private String name;
    private List<String> propertyKeys;
    private Boolean composite;
    private Boolean unique;
    private Boolean requiresReindex;
    private String type;

    public Index() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPropertyKeys() {
        return propertyKeys;
    }

    public void setPropertyKeys(List<String> propertyKeys) {
        this.propertyKeys = propertyKeys;
    }

    public Boolean getComposite() {
        return composite;
    }

    public void setComposite(Boolean composite) {
        this.composite = composite;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean getRequiresReindex() {
        return requiresReindex;
    }

    public void setRequiresReindex(Boolean requiresReindex) {
        this.requiresReindex = requiresReindex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Index{" +
                "name='" + name + '\'' +
                ", propertyKeys=" + propertyKeys +
                ", composite=" + composite +
                ", unique=" + unique +
                ", requiresReindex=" + requiresReindex +
                ", type='" + type + '\'' +
                '}';
    }
}
