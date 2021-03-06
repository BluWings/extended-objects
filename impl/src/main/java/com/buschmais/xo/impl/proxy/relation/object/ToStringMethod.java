package com.buschmais.xo.impl.proxy.relation.object;

import com.buschmais.xo.impl.SessionContext;
import com.buschmais.xo.impl.proxy.common.object.AbstractToStringMethod;

public class ToStringMethod<Relation> extends AbstractToStringMethod<Relation> {

    private final SessionContext<?, ?, ?, ?, ?, Relation, ?, ?, ?> sessionContext;

    public ToStringMethod(SessionContext<?, ?, ?, ?, ?, Relation, ?, ?, ?> sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Override
    protected String getId(Relation datastoreType) {
        return sessionContext.getDatastoreSession().getDatastoreRelationManager().getRelationId(datastoreType).toString();
    }
}
