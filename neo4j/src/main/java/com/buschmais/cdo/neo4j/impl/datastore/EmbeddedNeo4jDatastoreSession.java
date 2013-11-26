package com.buschmais.cdo.neo4j.impl.datastore;

import com.buschmais.cdo.neo4j.impl.node.metadata.NodeMetadataProvider;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.util.Iterator;
import java.util.Map;

public class EmbeddedNeo4jDatastoreSession extends AbstractNeo4jDatastoreSession<GraphDatabaseService> {

    private Transaction transaction;

    public EmbeddedNeo4jDatastoreSession(GraphDatabaseService graphDatabaseService, NodeMetadataProvider metadataProvider) {
        super(graphDatabaseService, metadataProvider);
    }

    public Iterator<Map<String, Object>> execute(String query, Map<String, Object> parameters) {
        ExecutionEngine executionEngine = new ExecutionEngine(getGraphDatabaseService());
        ExecutionResult executionResult = executionEngine.execute(query, parameters);
        return executionResult.iterator();
    }

    @Override
    public void begin() {
        transaction = getGraphDatabaseService().beginTx();
    }

    @Override
    public void commit() {
        transaction.success();
        transaction.close();
    }

    @Override
    public void rollback() {
        transaction.failure();
        transaction.close();
    }
}