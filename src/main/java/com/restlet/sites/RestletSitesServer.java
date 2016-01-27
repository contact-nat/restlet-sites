/*
 * Copyright 2005-2016 Restlet. All rights reserved.
 */

package com.restlet.sites;


import com.restlet.sites.web.WebComponent;
import org.restlet.representation.InputRepresentation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.String.format;

class RestletSitesServer {

    /**
     * Main method.
     *
     * @param args Program arguments.
     */
    public static void main(String[] args) throws Exception {
        Path vHostsConfigurationRootPath = null;
        Path globalConfigurationPath = null;
        for (int i = 0; i < args.length; i++) {
            String argument = args[i];
            switch (argument) {
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;
                case "--help":
                    printHelp();
                    System.exit(0);
                    break;
                case "-v":
                    vHostsConfigurationRootPath = checkVHostsConfigurationPath(args[++i]);
                    break;
                case "--virtual-hosts-configuration":
                    vHostsConfigurationRootPath = checkVHostsConfigurationPath(args[++i]);
                    break;
                case "-g":
                    globalConfigurationPath = checkGlobalConfigurationPath(args[++i]);
                    break;
                case "--global-configuration":
                    globalConfigurationPath = checkGlobalConfigurationPath(args[++i]);
                    break;
                case "--display-global-default-configuration":
                    displayDefaultGlobalPropertiesFile();
                    break;
                case "-dg":
                    displayDefaultGlobalPropertiesFile();
                    break;
                case "--display-sample-host-configuration":
                    displaySampleHostPropertiesFile();
                    break;
                case "-s":
                    displaySampleHostPropertiesFile();
                    break;
                case "--display-sample-jmxterm-commands":
                    displaySampleJmxCommands();
                    break;
                case "-j":
                    displaySampleJmxCommands();
                    break;
                default:
                    displayErrorAndQuit(String.format("This argument is unknown %s", argument));
            }
        }
        if (vHostsConfigurationRootPath == null) {
            printHelp();
            System.exit(0);
        }

        new WebComponent(Optional.ofNullable(globalConfigurationPath), vHostsConfigurationRootPath).start();
    }

    private static void displayDefaultGlobalPropertiesFile() {
        displaySampleFile("restlet-sites-default-global.properties.txt", "global default properties");
    }

    private static void displaySampleHostPropertiesFile() {
        displaySampleFile("restlet-sites-sample-host.properties.txt", "sample host properties");
    }
    private static void displaySampleJmxCommands() {
        displaySampleFile("restlet-sites-sample-jmxterm.txt", "sample jmxterm commands");
    }

    private static void displaySampleFile(String sampleFile, String title) {
        try (InputStream is = RestletSitesServer.class.getResourceAsStream(format("/%s", sampleFile))) {
            if (is == null) {
                System.out.println(format("Cannot display %s, as it seems that the file `%s` is missing from the packaged jar.", title, sampleFile));
                System.exit(0);
            }
            new InputRepresentation(is).write(System.out);
            System.exit(0);
        } catch (IOException ioe) {
            System.err.println(format("Can't read `%s` (taken from packaged jar).", sampleFile));
            System.exit(1);
        }

    }

    private static void displayErrorAndQuit(String message) {
        System.err.println(message);
        printHelp();
        System.exit(1);
    }

    private static Path checkVHostsConfigurationPath(String vHostsConfigurationPath) {
        Path path = Paths.get(vHostsConfigurationPath);
        if (!Files.isDirectory(path)) {
            displayErrorAndQuit(String.format("This path is not a valid directory: %s", vHostsConfigurationPath));
        }
        return path;
    }

    private static Path checkGlobalConfigurationPath(String globalConfigurationPath) {
        Path path = Paths.get(globalConfigurationPath);
        if (!Files.isRegularFile(path)) {
            displayErrorAndQuit(String.format("This path is not a valid file: %s", globalConfigurationPath));
        }
        return path;
    }

    private static void printHelp() {
        System.out.println("Serves a set of Web resources described in properties file.");
        System.out.println();
        System.out.println("Command line: [--help|-h] (--virtual-hosts-configuration|-v) directory [(--global-configuration|-g) file] [(--display-default-configuration|-dg)]");
        System.out.println();
        System.out.println("  -h,  --help                                : displays this help");
        System.out.println("  -v,  --virtual-hosts-configuration         : mandatory directory where to find descriptors of virtual hosts");
        System.out.println("  -g,  --global-configuration                : optional path to the global properties file");
        System.out.println("  -dg, --display-global-default-configuration: displays the default global configuration file");
        System.out.println("  -s,  --display-sample-host-configuration   : displays a sample host configuration file");
        System.out.println("  -j,  --display-sample-jmxterm-commands         : displays sample jmxterm commands");
        System.out.println();
        System.out.println("Sample command line parameters:");
        System.out.println("  reads the virtual hosts configuration files from '/etc/web-sites/configuration', and global properties from '/etc/web-sites/configuration/global.properties'");
        System.out.println("              -v /etc/web-sites/configuration -g /etc/web-sites/configuration/global.properties");
        System.out.println();
        System.out.println("  displays the default global configuration file");
        System.out.println("              -dg");
    }

}
