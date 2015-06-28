package com.device.storeplus.storeplus;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.device.storeplus.storeplus.models.Bag;
import com.device.storeplus.storeplus.models.SingleItem;
import com.device.storeplus.storeplus.models.User;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Tomer on 25-Jun-15.
 */
public class ServerApi {

    public static User getUserDetails(long uid) {
        String jsonResult = Json.getUserDetails(uid);

        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map userDetails = parser.parseJson(jsonResult.toString());

        // Fill the user details
        User user = new User();
        user.id = (String)userDetails.get("uid");
        user.name = (String)userDetails.get("name");
        user.imageUrl= (String)userDetails.get("imageURL");

        return user;
    }

    public static Bag getUserBag(long uid) {
        String jsonResult = Json.getUserBag(uid);

        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map userBag = parser.parseJson(jsonResult.toString());
        Map userMap = (Map)userBag.get("user");

        Map bagMap = (Map)userBag.get("bag");
        Bag bag = new Bag();

        // Fill the user details
        bag.user.id = (String)userMap.get("uid");
        bag.user.name = (String)userMap.get("name");
        bag.user.imageUrl= (String)userMap.get("imageURL");

        // Fill the bag details
        ArrayList<Map> bagItems = (ArrayList<Map>)bagMap.get("items");
        for (Map currItem : bagItems) {
            String id = (String) currItem.get("id");
            String title = (String) currItem.get("title");
            Double price = Double.parseDouble((String) currItem.get("price"));
            String currency = currItem.get("currency") == null ? "$" : (String) currItem.get("currency");
            String imageUrl = (String) currItem.get("imageURL");
            SingleItem singleItem = new SingleItem(id, title, price, imageUrl, currency);

            // Add the size
            Map itemSize = (Map) currItem.get("size");
            String sizeName = (String) itemSize.get("name");
            String sizeTitle = (String) itemSize.get("title");
            String sizeStatus = (String) itemSize.get("status");
            singleItem.itemSizes.add(new SingleItem.ItemSize(sizeName, sizeTitle, sizeStatus));

            bag.items.add(singleItem);
        }

        return bag;
    }

    public static SingleItem getItemDetails(long iid, String lang, String currency)  {
        String jsonResult = Json.getItemDetails(iid, lang, currency);

        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map currItem = parser.parseJson(jsonResult.toString());

        // Fill the singleItem details
        String id = (String) currItem.get("id");
        String title = (String) currItem.get("title");
        Double price = Double.parseDouble((String) currItem.get("price"));
        String retCurrency = currItem.get("currency") == null ? "$" : (String) currItem.get("currency");
        String imageUrl = (String) currItem.get("imageURL");
        SingleItem singleItem = new SingleItem(id, title, price, imageUrl, retCurrency);

        ArrayList<Map> itemSizes = (ArrayList<Map>) currItem.get("sizes");
        // Add the sizes
        for (Map currSize : itemSizes) {
            String sizeName = (String) currSize.get("name");
            String sizeTitle = (String) currSize.get("title");
            String inbagsStock = (String) currItem.get("inbags-stock");
            String availbleStock = (String) currItem.get("availble-stock");

            singleItem.itemSizes.add(new SingleItem.ItemSize(sizeName, sizeTitle, inbagsStock, availbleStock));
        }

        return singleItem;
    }

    public static Boolean addItemToBag(long iid, long uid, String size) throws Exception {
        String jsonResult = Json.addItemToBag(iid, uid, size);
        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map response = parser.parseJson(jsonResult.toString());

        if (!((String)response.get("status")).equals("success")) {
            if ((String)response.get("error message") != null)
                throw new Exception ((String)response.get("error message"));
            else
                throw new Exception ("error");
        }

        return true;
    }

    public static Boolean removeItemFromBag(long iid, long uid, String size) throws Exception {
        String jsonResult = Json.removeItemFromBag(iid, uid, size);
        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map response = parser.parseJson(jsonResult.toString());

        if (!((String)response.get("status")).equals("success")) {
            if ((String)response.get("error message") != null)
                throw new Exception ((String)response.get("error message"));
            else
                throw new Exception ("error");
        }

        return true;
    }

    public static Boolean cleanUserBag(long uid) throws Exception {
        String jsonResult = Json.cleanUserBag(uid);
        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map response = parser.parseJson(jsonResult.toString());

        if (!((String)response.get("status")).equals("success")) {
            if ((String)response.get("error message") != null)
                throw new Exception ((String)response.get("error message"));
            else
                throw new Exception ("error");
        }

        return true;
    }

    public static class Json {

