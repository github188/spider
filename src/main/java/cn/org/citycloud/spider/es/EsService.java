/*
 * Copyright (C) 2009-2017 Ivan All rights reserved
 * Author: Ivan Shen
 * Date: 2017/5/17
 * Description:EsUtil.java
 */
package cn.org.citycloud.spider.es;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.min.InternalMin;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * EsService
 *
 * @author Ivan Shen
 */
public class EsService {
    
    private static EsService esService;
    
    private TransportClient client;
    
    public EsService(){
        try {

            //设置集群名称
            Settings settings = Settings.builder().put("cluster.name", "iusofts").build();
            //创建client
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.100.103"), 9300));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static synchronized EsService getInstance(){
        if(esService==null){
            esService = new EsService();
            System.err.println("初始化EsService");
        }
        return esService;
    }
    
    public void close(){
        client.close();
    }

    /**
     * 创建一个索引
     * @param indexName 索引名
     */
    public void createIndex(String indexName) {
        try {
            CreateIndexResponse indexResponse = this.client
                    .admin()
                    .indices()
                    .prepareCreate(indexName)
                    .get();

            System.out.println(indexResponse.isAcknowledged()); // true表示创建成功
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给索引增加mapping。
     * @param index 索引名
     * @param type mapping所对应的type
     */
    public void addMapping(String index, String type) {
        try {
            // 使用XContentBuilder创建Mapping
            XContentBuilder builder =
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("properties")
                            .startObject()
                            
                            .field("host")
                            .startObject()
                            .field("index", "not_analyzed")
                            .field("type", "string")
                            .endObject()

                            .field("url")
                            .startObject()
                            .field("index", "not_analyzed")
                            .field("type", "string")
                            .endObject()

                            .field("title")
                            .startObject()
                            .field("index", "analyzed")
                            .field("type", "string")
                            .endObject()

                            .field("content")
                            .startObject()
                            .field("index", "analyzed")
                            .field("type", "string")
                            .endObject()
                            
                            .field("publishTime")
                            .startObject()
                            .field("index", "not_analyzed")
                            .field("type", "date")
                            .field("format", "yyyy-MM-dd HH:mm:ss")
                            .endObject()

                            .field("crawlerTime")
                            .startObject()
                            .field("index", "not_analyzed")
                            .field("type", "date")
                            .field("format", "yyyy-MM-dd HH:mm:ss")
                            .endObject()
                            
                            .endObject()
                            .endObject();
            System.out.println(builder.string());
            PutMappingRequest mappingRequest = Requests.putMappingRequest(index).source(builder).type(type);
            this.client.admin().indices().putMapping(mappingRequest).actionGet();
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     * @param index 要删除的索引名
     */
    public void deleteIndex(String index) {
        DeleteIndexResponse deleteIndexResponse =
                this.client
                        .admin()
                        .indices()
                        .prepareDelete(index)
                        .get();
        System.out.println(deleteIndexResponse.isAcknowledged()); // true表示成功
    }

    /**
     * 创建一个文档
     * @param index index
     * @param type type
     */
    public void createDoc(String index, String type,String data) {

        try {
            IndexResponse indexResponse = this.client
                    .prepareIndex()
                    .setIndex(index)
                    .setType(type)
                    // .setId(id) // 如果没有设置id，则ES会自动生成一个id
                    .setSource(data, XContentType.JSON)
                    .get();
            System.out.println(indexResponse.status());
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建一个文档
     * @param index index
     * @param type type
     */
    public void createDoc(String index, String type) {

        try {
            // 使用XContentBuilder创建一个doc source
            XContentBuilder builder =
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("name", "zhangsan")
                            .field("age", "lisi")
                            .endObject();

            IndexResponse indexResponse = this.client
                    .prepareIndex()
                    .setIndex(index)
                    .setType(type)
                    // .setId(id) // 如果没有设置id，则ES会自动生成一个id
                    .setSource(builder.string(), XContentType.JSON)
                    .get();
            System.out.println(indexResponse.status());
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新文档
     * @param index
     * @param type
     * @param id
     */
    public void updateDoc(String index, String type, String id) {
        try {
            XContentBuilder builder =
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("name", "lisi")
                            .field("age", 12)
                            .endObject();

            UpdateResponse updateResponse =
                    this.client
                            .prepareUpdate()
                            .setIndex(index)
                            .setType(type)
                            .setId(id)
                            .setDoc(builder.string(), XContentType.JSON)
                            .get();
            System.out.println(updateResponse.status().getStatus()); // true表示成功
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据ID查询一条数据记录。
     * @param id 要查询数据的ID。
     * @return 返回查询出来的记录对象的json字符串。
     */
    public String get(String index, String type, String id) {
        GetResponse getResponse = this.client
                .prepareGet()   // 准备进行get操作，此时还有真正地执行get操作。（与直接get的区别）
                .setIndex(index)  // 要查询的
                .setType(type)
                .setId(id)
                .get();
        return getResponse.getSourceAsString();
    }

    /**
     * 使用filter方式查询数据。
     * @param index 数据所在的索引名
     * @param type 数据所在的type
     * @return
     */
    public List<String> queryByFilter(String index, String type) {

        // 查询名为zhangsan的数据
        QueryBuilder queryBuilder = QueryBuilders.termQuery("name", "zhangsan");
        SearchResponse searchResponse =
                this.client
                        .prepareSearch()
                        .setIndices(index)
                        .setTypes(type)
                        .setPostFilter(queryBuilder)
                        .get();

        List<String> docList = new ArrayList<String>();
        SearchHits searchHits = searchResponse.getHits();
        for (SearchHit hit : searchHits) {
            docList.add(hit.getSourceAsString());
        }
        return docList;
    }

    /**
     * 删除一条数据
     * @param index
     * @param type
     * @param id
     */
    public void deleteDoc(String index, String type, String id) {
        DeleteResponse deleteResponse  = this.client
                .prepareDelete()
                .setIndex(index)
                .setType(type)
                .setId(id)
                .get();
        System.out.println(deleteResponse.status().getStatus()); // true表示成功
    }
    
    /**
     * 使用min聚合查询某个字段上最小的值。
     * @param index
     * @param type
     */
    public void min(String index, String type) {
        SearchResponse response = this.client
                .prepareSearch(index)
                .addAggregation(AggregationBuilders.min("min").field("age"))
                .get();

        InternalMin min = response.getAggregations().get("min");
        System.out.println(min.getValue());
    }
    
}
