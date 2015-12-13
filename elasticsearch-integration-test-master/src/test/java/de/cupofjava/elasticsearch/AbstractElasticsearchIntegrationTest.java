package de.cupofjava.elasticsearch;

import org.elasticsearch.client.Client;
import org.junit.After;
import org.junit.Before;

/**
 * This is a helper class the starts an embedded elasticsearch server
 * for each test.
 *
 * @author Felix Müller
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