        public static String getItemDetails(long iid, String lang, String currency) {

            String inputJsonString = "{\n" +
                    "    \"id\": 2,\n" +
                    "    \"name\": \"Micro-patterned oxford shirt\",\n" +
                    "    \"price\": 25.99,\n" +
                    "    \"imageURL\": \"http://static.zara.net/photos//2015/V/0/2/p/4036/260/403/2/w/1024/4036260403_1_1_1.jpg\",\n" +
                    "    \"sizes\": [\n" +
                    "         {\n" +
                    "               \"name\": \"s\",\n" +
                    "               \"title\": \"S\",\n" +
                    "               \"inbags-stock\": \"0\",\n" +
                    "               \"availble-stock\": \"4\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "               \"name\": \"m\",\n" +
                    "               \"title\": \"M\",\n" +
                    "               \"inbags-stock\": \"1\",\n" +
                    "               \"availble-stock\": \"2\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "               \"name\": \"l\",\n" +
                    "               \"title\": \"L\",\n" +
                    "               \"inbags-stock\": \"2\",\n" +
                    "               \"availble-stock\": \"5\"\n" +
                    "         }\n" +
                    "    ]\n" +
                    "}";


            return inputJsonString;
        }

        public static String getUserBag(long uid) {
            return "{\n" +
                    "     \"user\": \n" +
                    "     {\n" +
                    "          \"uid\": \"14932590\",\n" +
                    "          \"name\": \"Tomer Shohet\",\n" +
                    "          \"imageURL\": \"https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xtf1/v/t1.0-1/p160x160/10561763_10152659558942682_4962942110060764144_n.jpg?oh=fc3a189547e1d87d0e22ac1c13e975fb&oe=56217C4E&__gda__=1445122776_a533be467206cc4ec8cac28d84f98260\"      \n" +
                    "     },\n" +
                    "     \"bag\": \n" +
                    "     {\n" +
                    "          \"id\": \"123\",\n" +
                    "          \"numOfItem\": \"2\",\n" +
                    "          \"items\": \n" +
                    "           [\n" +
                    "             {\n" +
                    "    \t             \"id\": 2,\n" +
                    "\t             \"title\": \"Micro-patterned oxford shirt\",\n" +
                    "\t             \"price\": 25.99,\n" +
                    "             \"imageURL\":    \"http://static.zara.net/photos//2015/V/0/2/p/4036/260/403/2/w/1024/4036260403_1_1_1.jpg\",\n" +
                    "             \"size\": \n" +
                    "                  {\n" +
                    "                        \"name\": \"m\",\n" +
                    "                        \"title\": \"M\",\n" +
                    "                        \"status\": \"in-bag\"\n" +
                    "                  }\n" +
                    "             },\n" +
                    "             {\n" +
                    "    \t             \"id\": 43,\n" +
                    "\t             \"title\": \"Faux leather bomber jacket\",\n" +
                    "             \"price\": 39.99,\n" +
                    "             \"imageURL\":\n" +
                    "\"http://static.zara.net/photos//2015/V/0/2/p/0706/405/700/2/w/1024/0706405700_1_1_1.jpg\",\n" +
                    "             \"size\": \n" +
                    "                  {\n" +
                    "                        \"name\": \"m\",\n" +
                    "                        \"title\": \"M\",\n" +
                    "                        \"status\": \"in-bag\"\n" +
                    "                  }\n" +
                    "             }\n" +
                    "          ]     \n" +
                    "     }\n" +
                    "}";
        }

        public static String addItemToBag(long iid, long uid, String size) {
            if (uid != 666)
                return "{\n" +
                        "\t    \"status\": \"success\",\n" +
                        "\t    \"items in bag\": 4\n" +
                        "}";

            return "{\n" +
                    "\t    \"status\": \"error\",\n" +
                    "    \"items in bag\": 10\n" +
                    "\t    \"error message\": \"Too many items in bag. Please remove some\"\n" +
                    "}";
        }

        public static String removeItemFromBag(long iid, long uid, String size) {
            if (uid != 666)
                return "{\n" +
                        "\t    \"status\": \"success\",\n" +
                        "\t    \"items in bag\": 4\n" +
                        "}";

            return "{\n" +
                    "\t    \"status\": \"error\",\n" +
                    "    \"items in bag\": 10\n" +
                    "\t    \"error message\": \"item does not exist in bag\"\n" +
                    "}";
        }

        public static String cleanUserBag(long uid) {
            if (uid != 666)
                return "{\n" +
                        "\t    \"status\": \"success\",\n" +
                        "}";

            return "{\n" +
                    "\t    \"status\": \"error\",\n" +
                    "\t    \"error message\": \"user does not exist\"\n" +
                    "}";
        }

        public static String getUserDetails(long uid) {
            return "{\n" +
                    "     \"uid\": \"14932590\",\n" +
                    "     \"name\": \"Tomer Shohet\",\n" +
                    "     \"imageURL\": \"https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xtf1/v/t1.0-1/p160x160/10561763_10152659558942682_4962942110060764144_n.jpg?oh=fc3a189547e1d87d0e22ac1c13e975fb&oe=56217C4E&__gda__=1445122776_a533be467206cc4ec8cac28d84f98260\"      \n" +
                    "}";
        }
    }
}

