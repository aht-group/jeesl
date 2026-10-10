package net.sf.ahtutils.controller.constraints;


import org.jeesl.controller.processor.system.constraint.ConstraintsChecker;
import org.jeesl.model.xml.io.locale.status.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConstraintsChecker
{

    final static Logger logger = LoggerFactory.getLogger(TestConstraintsChecker.class);
    int test;
    Status ru;

    @Test @Disabled
    public void testNull() throws Exception {
        ru = new Status();
        Assertions.assertFalse(ConstraintsChecker.notNull(ru, "image"));
    }

    @Test //@Disabled
    public void testNotNull() throws Exception {
        ru = new Status();
        ru.setPosition(1337);
        Assertions.assertTrue(ConstraintsChecker.notNull(ru, "position"));
    }
}
