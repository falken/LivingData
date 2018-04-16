package com.lightstream.demo;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppTest
{
    private static Cluster cluster;
    private static Session session;

    @BeforeClass
    public static void initializeCluster(){
        cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .build();
        session = cluster.connect();
    }

    @AfterClass
    public static void cleanupCluster(){
        if (cluster != null) cluster.close();
    }

    @Test
    public void test1(){
        ResultSet rs = session.execute("select release_version from system.local");
        Row row = rs.one();
        System.out.println(row.getString("release_version"));
    }

}
