// SerialTrafficControllerTest.java

package jmri.jmrix.grapevine;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import junit.framework.Test;
import junit.framework.Assert;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import jmri.jmrix.AbstractMRReply;

/**
 * JUnit tests for the SerialTrafficController class
 * @author			Bob Jacobsen Copyright 2005, 2007
 * @version $Revision: 1.1 $
 */
public class SerialTrafficControllerTest extends TestCase {

    public void testCreate() {
        SerialTrafficController m = new SerialTrafficController();
    }
    
    byte[] testBuffer;
    boolean invoked;
    
    public void testStateMachine1() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{1,2,3,4}));
        
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("1st byte not address: 1");
        Assert.assertEquals("not invoked", false, invoked);
    }
    
    public void testStateMachine2() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{(byte)128,(byte)129,3,4}));
        
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("2nd byte HOB set: 129");
        Assert.assertEquals("not invoked", false, invoked);
    }
    
    public void testStateMachine3() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{(byte)128,(byte)12,1,4}));
        
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("addresses don't match: 128, 1");
        Assert.assertEquals("not invoked", false, invoked);
    }
    
    public void testStateMachine4() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{(byte)128,(byte)12,(byte)128,(byte)129}));
        
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("3rd byte HOB set: 129");
        Assert.assertEquals("not invoked", false, invoked);
    }
    
    public void testStateMachine5() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{(byte)129,(byte)90,(byte)129,(byte)32}));
        
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("parity mismatch: 18");
        Assert.assertEquals("not invoked", false, invoked);
    }
    
    public void testStateMachineOK1() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{(byte)129,(byte)90,(byte)129,(byte)31}));
        
        c.doNextStep(null,i);
        
        Assert.assertEquals("invoked", true, invoked);
        Assert.assertEquals("byte 0", (byte)129, testBuffer[0]);
        Assert.assertEquals("byte 1", (byte)90, testBuffer[1]);
        Assert.assertEquals("byte 2", (byte)129, testBuffer[2]);
        Assert.assertEquals("byte 3", (byte)31, testBuffer[3]);
    }
    
    public void testStateMachineRecover1() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{12,(byte)129,(byte)90,(byte)129,(byte)31}));
        
        c.doNextStep(null,i);
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("1st byte not address: 12");
        Assert.assertEquals("invoked", true, invoked);
        Assert.assertEquals("byte 0", (byte)129, testBuffer[0]);
        Assert.assertEquals("byte 1", (byte)90, testBuffer[1]);
        Assert.assertEquals("byte 2", (byte)129, testBuffer[2]);
        Assert.assertEquals("byte 3", (byte)31, testBuffer[3]);
    }
    
    public void testStateMachineRecover2() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{(byte)129,(byte)129,(byte)90,(byte)129,(byte)31}));
        
        c.doNextStep(null,i);
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("2nd byte HOB set: 129");
        Assert.assertEquals("invoked", true, invoked);
        Assert.assertEquals("byte 0", (byte)129, testBuffer[0]);
        Assert.assertEquals("byte 1", (byte)90, testBuffer[1]);
        Assert.assertEquals("byte 2", (byte)129, testBuffer[2]);
        Assert.assertEquals("byte 3", (byte)31, testBuffer[3]);
    }
    
    public void testStateMachineRecover3() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{(byte)128,(byte)12,(byte)129,(byte)90,(byte)129,(byte)31}));
        
        c.doNextStep(null,i);
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("addresses don't match: 128, 129");
        Assert.assertEquals("invoked", true, invoked);
        Assert.assertEquals("byte 0", (byte)129, testBuffer[0]);
        Assert.assertEquals("byte 1", (byte)90, testBuffer[1]);
        Assert.assertEquals("byte 2", (byte)129, testBuffer[2]);
        Assert.assertEquals("byte 3", (byte)31, testBuffer[3]);
    }
    
    public void testStateMachineRecover4() throws java.io.IOException {
        SerialTrafficController c = new SerialTrafficController(){
            void loadBuffer(AbstractMRReply msg) {        
                testBuffer[0] = buffer[0];testBuffer[1] = buffer[1];
                testBuffer[2] = buffer[2];testBuffer[3] = buffer[3];
                invoked = true;
            }
        };
        testBuffer = new byte[4];invoked = false;
        
        DataInputStream i = new DataInputStream(new ByteArrayInputStream(
                            new byte[]{(byte)129,(byte)12,(byte)129,(byte)90,(byte)129,(byte)31}));
        
        c.doNextStep(null,i);
        c.doNextStep(null,i);
        
        jmri.util.JUnitAppender.assertWarnMessage("parity mismatch: 28");
        Assert.assertEquals("invoked", true, invoked);
        Assert.assertEquals("byte 0", (byte)129, testBuffer[0]);
        Assert.assertEquals("byte 1", (byte)90, testBuffer[1]);
        Assert.assertEquals("byte 2", (byte)129, testBuffer[2]);
        Assert.assertEquals("byte 3", (byte)31, testBuffer[3]);
    }
    
    
    public void testSerialNodeEnumeration() {
        SerialTrafficController c = new SerialTrafficController();
        SerialNode b = new SerialNode(1,SerialNode.NODE2002V6);
        SerialNode f = new SerialNode(3,SerialNode.NODE2002V1);
        SerialNode d = new SerialNode(2,SerialNode.NODE2002V1);
        SerialNode e = new SerialNode(6,SerialNode.NODE2002V6);
        Assert.assertEquals("1st Node", b, c.getSerialNode(0) );
        Assert.assertEquals("2nd Node", f, c.getSerialNode(1) );
        Assert.assertEquals("3rd Node", d, c.getSerialNode(2) );
        Assert.assertEquals("4th Node", e, c.getSerialNode(3) );
        Assert.assertEquals("no more Nodes", null, c.getSerialNode(4) );
        Assert.assertEquals("1st Node Again", b, c.getSerialNode(0) );
        Assert.assertEquals("2nd Node Again", f, c.getSerialNode(1) );
        Assert.assertEquals("node with address 6", e, c.getNodeFromAddress(6) );
        Assert.assertEquals("3rd Node again", d, c.getSerialNode(2) );
        Assert.assertEquals("no node with address 0", null, c.getNodeFromAddress(0) );
        c.deleteSerialNode(6);
        Assert.assertEquals("1st Node after del", b, c.getSerialNode(0) );
        Assert.assertEquals("2nd Node after del", f, c.getSerialNode(1) );
        Assert.assertEquals("3rd Node after del", d, c.getSerialNode(2) );
        Assert.assertEquals("no more Nodes after del", null, c.getSerialNode(3) );
        c.deleteSerialNode(1);
        jmri.util.JUnitAppender.assertWarnMessage("Deleting the serial node active in the polling loop");
        Assert.assertEquals("1st Node after del2", f, c.getSerialNode(0) );
        Assert.assertEquals("2nd Node after del2", d, c.getSerialNode(1) );
        Assert.assertEquals("no more Nodes after del2", null, c.getSerialNode(2) );        
    }
    public void testSerialOutput() {
        SerialTrafficController c = new SerialTrafficController();
        SerialNode a = new SerialNode();
        SerialNode g = new SerialNode(5,SerialNode.NODE2002V1);
        Assert.assertTrue("must Send", g.mustSend() );
        g.resetMustSend();
        Assert.assertTrue("must Send off", !(g.mustSend()) );
        c.setSerialOutput("GL5B2",false);
        c.setSerialOutput("GL5B1",false);
        c.setSerialOutput("GL5B7",false);
        c.setSerialOutput("GL5B3",false);
        c.setSerialOutput("GL5B5",false);
        c.setSerialOutput("GL5B8",true);
        c.setSerialOutput("GL5B11",false);
        c.setSerialOutput("GL5B5",false);
        c.setSerialOutput("GL5B10",false);
        c.setSerialOutput("GL5B9",false);
        Assert.assertTrue("must Send on", g.mustSend() );
        SerialMessage m = g.createOutPacket();
        Assert.assertEquals("packet size", 4, m.getNumDataElements() );
        Assert.assertEquals("node address", 5, m.getElement(0) );
        Assert.assertEquals("packet type", 17, m.getElement(1) );  // 'T'        
    }

    private boolean waitForReply() {
        // wait for reply (normally, done by callback; will check that later)
        int i = 0;
        while ( rcvdReply == null && i++ < 100  )  {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
        if (log.isDebugEnabled()) log.debug("past loop, i="+i
                                            +" reply="+rcvdReply);
        if (i==0) log.warn("waitForReply saw an immediate return; is threading right?");
        return i<100;
    }

    // internal class to simulate a Listener
    class SerialListenerScaffold implements SerialListener {
        public SerialListenerScaffold() {
            rcvdReply = null;
            rcvdMsg = null;
        }
        public void message(SerialMessage m) {rcvdMsg = m;}
        public void reply(SerialReply r) {rcvdReply = r;}
    }
    SerialReply rcvdReply;
    SerialMessage rcvdMsg;

    // internal class to simulate a PortController
    class SerialPortControllerScaffold extends SerialPortController {
            public java.util.Vector getPortNames() { return null; }
	    public String openPort(String portName, String appName) { return null; }
	    public void configure() {}
	    public String[] validBaudRates() { return null; }
        protected SerialPortControllerScaffold() throws Exception {
            PipedInputStream tempPipe;
            tempPipe = new PipedInputStream();
            tostream = new DataInputStream(tempPipe);
            ostream = new DataOutputStream(new PipedOutputStream(tempPipe));
            tempPipe = new PipedInputStream();
            istream = new DataInputStream(tempPipe);
            tistream = new DataOutputStream(new PipedOutputStream(tempPipe));
        }

        // returns the InputStream from the port
        public DataInputStream getInputStream() { return istream; }

        // returns the outputStream to the port
        public DataOutputStream getOutputStream() { return ostream; }

        // check that this object is ready to operate
        public boolean status() { return true; }
    }
    static DataOutputStream ostream;  // Traffic controller writes to this
    static DataInputStream  tostream; // so we can read it from this

    static DataOutputStream tistream; // tests write to this
    static DataInputStream  istream;  // so the traffic controller can read from this

    // from here down is testing infrastructure

    public SerialTrafficControllerTest(String s) {
        super(s);
    }

    // Main entry point
    static public void main(String[] args) {
        String[] testCaseName = {SerialTrafficControllerTest.class.getName()};
        junit.swingui.TestRunner.main(testCaseName);
    }

    // test suite from all defined tests
    public static Test suite() {
        TestSuite suite = new TestSuite(SerialTrafficControllerTest.class);
        return suite;
    }
    // The minimal setup for log4J
    protected void setUp() { apps.tests.Log4JFixture.setUp(); }
    protected void tearDown() { apps.tests.Log4JFixture.tearDown(); }
    
    static org.apache.log4j.Category log = org.apache.log4j.Category.getInstance(SerialTrafficControllerTest.class.getName());

}
