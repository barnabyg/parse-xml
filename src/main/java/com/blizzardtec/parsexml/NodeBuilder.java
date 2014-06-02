/**
 *
 */
package com.blizzardtec.parsexml;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author bob
 *
 */
public final class NodeBuilder {

    /**
     * Private constructor denotes utility class.
     */
    private NodeBuilder() {

    }

    /**
     * Build the listeners node.
     *
     * @param doc
     *            XML document
     * @return listeners node
     */
    public static Node buildListenersNode(final Document doc) {
        // <listeners>
        // <currentbuildstatuslistener
        // file="logs/${project.name}/status.txt"/>
        // </listeners>
        final Node listenersNode = doc.createElement("listeners");
        final Node currentNode = doc
                .createElement("currentbuildstatuslistener");
        final NamedNodeMap atts = currentNode.getAttributes();
        final Attr fileAttribute = doc.createAttribute("file");
        fileAttribute.setValue("logs/${project.name}/status.txt");
        atts.setNamedItem(fileAttribute);
        listenersNode.appendChild(currentNode);

        return listenersNode;
    }

    /**
     * Build the bootstrappers node.
     *
     * @param doc
     *            XML document
     * @return bootstrappers node
     */
    public static Node buildBootstrapNode(final Document doc) {
        // <bootstrappers>
        // TBD
        // </bootstrappers>
        final Node bootstrapNode = doc.createElement("bootstrappers");

        return bootstrapNode;
    }

    /**
     * Build the modificationset node.
     *
     * @param doc
     *            XML document
     * @return modificationset node
     */
    public static Node buildModificationsetNode(final Document doc) {
        // <modificationset quietperiod="30">
        // <!-- touch any file in the project to trigger a build -->
        // <filesystem folder="projects/${project.name}"/>
        // </modificationset>
        final Node modNode = doc.createElement("modificationset");
        NamedNodeMap atts = modNode.getAttributes();
        final Attr quietPeriod = doc.createAttribute("quietperiod");
        quietPeriod.setValue("30");
        atts.setNamedItem(quietPeriod);
        final Node filesystemNode = doc.createElement("filesystem");
        atts = filesystemNode.getAttributes();
        final Attr folder = doc.createAttribute("folder");
        folder.setValue("projects/${project.name}");
        atts.setNamedItem(folder);
        modNode.appendChild(filesystemNode);

        return modNode;
    }

    /**
     * Build the schedule node.
     *
     * @param doc
     *            XML document
     * @param mavenDir
     *            Maven base directory
     * @return schedule node
     */
    public static Node buildScheduleNode(final String mavenDir,
            final Document doc) {
        // <schedule interval="300">
        // <maven2 mvnhome="D://java/apache-maven-2.2.1"
        // pomfile="projects/${project.name}/pom.xml" goal="package"/>
        // </schedule>
        final Node scheduleNode = doc.createElement("schedule");
        NamedNodeMap atts = scheduleNode.getAttributes();
        final Attr interval = doc.createAttribute("interval");
        interval.setValue("300");
        atts.setNamedItem(interval);
        final Node mavenNode = doc.createElement("maven2");
        atts = mavenNode.getAttributes();
        final Attr mvnhome = doc.createAttribute("mvnhome");
        mvnhome.setValue(mavenDir);
        final Attr pomfile = doc.createAttribute("pomfile");
        pomfile.setValue("projects/${project.name}/pom.xml");
        final Attr goal = doc.createAttribute("goal");
        goal.setValue("package");
        atts.setNamedItem(mvnhome);
        atts.setNamedItem(pomfile);
        atts.setNamedItem(goal);
        scheduleNode.appendChild(mavenNode);

        return scheduleNode;
    }

    /**
     * Buld the log node.
     *
     * @param doc
     *            XML document
     * @return log node
     */
    public static Node buildLogNode(final Document doc) {
        // <log>
        // <merge dir="projects/${project.name}/target/test-results"/>
        // </log>
        final Node logNode = doc.createElement("log");
        final Node mergeNode = doc.createElement("merge");
        final NamedNodeMap atts = mergeNode.getAttributes();
        final Attr dir = doc.createAttribute("dir");
        dir.setValue("projects/${project.name}/target/test-results");
        atts.setNamedItem(dir);
        logNode.appendChild(mergeNode);

        return logNode;
    }
}
