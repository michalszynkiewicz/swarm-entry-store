package com.github.michalszynkiewicz.entrystore.boot;

import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.cdi.CDIFraction;
import org.wildfly.swarm.config.logging.Level;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.monitor.MonitorFraction;

import java.net.URL;

/**
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 1/29/17
 * Time: 10:54 PM
 */
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Initializing app from main");
        createSwarm(args)
//                .fraction(new JAXRSFraction())
                .start()
                .deploy();
    }

    public static Swarm createSwarm(String... args) throws Exception {
        ClassLoader cl = App.class.getClassLoader();
        URL stageConfig = cl.getResource("project-stages.yml");

        return new Swarm(args)
                .withStageConfig(stageConfig)
                .fraction(new LoggingFraction().rootLogger(Level.DEBUG))
                .fraction(new CDIFraction())
                .fraction(new MonitorFraction());
    }
}
