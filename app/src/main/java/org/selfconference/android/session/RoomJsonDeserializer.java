package org.selfconference.android.session;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public final class RoomJsonDeserializer implements JsonDeserializer<Room> {
  @Override
  public Room deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();

    if (hasRoomProperties(jsonObject)) {
      int id = jsonObject.get("id").getAsInt();
      String name = jsonObject.get("name").getAsString();

      return Room.create(id, name);
    }

    return Room.emptyRoom();
  }

  private static boolean hasRoomProperties(JsonObject jsonObject) {
    return jsonObject.get("id") != null && jsonObject.get("name") != null;
  }
}
