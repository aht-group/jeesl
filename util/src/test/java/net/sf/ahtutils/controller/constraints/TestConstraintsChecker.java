package net.sf.ahtutils.controller.constraints;


import net.sf.ahtutils.xml.status.Status;

import org.jeesl.controller.processor.system.constraint.ConstraintsChecker;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConstraintsChecker
{

    final static Logger logger = LoggerFactory.getLogger(TestConstraintsChecker.class);
    int test;
    Status ru;

    @Test @Ignore
    public void testNull() throws Exception {
        ru = new Status();
        Assert.assertFalse(ConstraintsChecker.notNull(ru, "image"));
    }

    @Test //@Ignore
    public void testNotNull() throws Exception {
        ru = new Status();
        ru.setPosition(1337);
        Assert.assertTrue(ConstraintsChecker.notNull(ru, "position"));
    }
}
