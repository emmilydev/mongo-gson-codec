package me.ialext.gsoncodec;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.junit.jupiter.api.Test;

public class ElaboratedPOJOTest {

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
    MongoCollection<ElaboratedPojo> elaboratedPojoMongoCollection = mongoDatabase.getCollection(
        "elaboratedPojos",
        ElaboratedPojo.class
    );
    elaboratedPojoMongoCollection.insertOne(
        new ElaboratedPojo(
            "si",
            12919391,
            true
        )
    );
  }

  private static class ElaboratedPojo {

    @SerializedName("_id") private final String id;
    @SerializedName("squared") private final long squaredDistance;
    @SerializedName("doesWorks") private final boolean works;

    public ElaboratedPojo(
        String id,
        long squaredDistance,
        boolean works
    ) {
      this.id = id;
      this.squaredDistance = squaredDistance;
      this.works = works;
    }

    public String getId() {
      return id;
    }

    public long getSquaredDistance() {
      return squaredDistance;
    }

    public boolean isWorks() {
      return works;
    }
  }
}
