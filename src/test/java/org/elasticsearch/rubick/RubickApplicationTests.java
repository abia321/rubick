package org.elasticsearch.rubick;

import org.elasticsearch.rubick.core.ElasticConfigClient;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RubickApplicationTests {

	@Test
	public void contextLoads() {

	}

	@Autowired
	ElasticConfigClient elasticConfigClient;

	@Test
	public void test() throws IOException, ExecutionException, InterruptedException {
		XContentBuilder jsonBuild = XContentFactory.jsonBuilder();
		jsonBuild.startObject()
				.field("room_id",123)
				.field("pageRank",123)//暂用pageRank替uid
				.field("room_id_for_query",123)
				.field("room_name", "hahaha")
				.endObject();
		/**upsert,存在则更新，不存在则插入**/
		System.out.println(jsonBuild.string());
		IndexRequest indexRequest =new IndexRequest("test1","test1","123").source(jsonBuild);
		UpdateRequest updateRequest = new UpdateRequest("test1","test1","123").doc(jsonBuild).upsert(indexRequest);
		elasticConfigClient.getClient().update(updateRequest).get();
	}
}
