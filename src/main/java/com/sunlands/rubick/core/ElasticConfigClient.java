package com.sunlands.rubick.core;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by baifan on 2017/11/10.
 */
@Service
public class ElasticConfigClient {

    private Client client;

    private static Logger logger = LoggerFactory.getLogger(ElasticConfigClient.class);

    /**cluster name**/
    @Value("${cluster.name}")
    private String clusterName;

    /**cluster master data ip**/
    @Value("${cluster.ip}")
    private  String clusterIp;

    /**cluster master data transport,not http port**/
    @Value("${cluster.transport}")
    private  Integer clusterPort;


    /**
     * Elasticsearch client init
     */
    @PostConstruct
    private void init() {
        synchronized(ElasticConfigClient.class){
            if(client==null){
                /**集群连接超时设置**/
                Settings settings = Settings
                        .builder()
                        .put("client.transport.ping_timeout", "30s")
                        .put("cluster.name",clusterName)
                        .build();
                try {
                    client = new PreBuiltTransportClient(settings)
                            .addTransportAddress(
                                    new TransportAddress(
                                            InetAddress.getByName(clusterIp)
                                            ,clusterPort));
                } catch (UnknownHostException e) {
                    logger.error("ElasticConfigPool UnknownHostException.",e);
                }
            }
        }
    }

    /**
     * Get the Elasticsearch client instance
     * @return Elasticsearch client
     * */
    public Client getClient(){
        if(client==null){
            init();
        }
        return client;
    }
}
