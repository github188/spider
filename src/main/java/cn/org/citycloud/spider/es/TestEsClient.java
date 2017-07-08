package cn.org.citycloud.spider.es;

import cn.org.citycloud.spider.entry.News;
import cn.org.citycloud.spider.util.JsonUtils;

public class TestEsClient {

        public static void main(String[] args) {
            EsService esService = EsService.getInstance();
            //String result = esService.get("iusofts","test","1");
            //System.out.println(result);
            //esService.createIndex("iusofts");
            //esService.addMapping("iusofts","fish");
            News news = new News();
            news.setHost("127.0.0.1");
            news.setUrl("http://test.com/01");
            news.setTitle("南京出60方缸");
            news.setContent("一口价200,地址：中山北路鼓楼");
            news.setPublishTime("2017-05-17 18:02:23");
            news.setCrawlerTime("2017-05-12 12:02:23");
            esService.createDoc("iusofts","fish", JsonUtils.obj2json(news));
            //esService.deleteDoc("iusofts","fish","AVwVzJUllVh52UK78o0X");
            esService.getInstance().close();
        }

    }