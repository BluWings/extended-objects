package com.buschmais.xo.neo4j.test.relation.typed.composite;

import static com.buschmais.xo.neo4j.api.annotation.Relation.Incoming;
import static com.buschmais.xo.neo4j.api.annotation.Relation.Outgoing;

public interface BaseType {

    @Outgoing
    C getC();

    @Incoming
    D getD();

    int getVersion();

    void setVersion(int version);

}
