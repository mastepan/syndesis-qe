package io.syndesis.qe.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import io.fabric8.kubernetes.client.LocalPortForward;
import lombok.extern.slf4j.Slf4j;

/**
 * Nov 15, 2017 Red Hat
 *
 * @author tplevko@redhat.com
 */
@Slf4j
public class SampleDbConnectionManager {

    private static LocalPortForward localPortForward = null;
    private static String dbUrl;
    private static Connection dbConnection = null;
    private static SampleDbConnectionManager instance;

    public static SampleDbConnectionManager getInstance() {
        if (instance == null) {
            instance = new SampleDbConnectionManager();
        }
        return instance;
    }

    private SampleDbConnectionManager() {
    }

    public static Connection getConnection() {
        final Properties props = new Properties();
        props.setProperty("user", "sampledb");
        if (localPortForward == null || !localPortForward.isAlive()) {
            localPortForward = TestUtils.createLocalPortForward("syndesis-db", 5432, 5432);
        }
        try {
            if (dbConnection == null || dbConnection.isClosed()) {
                try {
                    dbUrl = String.format("jdbc:postgresql://%s:%s/sampledb", localPortForward.getLocalAddress().getLoopbackAddress().getHostName(), localPortForward.getLocalPort());
                } catch (IllegalStateException ex) {
                    dbUrl = String.format("jdbc:postgresql://%s:%s/sampledb", "127.0.0.1", 5432);
                }
                log.debug("DB endpoint URL: " + dbUrl);
                dbConnection = DriverManager.getConnection(dbUrl, props);
            }
        } catch (SQLException ex) {
            log.error("Error: " + ex);
        }

        return dbConnection;
    }

    public static void closeConnection() {
        TestUtils.terminateLocalPortForward(localPortForward);
        try {
            if (dbConnection == null) {
                return;
            }
            if (!dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException ex) {
            log.error("Error: " + ex);
        }
    }
}
