package com.buschmais.cdo.neo4j.test.composite.migration;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.neo4j.test.composite.AbstractCdoManagerTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MigrationTest extends AbstractCdoManagerTest {

    @Override
    protected Class<?>[] getTypes() {
        return new Class<?>[]{A.class, B.class, C.class};
    }

    @Test
    public void downCast() {
        CdoManager cdoManager = getCdoManagerFactory().createCdoManager();
        cdoManager.begin();
        A a = cdoManager.create(A.class);
        a.setValue("Value");
        cdoManager.commit();
        cdoManager.begin();
        B b = cdoManager.migrate(a, B.class);
        assertThat(a == b, equalTo(false));
        assertThat(b.getValue(), equalTo("Value"));
        cdoManager.commit();
        cdoManager.close();
    }

    @Test
    public void migrationHandler() {
        CdoManager cdoManager = getCdoManagerFactory().createCdoManager();
        cdoManager.begin();
        A a = cdoManager.create(A.class);
        a.setValue("Value");
        cdoManager.commit();
        cdoManager.begin();
        CdoManager.MigrationHandler<A, C> migrationHandler = new CdoManager.MigrationHandler<A, C>() {
            @Override
            public void migrate(A instance, C target) {
                target.setName(instance.getValue());
            }
        };
        C c = cdoManager.migrate(a, C.class, migrationHandler);
        assertThat(c.getName(), equalTo("Value"));
        cdoManager.commit();
        cdoManager.close();
    }
}
