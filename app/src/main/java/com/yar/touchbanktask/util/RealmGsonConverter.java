package com.yar.touchbanktask.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.yar.touchbanktask.entity.realm.RealmString;

import java.lang.reflect.Type;

import io.realm.RealmList;


public class RealmGsonConverter implements JsonSerializer<RealmList<RealmString>>, JsonDeserializer<RealmList<RealmString>> {

    @Override
    public JsonElement serialize(RealmList<RealmString> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray ja = new JsonArray();
        for (RealmString realmString : src) {
            ja.add(context.serialize(realmString.getString()));
        }
        return ja;
    }

    @Override
    public RealmList<RealmString> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        RealmList<RealmString> realmList = new RealmList<>();
        JsonArray ja = json.getAsJsonArray();
        for (JsonElement je : ja) {
            realmList.add(new RealmString(context.deserialize(je, String.class)));
        }
        return realmList;
    }
}
