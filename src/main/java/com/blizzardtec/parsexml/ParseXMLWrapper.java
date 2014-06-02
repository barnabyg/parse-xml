/**
 *
 */
package com.blizzardtec.parsexml;

/**
 * Command line wrapper for parser. We allow System.out
 * as this is a command line tool.
 * @author bob
 *
 */
@SuppressWarnings("PMD.SystemPrintln")
public final class ParseXMLWrapper {

    /**
     * Private constructor.
     */
    private ParseXMLWrapper() {

    }

    /**
     * Project mode.
     */
    private static final String PROJECT = "project";
    /**
     * Cruisecontrol mode.
     */
    private static final String CRUISECONTROL = "cruisecontrol";

    /**
     * @param args arguments
     */
    public static void main(final String[] args) {

        boolean flag = true;

        if (args.length < 2) {
            System.out.println("ParseXML <" + PROJECT + "> <file name>");
            System.out.println("ParseXML" + " <"
                    + CRUISECONTROL
                    + "> <file name> <project name> <Maven base dir>");
            flag = false;
        } else if (PROJECT.equalsIgnoreCase(args[0])) {
            // make sure the correct number of arguments were passed
            if (args.length != 2) {
                System.out.println(
                        "Invalid number of arguments for"
                        + " project file modification");
                flag = false;
            }

            doProject(args[1]);

        } else if (CRUISECONTROL.equalsIgnoreCase(args[0])) {
            // make sure the correct number of arguments were passed
            if (args.length != 4) {
                System.out.println(
                        "Invalid number of args for"
                        + " cruisecontrol file modification");
                flag = false;
            }

            if (flag) {
                doCruisecontrol(args[1], args[2], args[3]);
            }
        }
    }

    /**
     * Project mode.
     * @param filename file name
     */
    private static void doProject(final String filename) {
        try {
            final ParseXML parser = new ParseXML(filename);
            parser.modifyProjectXML();
            parser.saveXMLFile(filename);
        } catch (ParseXMLException pxe) {
            System.out.println(pxe.getMessage());
        }
    }

    /**
     * Cruisecontrol mode.
     * @param filename file name
     * @param projectName the project name
     * @param mavenDir Maven base directory
     */
    private static void doCruisecontrol(final String filename,
                                        final String projectName,
                                        final String mavenDir) {
        try {
            final ParseXML parser = new ParseXML(filename);
            parser.modifyCruisecontrolXML(projectName, mavenDir);
            parser.saveXMLFile(filename);
        } catch (ParseXMLException pxe) {
            System.out.println(pxe.getMessage());
        }
    }
}
