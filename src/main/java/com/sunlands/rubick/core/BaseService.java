package com.sunlands.rubick.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunlands.rubick.common.Index;
import com.sunlands.rubick.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by baifan on 2017/11/10.
 */
@Service
public class BaseService {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Value("${cluster.prefix}")
    private String clusterPrefix;

    @Value("${cluster.cat.indices}")
    private String catIndices;

    @Autowired
    private ElasticConfigClient elasticConfigClient;

    private JSONArray fieldList = new JSONArray();

    /**
     * Get cluster total indexs name
     */
    public List<Index> getClusterIndex(){
        List<Index> list = new ArrayList<Index>();
        if(StringUtils.isBlank(catIndices)){
            return list;
        }
        try {
            String result = new HttpConnectUtil().getUrlReturnData(catIndices);
            if(StringUtils.isBlank(result)){
                return list;
            }
            list = JSONArray.parseArray(result,Index.class);
        } catch (Exception e) {
            logger.error("GetClusterIndex Error",e);
        }
        return list;
    }

    /**
     * Get the index total fields name
     * @param index The index which will be query,not exist return NULL.
     * @return list The field String list.
     * */
    public JSONArray getIndexField(String index){
        fieldList.clear();
        if(StringUtils.isBlank(index)){
            return fieldList;
        }
        try {
            String result = new HttpConnectUtil().getUrlReturnData(clusterPrefix+index);
            if(StringUtils.isBlank(result)){
                return fieldList;
            }
            JSONObject json = JSONObject.parseObject(result);
            if(!json.containsKey(index)){
                return fieldList;
            }
            JSONObject jsonMappings = JSONObject.parseObject(json.get(index).toString());
            if(!jsonMappings.containsKey("mappings")){
                return fieldList;
            }
            JSONObject jsonIndex = JSONObject.parseObject(jsonMappings.get("mappings").toString());
            if(jsonIndex.isEmpty()){
                return fieldList;
            }
            for(Map.Entry<String,Object> typeMap:jsonIndex.entrySet()){
                JSONObject jsonProperties = JSONObject.parseObject(jsonIndex.get(typeMap.getKey()).toString());
                if(!jsonProperties.containsKey("properties")){
                    return fieldList;
                }
                JSONObject jsonField = JSONObject.parseObject(jsonProperties.get("properties").toString());
                if(jsonField.isEmpty()){
                    return fieldList;
                }
                for(Map.Entry<String,Object> map:jsonField.entrySet()){
                    if(StringUtils.contains(map.getValue().toString()
                            ,"properties")){
                        JSONObject jsonIterator = JSONObject.parseObject(map.getValue().toString());
                        getTotalField(jsonIterator,typeMap.getKey()+"."+map.getKey(),0);
                    }else {
                        fieldList.add(typeMap.getKey()+"."+map.getKey());
                    }
                }
            }
            return fieldList;
        } catch (Exception e) {
            logger.error("GetIndexField Error",e);
            return fieldList;
        }
    }

    private void getTotalField(JSONObject json,
                                    String prefix,Integer count){
        if(!json.containsKey("properties")){
            return ;
        }
        if(count>10){
            return ;
        }
        count++;
        JSONObject jsonField = JSONObject.parseObject(json.get("properties").toString());
        for(Map.Entry<String,Object> map:jsonField.entrySet()){
            if(StringUtils.contains(map.getValue().toString()
                    ,"properties")){
                JSONObject jsonIterator = JSONObject.parseObject(map.getValue().toString());
                this.getTotalField(jsonIterator,prefix+"."+map.getKey(),count);
            }else{
                fieldList.add(prefix+"."+map.getKey());
            }
        }
    }
}
