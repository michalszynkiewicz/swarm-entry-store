package com.github.michalszynkiewicz.entrystore;

import com.github.michalszynkiewicz.entrystore.dao.EntryDao;
import com.github.michalszynkiewicz.entrystore.model.Entry;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.swarm.spi.api.JARArchive;

import javax.inject.Inject;

import static com.github.michalszynkiewicz.entrystore.ShrinkWrapUtils.addDependency;
import static com.github.michalszynkiewicz.entrystore.ShrinkWrapUtils.createDefaultJar;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 1/30/17
 * Time: 12:00 AM
 */
@RunWith(Arquillian.class)
public class ExampleTest {

    @Inject
    private EntryDao dao;

    @Test
    public void testMyComponent() {
        Entry entry = new Entry("michal", "I give up");

        dao.save(entry);
        assertThat(dao.getAll()).hasSize(1);
    }

    @Deployment
    public static Archive createDeployment() {
        JARArchive archive = createDefaultJar(ExampleTest.class);
        return addDependency(archive, "org.assertj:assertj-core:3.5.2");
    }
}
