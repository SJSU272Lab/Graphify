package com.graphify.db.rule.engine;

import com.graphify.db.model.ibm.graph.GraphSchema;
import com.graphify.db.model.mysql.Schema;

/**
 * Created by Sushant on 23-11-2016.
 */
public interface Strategy {

    GraphSchema convert(Schema schema);

}
