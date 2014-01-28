package com.buschmais.cdo.impl;

import com.buschmais.cdo.api.*;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.buschmais.cdo.impl.metadata.MetadataProviderImpl;
import com.buschmais.cdo.impl.reflection.ClassHelper;
import com.buschmais.cdo.spi.bootstrap.CdoDatastoreProvider;
import com.buschmais.cdo.spi.datastore.Datastore;
import com.buschmais.cdo.spi.datastore.DatastoreEntityMetadata;
import com.buschmais.cdo.spi.datastore.DatastoreRelationMetadata;
import com.buschmais.cdo.spi.datastore.DatastoreSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;

public class CdoManagerFactoryImpl<EntityId, Entity, EntityMetadata extends DatastoreEntityMetadata<EntityDiscriminator>, EntityDiscriminator, RelationId, Relation, RelationMetadata extends DatastoreRelationMetadata<RelationDiscriminator>, RelationDiscriminator> implements CdoManagerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdoManagerFactoryImpl.class);

    private CdoUnit cdoUnit;
    private MetadataProvider metadataProvider;
    private ClassLoader classLoader;
    private Datastore<?, EntityMetadata, EntityDiscriminator, RelationMetadata, RelationDiscriminator> datastore;
    private ValidatorFactory validatorFactory;
    private ConcurrencyMode concurrencyMode;
    private Transaction.TransactionAttribute defaultTransactionAttribute;

    public CdoManagerFactoryImpl(CdoUnit cdoUnit) {
        this.cdoUnit = cdoUnit;
        Class<?> providerType = cdoUnit.getProvider();
        if (providerType == null) {
            throw new CdoException("No provider specified for CDO unit '" + cdoUnit.getName() + "'.");
        }
        if (!CdoDatastoreProvider.class.isAssignableFrom(providerType)) {
            throw new CdoException(providerType.getName() + " specified as CDO provider must implement " + CdoDatastoreProvider.class.getName());
        }
        CdoDatastoreProvider<EntityMetadata, EntityDiscriminator, RelationMetadata, RelationDiscriminator> cdoDatastoreProvider = CdoDatastoreProvider.class.cast(ClassHelper.newInstance(providerType));
        this.datastore = cdoDatastoreProvider.createDatastore(cdoUnit);
        this.concurrencyMode = cdoUnit.getConcurrencyMode();
        this.defaultTransactionAttribute = cdoUnit.getDefaultTransactionAttribute();
        final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader parentClassLoader = contextClassLoader != null ? contextClassLoader : cdoUnit.getClass().getClassLoader();
        LOGGER.info("Using class loader '{}'.", parentClassLoader.toString());
        classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return parentClassLoader.loadClass(name);
            }
        };
        metadataProvider = new MetadataProviderImpl(cdoUnit.getTypes(), datastore);
        try {
            this.validatorFactory = Validation.buildDefaultValidatorFactory();
        } catch (ValidationException e) {
            LOGGER.debug("Cannot find validation provider.", e);
            LOGGER.info("No JSR 303 Bean Validation provider available.");
        }
        datastore.init(metadataProvider.getRegisteredMetadata());
    }

    @Override
    public CdoManager createCdoManager() {
        DatastoreSession<EntityId, Entity, EntityMetadata, EntityDiscriminator, RelationId, Relation, RelationMetadata, RelationDiscriminator> datastoreSession = datastore.createSession();
        SessionContext<EntityId, Entity, EntityMetadata, EntityDiscriminator, RelationId, Relation, RelationMetadata, RelationDiscriminator> sessionContext = new SessionContext<>(metadataProvider, datastoreSession, validatorFactory, defaultTransactionAttribute, concurrencyMode, classLoader);
        CdoManagerImpl<EntityId, Entity, EntityMetadata, EntityDiscriminator, RelationId, Relation, RelationMetadata, RelationDiscriminator> cdoManager = new CdoManagerImpl<>(sessionContext);
        return sessionContext.getInterceptorFactory().addInterceptor(cdoManager);
    }

    @Override
    public void close() {
        datastore.close();
    }

    public CdoUnit getCdoUnit() {
        return cdoUnit;
    }
}
