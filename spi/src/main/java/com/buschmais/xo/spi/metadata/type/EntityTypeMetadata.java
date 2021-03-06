package com.buschmais.xo.spi.metadata.type;

import com.buschmais.xo.spi.datastore.DatastoreEntityMetadata;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.method.MethodMetadata;
import com.buschmais.xo.spi.reflection.AnnotatedType;

import java.util.Collection;

/**
 * Represents metadata for entity types.
 *
 * @param <DatastoreMetadata> The datastore specific metadata type.
 */
public class EntityTypeMetadata<DatastoreMetadata extends DatastoreEntityMetadata<?>> extends AbstractDatastoreTypeMetadata<DatastoreMetadata> {

    private boolean abstractType;

    private boolean finalType;

    /**
     * Constructor.
     *
     * @param annotatedType     The annotated type this metadata is created for.
     * @param superTypes        The already registered super types.
     * @param properties        The properties of this type.
     * @param abstractType      <code>true</code> indicates that this type is abstract.
     * @param finalType         <code>true</code> indicates that this type is final.
     * @param indexedProperty   The indexed property.
     * @param datastoreMetadata The datastore specific metadata.
     */
    public EntityTypeMetadata(AnnotatedType annotatedType, Collection<TypeMetadata> superTypes, Collection<MethodMetadata<?, ?>> properties,
                              boolean abstractType, boolean finalType, IndexedPropertyMethodMetadata indexedProperty, DatastoreMetadata datastoreMetadata) {
        super(annotatedType, superTypes, properties, indexedProperty, datastoreMetadata);
        this.abstractType = abstractType;
        this.finalType = finalType;
    }

    public boolean isAbstract() {
        return abstractType;
    }

    public boolean isFinal() {
        return finalType;
    }
}
