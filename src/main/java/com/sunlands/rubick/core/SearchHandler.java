package com.sunlands.rubick.core;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by baifan on 2017/11/27.
 */
public class SearchHandler {

    private Logger logger = LoggerFactory.getLogger(SearchHandler.class);

    @Autowired
    private ElasticConfigClient elasticConfigClient;

    /**
     *
     * */
    public void searchBuilder(QueryBuilder queryBuilder, String indexname,
                              String type, Integer pageNum, String sort){
        SearchRequestBuilder searchRequestBuilder=elasticConfigClient.getClient()
                .prepareSearch(indexname).setTypes(type)
                .setQuery(queryBuilder)
                .setSize(20)
                .setFrom(pageNum);
        searchRequestBuilder.addSort("follow", SortOrder.DESC);
        SearchResponse searchResponseAllHits=searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponseAllHits.getHits();
        SearchHit[] searchHists = hits.getHits();
        if(ArrayUtils.isEmpty(searchHists)){
            return ;
        }
        Set<String> set = new HashSet<>();
        for(SearchHit hit:searchHists){
            JSONObject json = new JSONObject();
            for(Map.Entry<String,Object> entry:hit.getSourceAsMap().entrySet()){
                set.add(entry.getKey());
                json.put(entry.getKey(),entry.getValue());
            }
        }
    }
}
