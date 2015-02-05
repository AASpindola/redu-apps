package bootstrap;

import java.util.List;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequestBuilder;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import play.Logger;
import play.Play;

/**
 * Created by arturspindola on 05/02/15.
 */
public class SE {

    public static ElasticsearchOperations esop;

    public static Client client;

//	public static ActorRef indexer;

    private SE() {
    }

    public static void init() {
        if (client == null) {
            String server = Play.application().configuration().getString("elasticsearch.server");

            Settings clientSettings = ImmutableSettings.settingsBuilder().put("cluster.name", "reduapps").put("client.transport.sniff", true).build();
            String[] hostPort = server.split(":");
            Client transportClient = new TransportClient(/*clientSettings*/).addTransportAddress(new InetSocketTransportAddress(hostPort[0], Integer.parseInt(hostPort[1])));

            ClusterAdminClient cac = transportClient.admin().cluster();

            ClusterHealthRequest healthRequest = new ClusterHealthRequestBuilder(cac).request();
            try {
                ClusterHealthResponse chr = cac.health(healthRequest).actionGet();
                Logger.debug("ElasticSearch cluster status: " + chr.getStatus());
                client = transportClient;
            } catch (Exception e) {
                e.printStackTrace();
                Node node = NodeBuilder.nodeBuilder().settings(clientSettings).node();
                Logger.debug("ElasticSearch cluster not found, starting self node");
                client = node.client();
            }
            esop = new ElasticsearchTemplate (client);
        }
    }

    public static void shutdown() {
        if (client != null) {
            client.close();
        }

    }
}
