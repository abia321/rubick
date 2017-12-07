package com.sunlands.rubick.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunlands.rubick.common.Index;
import com.sunlands.rubick.core.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by baifan on 2017/11/10.
 */
@Controller
@RequestMapping("/base")
public class BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private BaseService baseService;

    /**
     *  get cluster total index
     * */
    @RequestMapping("/index")
    public String getClusterIndex(Model model){
        List<Index> list = baseService.getClusterIndex();
        model.addAttribute("indexList",list);
        return "index";
    }

    /**
     *  get the index total fields
     *  @param index the index name to Query
     * */
    @RequestMapping("/field")
    @ResponseBody
    public JSONArray getIndexField(@RequestParam String index){
        JSONArray array =baseService.getIndexField(index);
        return array;
    }

    @RequestMapping("/search")
    @ResponseBody
    public JSONObject search(@RequestParam String queryFilter){
        JSONArray array = new JSONArray();
//        if(StringUtils.isBlank(queryFilter)){
//           return array;
//        }
        for(int i=0;i<10;i++){
            JSONObject json = new JSONObject();
            json.put("column0","baifan"+i);
            json.put("column1",123);
            array.add(json);
        }
        JSONObject result = new JSONObject();
        result.put("total",20);
        result.put("rows",array);
        return result;
    }

    @RequestMapping("/column")
    @ResponseBody
    public JSONArray getColumn(){
        JSONArray array = new JSONArray();
        for(int i=0;i<2;i++){
            JSONObject json = new JSONObject();
            json.put("field","column"+i);
            json.put("title",123+i);
            array.add(json);
        }
        return array;
    }
}
