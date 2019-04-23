package org.tyaa.android.portalandroidloader.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.tyaa.android.portalandroidloader.model.Author;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AuthorsParser {

    private Gson mGson;

    public AuthorsParser() {
        mGson = (new GsonBuilder())
                .setDateFormat("dd.MM.yyyy")
                .create();
    }

    public List<Author> parseAuthors(String _jsonString)
            throws Exception {

        JsonParser parser = new JsonParser();
        JsonObject jsonObject =
                parser.parse(_jsonString).getAsJsonObject();
        //
        Type authorListType =
                new TypeToken<ArrayList<Author>>(){}.getType();
        //
        if (!jsonObject.get("status").equals("error")){
            return (List<Author>)mGson.fromJson(
                    jsonObject.get("data").toString()
                    , authorListType
            );
        } else {
            String errorString =
                    "Server error"
                            + ((jsonObject.has("message"))
                            ? (": " + jsonObject.get("message"))
                            : "");
            throw new Exception(errorString);
        }
    }
}
