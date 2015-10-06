package com.project.neo;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

@ManagedBean(eager = false, name = "serviceManagerBean")
@ApplicationScoped
public class ServiceManagerBean implements Serializable {

    private static final String DB_PATH = "C:/neo4j/db/player_db";
    //private static final String DB_PATH = "C:\\Users\\armen arzumanyan\\Documents\\arxives\\a_projects_archive\\neo_books_codes\\neo-web\\database\\player_db";
    private final GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
    private final GraphDatabaseService databaseService = graphDbFactory.newEmbeddedDatabase(DB_PATH);

    public ServiceManagerBean() {
    }

    @PostConstruct
    public void init() {

    }

//    public GraphDatabaseService create() {
//        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
//        GraphDatabaseService db = graphDbFactory.newEmbeddedDatabase(DB_PATH);
//        return db;
//    }
//
//    public GraphDatabaseService createWithMoreMemory() {
//        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
//        GraphDatabaseService db = graphDbFactory
//                .newEmbeddedDatabaseBuilder(DB_PATH)
//                .setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "20M")
//                .newGraphDatabase();
//        return db;
//    }
//
//    public GraphDatabaseService createWithPropertiesFile() {
//        GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
//        try {
//            URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation();
//            String jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
//            URL propertyFileUrl = new URL("file:////" + jarPath + "neo4j.properties");
//
//            GraphDatabaseService db = graphDbFactory
//                    .newEmbeddedDatabaseBuilder(DB_PATH)
//                    .loadPropertiesFromURL(propertyFileUrl)
//                    .newGraphDatabase();
//            return db;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
//
//    public boolean clearAll() {
//        File f = new File(DB_PATH);
//        return delete(f);
//    }
//
//    private boolean delete(File f) {
//        if (f.isDirectory()) {
//            for (File c : f.listFiles()) {
//                delete(c);
//            }
//        }
//        return f.delete();
//    }
    public GraphDatabaseService getDatabaseService() {
        return databaseService;
    }

}
