package com.buschmais.cdo.store.json.impl.metadata;

import com.buschmais.cdo.spi.datastore.DatastoreRelationMetadata;

public class JsonRelationMetadata implements DatastoreRelationMetadata<String> {

    @Override
    public String getDiscriminator() {
        return null;
    }

}
