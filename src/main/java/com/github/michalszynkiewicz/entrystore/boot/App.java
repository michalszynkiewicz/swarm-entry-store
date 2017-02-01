package com.github.michalszynkiewicz.entrystore.boot;

import com.github.michalszynkiewicz.entrystore.endpoint.EntryEndpoint;
import org.jboss.shrinkwrap.api.Archive;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.cdi.CDIFraction;
import org.wildfly.swarm.config.logging.Level;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.monitor.MonitorFraction;
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
                .start()
//                .deploy();
                .deploy(deployment(swarm));
    }

    private static Archive deployment(Swarm swarm) throws Exception {
        JAXRSArchive deployment = swarm.createDefaultDeployment().as(JAXRSArchive.class); // mstodo remove?
        deployment.addResource(RestApplication.class);
        deployment.addClass(EntryEndpoint.class);
        // Enable the swagger bits
        SwaggerArchive archive = deployment.as(SwaggerArchive.class);
        // Tell swagger where our resources are
        archive.setResourcePackages(
                "com.github.michalszynkiewicz.entrystore.endpoint",
                "com.github.michalszynkiewicz.entrystore.boot");
        archive.setTitle("Entry store");

        deployment.addAllDependencies();

        return deployment;
    }

    public static Swarm createSwarm(String... args) throws Exception {
        ClassLoader cl = App.class.getClassLoader();
        URL stageConfig = cl.getResource("project-stages.yml");

        return new Swarm(args)
                .withStageConfig(stageConfig)
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
                .rootLogger(Level.DEBUG, "FILE");
    }
}
