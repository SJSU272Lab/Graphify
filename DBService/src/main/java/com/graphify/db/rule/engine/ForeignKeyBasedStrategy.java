package com.graphify.db.rule.engine;

import com.graphify.db.model.ibm.graph.*;
import com.graphify.db.model.ibm.graph.Index;
import com.graphify.db.model.mysql.*;
import com.graphify.db.util.ServiceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sushant on 24-11-2016.
 */
public class ForeignKeyBasedStrategy implements Strategy {

    @Override
    public GraphSchema convert(Schema schema) {
        List<Table> tables = schema.getTables();
        GraphSchema graphSchema = new GraphSchema();
        graphSchema.init();
        for (Table table : tables) {
            Vertex vertex = new Vertex();
            vertex.setName(table.getName().toLowerCase());
            graphSchema.getVertexLabels().add(vertex);

            for (Column column : table.getColumns()) {
                if (!ServiceUtil.isForeignKey(column.getName(), table)) {
                    String dataType = ServiceUtil.getMySQLToIBMDataType(column.getType().toUpperCase());
                    if (dataType != null) {
                        Property property = new Property();
                        String propertyName = column.getName().toLowerCase();
                        if (!ServiceUtil.hasProperty(propertyName, graphSchema)) {
                            property.setName(propertyName);
                            property.setDataType(dataType);
                            //Using 'SINGLE' as default, may need to change for SET
                            property.setCardinality("SINGLE");
                            graphSchema.getPropertyKeys().add(property);
                        } else {
                            //TODO: Already has the property with this name
                        }

                    } else {
                        //TODO: Not supported data Type
                    }
                }
            }

            for (Constraint constraint : table.getConstraints()) {
                if (ServiceUtil.isForeignKeyConstraint(constraint)) {
                    Edge edge = new Edge();
                    String edgeName = (constraint.getRefTable() + "_" + table.getName()).toLowerCase();
                    edge.setName(edgeName);
                    edge.setMultiplicity("MULTI");
                    graphSchema.getEdgeLabels().add(edge);
                }
            }

            if (ServiceUtil.hasPriamryKeys(table)) {
                Index graphIndex = new Index();
                graphIndex.setName("vByPrimary_" + table.getName().toLowerCase());
                List<String> propertyKeys = ServiceUtil.getPrimaryColumns(table);
                graphIndex.setPropertyKeys(propertyKeys);
                graphIndex.setComposite(true);
                graphIndex.setUnique(true); //As this is primary key
                graphSchema.getVertexIndexes().add(graphIndex);
            } else {
                for (com.graphify.db.model.mysql.Index index : table.getIndexes()) {
                    if (!index.getIndexName().equals("PRIMARY")) {
                        Index graphIndex = new Index();
                        graphIndex.setName("vBy" + index.getIndexName().toLowerCase());
                        List<String> propertyKeys = new ArrayList<>();
                        propertyKeys.add(index.getColumn());
                        graphIndex.setPropertyKeys(propertyKeys);
                        graphIndex.setComposite(true);
                        graphIndex.setUnique(false); //As this is primary key
                        //Index only for this vertex as in case of foreign key
                        //it may create duplicate indexes one for this vertex
                        //and other for reference table of the foreign key
                        graphIndex.setIndexOnly(vertex.getName());
                        graphSchema.getVertexIndexes().add(graphIndex);
                    }
                }
            }

        }
        return graphSchema;
    }
}
