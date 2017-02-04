package com.github.michalszynkiewicz.entrystore.boot;

import org.jboss.shrinkwrap.api.Archive;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.cdi.CDIFraction;
import org.wildfly.swarm.config.logging.Level;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.management.ManagementFraction;
import org.wildfly.swarm.monitor.MonitorFraction;
import org.wildfly.swarm.spi.api.Fraction;
import org.wildfly.swarm.swagger.SwaggerArchive;
import org.wildfly.swarm.swagger.SwaggerFraction;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 1/29/17
 * Time: 10:54 PM
 */
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Initializing app from main");

        Swarm swarm = createSwarm(args);
        swarm.fraction(logging())
                .fraction(security())
                .start()
                .deploy(deployment(swarm));
    }

    private static Fraction security() {
        // mstodo!!!
        return new ManagementFraction()
                .securityRealm("ManagementRealm", (realm) -> {
                    realm.inMemoryAuthentication((authn) -> {
                        authn.add("admin", "password", true);
                    });
                    realm.inMemoryAuthorization((authz) -> {
                        authz.add("bob", "admin");
                    });
                });
    }

    private static Archive deployment(Swarm swarm) throws Exception {
        JAXRSArchive deployment = swarm.createDefaultDeployment().as(JAXRSArchive.class);

        SwaggerArchive archive = deployment.as(SwaggerArchive.class);
        archive.setResourcePackages("com.github.michalszynkiewicz.entrystore.endpoint");
        archive.setPrettyPrint(true);
        archive.setTitle("Entry store");
        archive.setContextRoot("/rest");

        deployment.addAllDependencies();

        return deployment;
    }

    public static Swarm createSwarm(String... args) throws Exception {
        ClassLoader cl = App.class.getClassLoader();
        URL stageConfig = cl.getResource("project-stages.yml");

        return new Swarm(args)
                .fraction(new CDIFraction())
                .fraction(new SwaggerFraction())
                .fraction(new MonitorFraction());
    }

    private static LoggingFraction logging() {
        String logFile = System.getProperty("user.dir") + File.separator + "target" + File.separator + "debug.log";

        return new LoggingFraction()
                .formatter("default", "%K{level}%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n")
                .consoleHandler(Level.INFO, "default")
                .fileHandler("FILE", f -> {

                    Map<String, String> fileProps = new HashMap<>();
                    fileProps.put("path", logFile);
                    f.file(fileProps);
                    f.level(Level.DEBUG);
                    f.formatter("%K{level}%d{HH:mm:ss,SSS} %-5p [%c] (%t) %s%e%n");

                })
                .rootLogger(Level.DEBUG, "FILE", "CONSOLE");
    }
}
