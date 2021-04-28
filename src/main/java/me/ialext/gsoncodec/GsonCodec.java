package me.ialext.gsoncodec;

import com.google.gson.Gson;
import com.mongodb.MongoClientSettings;
import org.bson.BsonDocument;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.BsonDocumentCodec;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

class GsonCodec<T> implements Codec<T> {

  private final Gson gson;
  private final Codec<BsonDocument> bsonDocumentCodec;
  private final Class<T> clazz;

  public GsonCodec(
      Gson gson,
      Codec<BsonDocument> bsonDocumentCodec,
      Class<T> clazz
  ) {
    this.gson = gson;
    this.bsonDocumentCodec = bsonDocumentCodec;
    this.clazz = clazz;
  }

  public GsonCodec(
      Gson gson,
      Class<T> clazz
  ) {
    this(
        gson,
        new BsonDocumentCodec(
            MongoClientSettings.getDefaultCodecRegistry()
        ),
        clazz
    );
  }

  @Override public T decode(
      BsonReader reader,
      DecoderContext decoderContext
  ) {
    BsonDocument bson = bsonDocumentCodec.decode(
        reader,
        decoderContext
    );

    return gson.fromJson(
        bson.toJson(),
        clazz
    );
  }

  @Override public void encode(
      BsonWriter writer,
      T value,
      EncoderContext encoderContext
  ) {
    String json = gson.toJson(value);
    bsonDocumentCodec.encode(
        writer,
        BsonDocument.parse(json),
        encoderContext
    );
  }

  @Override public Class<T> getEncoderClass() {
    return clazz;
  }
}
