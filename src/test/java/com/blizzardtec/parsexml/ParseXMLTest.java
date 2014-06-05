/**
 *
 */
package com.blizzardtec.parsexml;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author bob
 *
 */
public final class ParseXMLTest {

    /**
     * Test XML file.
     */
    private static final String FILENAME =
        "src/test/resources/test1.xml";

    /**
     * Output file after parsing.
     */
    private static final String OUT_FILENAME =
        "src/test/resources/output.xml";

    /**
     * Test the loading of an XML file.
     * @throws ParseXMLException thrown
     */
    @Test
    public void loadXMLFileTest() throws ParseXMLException {
        final ParseXML xmlparse = new ParseXML(FILENAME);
        assertNotNull("xmlparse result was null", xmlparse);
    }

    /**
     * Modify the XML in a .project file to include
     * Checkstyle and PMD.
     *
     * @throws ParseXMLException thrown
     */
    @Test
    public void modifyProjectXMLTest() throws ParseXMLException {
        final ParseXML xmlparse = new ParseXML(FILENAME);
        assertNotNull("the xmlparse result was null", xmlparse);
        xmlparse.modifyProjectXML();
    }

    /**
     * Test the saving of an XML file.
     * @throws ParseXMLException thrown
     */
    @Test
    public void saveXMLFileTest() throws ParseXMLException {
        final ParseXML xmlparse = new ParseXML(FILENAME);
        assertNotNull("result was null", xmlparse);
        xmlparse.modifyProjectXML();
        xmlparse.saveXMLFile(OUT_FILENAME);
    }
}
