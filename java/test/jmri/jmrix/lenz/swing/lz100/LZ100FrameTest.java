package jmri.jmrix.lenz.swing.lz100;

import java.awt.GraphicsEnvironment;
import jmri.jmrix.lenz.LenzCommandStation;
import jmri.jmrix.lenz.XNetInterfaceScaffold;
import jmri.jmrix.lenz.XNetSystemConnectionMemo;
import jmri.util.JUnitUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * LZ100FrameTest.java
 *
 * Description:	tests for the jmri.jmrix.lenz.swing.lz100.LZ100Frame class
 *
 * @author	Paul Bender
 */
public class LZ100FrameTest {

    @Test
    public void testCtor() {
        Assume.assumeFalse(GraphicsEnvironment.isHeadless());
        // infrastructure objects
        XNetInterfaceScaffold tc = new XNetInterfaceScaffold(new LenzCommandStation());

        LZ100Frame f = new LZ100Frame(new XNetSystemConnectionMemo(tc));
        Assert.assertNotNull(f);
        f.dispose();
    }

    // The minimal setup for log4J
    @Before
    public void setUp() {
        JUnitUtil.setUp();
        jmri.util.JUnitUtil.resetProfileManager();
    }

    @After
    public void tearDown() {
        JUnitUtil.tearDown();
    }

}
