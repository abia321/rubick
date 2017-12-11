package com.sunlands.rubick.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by baifan on 2017/11/27.
 */
@Service
public class SearchHandler {

    private Logger logger = LoggerFactory.getLogger(SearchHandler.class);

    @Autowired
    private ElasticConfigClient elasticConfigClient;

    private Set<String> set = new HashSet<>();
    private JSONArray array = new JSONArray();

    /**
     *
     * */
    public JSONObject searchBuilder(QueryBuilder queryBuilder,
                              String indexname){
        JSONObject result = new JSONObject();
        SearchRequestBuilder searchRequestBuilder=elasticConfigClient.getClient()
                .prepareSearch(indexname)
                .setQuery(queryBuilder)
                .setSize(1000);
        SearchResponse searchResponseAllHits=searchRequestBuilder.execute().actionGet();
        SearchHits hits = searchResponseAllHits.getHits();
        SearchHit[] searchHists = hits.getHits();
        if(ArrayUtils.isEmpty(searchHists)){
            return result;
        }
        for(SearchHit hit:searchHists){
            JSONObject json = new JSONObject();
            for(JSONObject.Entry<String,Object> entry:hit.getSourceAsMap().entrySet()){
                JSONObject data = new JSONObject();
                getMapKey(JSONObject.toJSON(entry.getValue())
                        .toString(),entry.getKey(),data);
            }
            array.add(json);
        }
        result.put("column",set);
        result.put("data",array);
        return result;
    }

    private void getMapKey(String str,String prefix,JSONObject data){
        if(StringUtils.contains(str,"{")){
            JSONObject json= JSONObject.parseObject(str);
            for(JSONObject.Entry<String,Object> entry:json.entrySet()){
                this.getMapKey(JSONObject.toJSON(entry.getValue()).toString(),
                        prefix+"."+entry.getKey(),data);
            }
        }else {
            set.add(prefix);
            data.put(prefix,str);
            array.add(data);
        }
    }
}
