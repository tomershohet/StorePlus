package com.device.storeplus.storeplus;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Tomer on 25-Jun-15.
 */
public class serverApi {
    public static String getItemDetails(long iid, String lang, String currency) {

        String inputJsonString = "{\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"Micro-patterned oxford shirt\",\n" +
                "    \"price\": 25.99,\n" +
                "    \"imageURL\": \"http://static.zara.net/photos//2015/V/0/2/p/0722/451/800/2/w/1024/0722451800_1_1_1.jpg\",\n" +
                "    \"sizes\": [\n" +
                "         {\n" +
                "               \"name\": \"s\",\n" +
                "               \"title\": \"S\",\n" +
                "               \"status\": \"available\"\n" +
                "         },\n" +
                "         {\n" +
                "               \"name\": \"m\",\n" +
                "               \"title\": \"M\",\n" +
                "               \"status\": \"waiting\"\n" +
                "         },\n" +
                "         {\n" +
                "               \"name\": \"l\",\n" +
                "               \"title\": \"L\",\n" +
                "               \"status\": \"available\"\n" +
                "         }\n" +
                "    ]\n" +
                "}";


        return inputJsonString;
    }
}
