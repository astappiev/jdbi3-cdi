package io.github.patrikostberg.jdbi.v3.cdi;

import javax.inject.Inject;

import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.patrikostberg.jdbi.v3.cdi.beans.JdbiProvider;
import io.github.patrikostberg.jdbi.v3.cdi.dao.TestDao;
import io.github.patrikostberg.jdbi.v3.cdi.extension.JdbiExtension;

@EnableAutoWeld
@AddExtensions(JdbiExtension.class)
@AddBeanClasses({JdbiProvider.class, TestDao.class})
public class JdbiExtensionTest {

    @Inject
    private TestDao testDao;

    @Test
    public void testInjectNotNull() {
        Assertions.assertNotNull(testDao);
    }

    @Test
    public void testDummy() {
        Assertions.assertEquals("1.4.200", testDao.getVersion());
    }
}
