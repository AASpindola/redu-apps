package services;

import bootstrap.SE;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.App;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturspindola on 05/02/15.
 */
public class ElasticSearchServices {

    public static ElasticSearchServices instance = null;

    public static ElasticSearchServices getInstance() {
        if (instance == null){
            instance = new ElasticSearchServices();
        }
        return instance;
    }

    public static List<App> searchApps(String text) {
        SearchRequestBuilder builder = SE.client.prepareSearch("reduapps")
                .setTypes("app")
                .addSort("views", SortOrder.DESC)
                .addSort("appName", SortOrder.ASC);

        if (text != null && !text.equals("")) {
            builder.setQuery(QueryBuilders.prefixQuery("appName", text));
            //builder.setQuery(QueryBuilders.moreLikeThisQuery("author", "appName", "objective", "synopsis", "description", "area", "level").likeText(text));
        }

        SearchResponse response = builder.execute().actionGet();
        builder.setSize((int) response.getHits().getTotalHits());
        SearchHit[] hits = builder.execute().actionGet().getHits().getHits();

        List<App> returnList = new ArrayList<App>();
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < hits.length; ++i) {
            String sourceString = hits[i].sourceAsString();
            JsonNode jsonsource = null;
            try {
                jsonsource = mapper.readTree(sourceString);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                App app = new App();
                app.buildFromJson(jsonsource);
                returnList.add(app);
            } catch (NullPointerException e) {
                play.Logger.debug("deu erro com esse json: "
                        + jsonsource.toString());
            }
        }

        return returnList;
    }

    public static List<App> searchAppsInRange (String text, int start, int end){
        List<App> allApps = searchApps(text);
        if (start > allApps.size()) {
            return null;
        } else {
            end = end < allApps.size() ? end : allApps.size();
            return allApps.subList(start, end);
        }
    }

    public static void updateViewsCount(String appName, int newViews){
        SE.client.prepareUpdate("reduapps", "app", appName)
                .setScript("ctx._source.views = " + newViews, ScriptService.ScriptType.INLINE)
                .get();
    }
}
