package com.buschmais.cdo.impl.test.bootstrap.provider.metadata;

import com.buschmais.cdo.spi.datastore.DatastoreRelationMetadata;

public class TestRelationMetadata implements DatastoreRelationMetadata<String> {

    @Override
    public String getDiscriminator() {
        return null;
    }

}
