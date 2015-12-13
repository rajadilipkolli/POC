package com.example.poc.elasticsearch;

import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Before;

import com.example.poc.elasticsearch.EmbeddedElasticsearchServer;

/**
 * This is a helper class the starts an embedded elasticsearch server
 * for each test.
 *
 * @author RajaDileep
 */
public abstract class AbstractElasticsearchIntegrationTest {

    private EmbeddedElasticsearchServer embeddedElasticsearchServer;

    @Before
    public void startEmbeddedElasticsearchServer() {
        embeddedElasticsearchServer = new EmbeddedElasticsearchServer();
    }

    @After
    public void shutdownEmbeddedElasticsearchServer() {
        embeddedElasticsearchServer.shutdown();
    }

    /**
     * By using this method you can access the embedded server.
     */
    protected Client getClient() {
        return embeddedElasticsearchServer.getClient();
    }
}
