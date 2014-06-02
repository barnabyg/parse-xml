/**
 *
 */
package com.blizzardtec.parsexml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author bob
 *
 */
public final class ParseXML {

    /**
     * Name.
     */
    private static final String NAME = "name";
    /**
     * XML document.
     */
    private final transient Document doc;

    /**
     * Constructor - takes the XML file name to be parsed.
     *
     * @param filename file name
     * @throws ParseXMLException thrown
     */
    public ParseXML(final String filename)
        throws ParseXMLException {
        final DocumentBuilderFactory docFactory =
                   DocumentBuilderFactory.newInstance();

        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            throw new ParseXMLException(pce);
        }

        try {
            doc = docBuilder.parse(filename);
        } catch (IOException ioe) {
            throw new ParseXMLException(ioe);
        } catch (SAXException sae) {
            throw new ParseXMLException(sae);
        }
    }

    /**
     * Modify the config.xml file to add in a new project.
     * Effectively builds a block of XML and inserts it as a new project
     * node.
     * @param projectName the name of the new project
     * @param mavenDir Maven base directory
     */
    public void modifyCruisecontrolXML(
                 final String projectName, final String mavenDir) {
        // select the <cruisecontrol> node
        final Node cruisecontrolNode = doc.getFirstChild();

        // build the new project node
        final Node newProjectNode = doc.createElement("project");
        final NamedNodeMap atts = newProjectNode.getAttributes();
        final Attr projName = doc.createAttribute(NAME);
        projName.setValue(projectName);
        atts.setNamedItem(projName);

        // append the various nodes to the project node
        newProjectNode.appendChild(NodeBuilder.buildListenersNode(doc));
        newProjectNode.appendChild(NodeBuilder.buildBootstrapNode(doc));
        newProjectNode.appendChild(NodeBuilder.buildModificationsetNode(doc));
        newProjectNode.appendChild(
                NodeBuilder.buildScheduleNode(mavenDir, doc));
        newProjectNode.appendChild(NodeBuilder.buildLogNode(doc));
        newProjectNode.appendChild(buildPublishersNode());
        cruisecontrolNode.appendChild(newProjectNode);
    }

    /**
     * Build publishers node.
     * @return publishers node
     */
    private Node buildPublishersNode() {
        // <publishers>
        //   <onsuccess>
        //     <artifactspublisher dest="artifacts/${project.name}"
        //                     file="projects/${project.name}/
        //                   target/${project.name}-1.0-SNAPSHOT.jar"/>
        //   </onsuccess>
        // </publishers>
        final Node publishersNode = doc.createElement("publishers");
        final Node onsuccessNode = doc.createElement("onsuccess");
        final Node artifactNode = doc.createElement("artifactspublisher");
        final NamedNodeMap atts = artifactNode.getAttributes();
        final Attr dest = doc.createAttribute("dest");
        dest.setValue("artifacts/${project.name}");
        final Attr file2 = doc.createAttribute("file");
        file2.setValue(
            "projects/${project.name}/target/${project.name}-1.0-SNAPSHOT.jar");
        atts.setNamedItem(dest);
        atts.setNamedItem(file2);
        onsuccessNode.appendChild(artifactNode);
        publishersNode.appendChild(onsuccessNode);

        return publishersNode;
    }

    /**
     * Modify .project file XML to add in Checkstyle and PMD
     * configuration.
     */
    public void modifyProjectXML() {
        // select the <projectDescription> node
        final Node projDesNode = doc.getFirstChild();
        // find the <buildSpec> and <natures> nodes
        final NodeList projNodeList = projDesNode.getChildNodes();
        Node buildSpecNode = null;
        Node naturesNode = null;
        for (int counter = 0; counter < projNodeList.getLength(); counter++) {
            final Node node = projNodeList.item(counter);
            if ("buildSpec".equals(node.getNodeName())) {
                buildSpecNode = node;
            }
            if ("natures".equals(node.getNodeName())) {
                naturesNode = node;
            }
        }

        // create new buildCommand node for PMD
        buildSpecNode.appendChild(
                createBuildComNode(
                        "net.sourceforge.pmd.eclipse.plugin.pmdBuilder"));

        // create new buildCommand node for Checkstyle
        buildSpecNode.appendChild(
                createBuildComNode(
                        "net.sf.eclipsecs.core.CheckstyleBuilder"));

        naturesNode.appendChild(
                createNatureNode(
                        "net.sourceforge.pmd.eclipse.plugin.pmdNature"));

        naturesNode.appendChild(
                createNatureNode(
                        "net.sf.eclipsecs.core.CheckstyleNature"));
    }

    /**
     * Build a buildCommand node with the given name.
     * @param name the name to use
     * @return returns the node
     */
    private Node createBuildComNode(final String name) {
        final Node buildComNode = doc.createElement("buildCommand");
        final Node nameNode = doc.createElement(NAME);
        nameNode.setTextContent(name);
        final Node argumentsNode = doc.createElement("arguments");
        buildComNode.appendChild(nameNode);
        buildComNode.appendChild(argumentsNode);
        return buildComNode;
    }

    /**
     * Build a nature node with the given text content.
     * @param name the text content
     * @return returns the node
     */
    private Node createNatureNode(final String name) {
        final Node natureNode = doc.createElement("nature");
        natureNode.setTextContent(name);

        return natureNode;
    }

    /**
     * Save the XML document to a file.
     * @param filename file name
     * @throws ParseXMLException thrown
     */
    public void saveXMLFile(final String filename)
                            throws ParseXMLException {
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException tce) {
            throw new ParseXMLException(tce);
        }

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //initialise StreamResult with File object to save to file
        final StreamResult result =
            new StreamResult(new StringWriter());
        final DOMSource source = new DOMSource(doc);
        try {
            transformer.transform(source, result);
        } catch (TransformerException tfe) {
            throw new ParseXMLException(tfe);
        }

        final String xmlString = result.getWriter().toString();

        Writer output = null;
        final File file = new File(filename);

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write(xmlString);
            output.close();
        } catch (IOException ioe) {
            throw new ParseXMLException(ioe);
        }
    }
}
