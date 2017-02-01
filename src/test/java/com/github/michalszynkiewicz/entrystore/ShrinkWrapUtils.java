package com.github.michalszynkiewicz.entrystore;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.URLPackageScanner;
import org.jboss.shrinkwrap.impl.base.asset.AssetUtil;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.wildfly.swarm.spi.api.JARArchive;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Mostly copied from @link{{@link org.wildfly.swarm.arquillian.adapter.DefaultDeploymentScenarioGenerator}}
 *
 * Author: Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * Date: 1/31/17
 * Time: 12:33 AM
 */
public class ShrinkWrapUtils {

    public static Archive addDependency(JARArchive archive, String dependency) {
        File[] files = Maven.resolver().resolve(dependency).withTransitivity().asFile();

        ZipImporter imported = ShrinkWrap.create(ZipImporter.class);
        Stream.of(files).forEach(imported::importFrom);

        return archive.merge(imported.as(JavaArchive.class));
    }

    public static JARArchive createDefaultJar(Class<?> aClass) {   // mstodo try to reuse swarm getDefaultDeployment!
        JARArchive archive = ShrinkWrap.create(JARArchive.class);
        ClassLoader cl = aClass.getClassLoader();

        Set<CodeSource> codeSources = new HashSet<>();

        URLPackageScanner.Callback callback = (className, asset) -> {
            ArchivePath classNamePath = AssetUtil.getFullPathForClassResource(className);
            ArchivePath location = new BasicPath("", classNamePath);
            archive.add(asset, location);

            try {
                Class<?> cls = cl.loadClass(className);
                codeSources.add(cls.getProtectionDomain().getCodeSource());
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                e.printStackTrace();
            }
        };

        URLPackageScanner scanner = URLPackageScanner.newInstance(
                true,
                cl,
                callback,
                aClass.getPackage().getName());

        scanner.scanPackage();

        Set<String> prefixes = codeSources.stream().map(e -> e.getLocation().toExternalForm()).collect(Collectors.toSet());

        try {
            List<URL> resources = Collections.list(cl.getResources(""));

            resources.stream()
                    .filter(e -> {
                        for (String prefix : prefixes) {
                            if (e.toExternalForm().startsWith(prefix)) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .filter(e -> e.getProtocol().equals("file"))
                    .map(e -> getPlatformPath(e.getPath()))
                    .map(e -> Paths.get(e))
                    .filter(e -> Files.isDirectory(e))
                    .forEach(e -> {
                        try {
                            Files.walkFileTree(e, new SimpleFileVisitor<Path>() {
                                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                                    if (!file.toString().endsWith(".class")) {
                                        Path location = e.relativize(file);
                                        archive.add(new FileAsset(file.toFile()), javaSlashize(location));
                                    }
                                    return super.visitFile(file, attrs);
                                }
                            });
                        } catch (IOException e1) {
                        }
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return archive;
    }


    protected static String getPlatformPath(String path) {
        if (!isWindows()) {
            return path;
        }

        URI uri = URI.create("file://" + path);
        return Paths.get(uri).toString();
    }

    protected static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static String javaSlashize(Path path) {

        List<String> parts = new ArrayList<>();

        int numParts = path.getNameCount();

        for ( int i = 0 ; i < numParts ; ++i ) {
            parts.add( path.getName(i).toString());
        }


        return String.join( "/", parts);

    }

}
