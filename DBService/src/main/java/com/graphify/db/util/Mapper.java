package com.graphify.db.util;

import com.graphify.db.model.ibm.graph.Edge;
import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.ibm.graph.Property;
import com.graphify.db.model.ibm.graph.Vertex;
import com.graphify.db.model.mysql.Column;
import com.graphify.db.model.mysql.Schema;
import com.graphify.db.model.mysql.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sushant on 23-11-2016.
 */
public class Mapper {

    private Schema schema;
    private GraphSchema graphSchema;

    public Mapper(Schema schema, GraphSchema graphSchema) {
        this.schema = schema;
        this.graphSchema = graphSchema;
    }




}
