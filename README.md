## mongo-gson-codec

-------
This is a lightweight Mongo codec that uses Gson for serialization.

### Usage:
You just need to add the GsonCodecProvider to your CodecRegistry!
````java
public class Example {
  
  public void init() {
    CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
        CodecRegistries.fromCodecs(
            MongoClientSettings.getDefaultCodecRegistry() // or your codec registry
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
        "example"
    ).withCodecRegistry(codecRegistry);
    MongoCollection<Pojo> pojoMongoCollection = mongoDatabase.getCollection(
        "pojos",
        Pojo.class
    );
    // ...
  }
}
````