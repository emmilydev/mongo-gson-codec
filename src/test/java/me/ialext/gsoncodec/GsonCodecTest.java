package me.ialext.gsoncodec;

import com.google.gson.Gson;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.jupiter.api.Test;

public class GsonCodecTest {

  @Test public void test() {
    CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
        CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry()
        ),
        CodecRegistries.fromProviders(
            new GsonCodecProvider(
                new Gson(),
                new BsonDocumentCodec()
            )
        )
    );

    MongoClient mongoClient = MongoClients.create();
    MongoDatabase mongoDatabase = mongoClient.getDatabase(
        "gsonCodec"
    ).withCodecRegistry(codecRegistry);
    MongoCollection<POJO> pojoMongoCollection = mongoDatabase.getCollection(
        "pojos",
        POJO.class
    );

    POJO pojo = new POJO(
        "alex",
        1331
    );
    pojoMongoCollection.insertOne(pojo);
    System.out.print(
        pojoMongoCollection
            .find(
                Filters.eq(
                    "id",
                    "alex"
                )
            )
            .first()
            .getSize() // Ik, it can throw NPE.
    );
  }

  static class POJO {

    private final String id;
    private final int size;

    public POJO(
        String id,
        int size
    ) {
      this.id = id;
      this.size = size;
    }

    public String getId() {
      return id;
    }

    public int getSize() {
      return size;
    }
  }
}
