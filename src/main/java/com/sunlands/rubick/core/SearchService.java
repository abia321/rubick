package com.sunlands.rubick.core;

import com.alibaba.fastjson.JSONArray;
import com.sunlands.rubick.common.Filter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baifan on 2017/11/27.
 */
@Service
public class SearchService {

    private static Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private SearchHandler searchHandler;

    /**
     *
     * */
    public JSONArray search(String queryFilter,String index){
        logger.info(queryFilter);
        List<Filter> filterList = JSONArray.parseArray(queryFilter,Filter.class);
        if(CollectionUtils.isEmpty(filterList)){
            return null;
        }
        BoolQueryBuilder boolQuery = new BoolQueryBuilder();
        for(Filter filter:filterList){
            switch (filter.getOp()){
                case "term":
                    QueryBuilder termQuery = QueryBuilders.termQuery(filter.getField(),
                            filter.getValue().trim());
                    logicFormer(boolQuery,termQuery,filter.getLogic());
                    break;
                case "match":
                    QueryBuilder matchQuery = QueryBuilders.matchQuery(filter.getField(),
                            filter.getValue().trim());
                    logicFormer(boolQuery,matchQuery,filter.getLogic());
                    break;
                case "prefix":
                    QueryBuilder prefixQuery = QueryBuilders.prefixQuery(filter.getField(),
                            filter.getValue().trim());
                    logicFormer(boolQuery,prefixQuery,filter.getLogic());
                    break;
                case "wildcard":
                    QueryBuilder wildcardQuery = QueryBuilders.wildcardQuery(filter.getField(),
                            filter.getValue().trim());
                    logicFormer(boolQuery,wildcardQuery,filter.getLogic());
                    break;
                case "range":
                    Map<String,String> map = rangeDataFormer(filter.getValue().trim());
                    RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery(filter.getField());
                    for(Map.Entry<String,String> entry:map.entrySet()){
                        switch (entry.getKey()) {
                            case "gt":
                                rangeQuery.gt(entry.getValue());
                                break;
                            case "gte":
                                rangeQuery.gte(entry.getValue());
                                break;
                            case "lt":
                                rangeQuery.lt(entry.getValue());
                                break;
                            case "lte":
                                rangeQuery.lte(entry.getValue());
                                break;
                        }
                    }
                    logicFormer(boolQuery,rangeQuery,filter.getLogic());
                    break;
            }
            logger.info(boolQuery.toString());
        }
        //search
        searchHandler.searchBuilder(boolQuery,index,index);
        return null;
    }

    private Map<String,String> rangeDataFormer(String value){
        Map<String,String> map = new HashMap<String,String>();
        if(StringUtils.isBlank(value)){
            return map;
        }
        String[] str= StringUtils.split(value,"&");
        if(ArrayUtils.isEmpty(str)){
            return map;
        }
        for(int i=0;i<str.length;i++){
            String[] rangeValue= StringUtils.split(value,":");
            if(rangeValue.length !=2){
                continue;
            }
            switch (rangeValue[0]) {
                case "gt":
                    map.put("gt",rangeValue[1]);
                    break;
                case "gte":
                    if(map.containsKey("gt")){
                        map.remove("gt");
                    }
                    map.put("gte",rangeValue[1]);
                    break;
                case "lt":
                    map.put("lt",rangeValue[1]);
                    break;
                case "lte":
                    if(map.containsKey("lt")){
                        map.remove("lt");
                    }
                    map.put("lte",rangeValue[1]);
                    break;
            }
        }
        return map;
    }

    private BoolQueryBuilder logicFormer(BoolQueryBuilder boolQuery,
                                         QueryBuilder queryBuilder,String logic){
        if(StringUtils.isBlank(logic)){
            return boolQuery;
        }
        switch (logic) {
            case "must":
                boolQuery.must(queryBuilder);
                break;
            case "must_not":
                boolQuery.mustNot(queryBuilder);
                break;
            case "should":
                boolQuery.should(queryBuilder);
                break;
        }
        return boolQuery;
    }
}
