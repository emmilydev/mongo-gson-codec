package me.ialext.gsoncodec;

import com.google.gson.Gson;
import org.bson.BsonDocument;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class GsonCodecProvider implements CodecProvider {

  private final Gson gson;
  private final Codec<BsonDocument> bsonDocumentCodec;

  public GsonCodecProvider(
      Gson gson,
      Codec<BsonDocument> bsonDocumentCodec
  ) {
    this.gson = gson;
    this.bsonDocumentCodec = bsonDocumentCodec;
  }
  @Override public <T> Codec<T> get(
      Class<T> clazz,
      CodecRegistry registry
  ) {
    if (bsonDocumentCodec == null) {
      return new GsonCodec<>(
          gson,
          clazz
      );
    }

    return new GsonCodec<>(
        gson,
        bsonDocumentCodec,
        clazz
    );
  }
}
