package com.device.storeplus.storeplus;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;
import com.device.storeplus.storeplus.models.Bag;
import com.device.storeplus.storeplus.models.SingleItem;
import com.device.storeplus.storeplus.models.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Tomer on 25-Jun-15.
 */
public class ServerApi {

    static final String serverHostName = "192.168.1.15:8080/";

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
        String jsonResult = Json.getUserBag("0", uid);

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

    public static SingleItem getItemDetails(String iid, String lang, String currency)  {
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
            String inbagsStock = (String) currItem.get("inbagsStock");
            String availbleStock = (String) currItem.get("availbleStock");

            singleItem.itemSizes.add(new SingleItem.ItemSize(sizeName, sizeTitle, inbagsStock, availbleStock));
        }

        return singleItem;
    }

    public static Boolean addItemToBag(long iid, long uid, String size) throws Exception {
        String jsonResult = Json.addItemToBag("0", iid, uid, size);
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
        String jsonResult = Json.removeItemFromBag("0", iid, uid, size);
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
        String jsonResult = Json.cleanUserBag("0", uid);
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

    public static ArrayList<SingleItem> getUserPastItems(long uid) {
        String jsonResult = Json.getUserBag("0", uid);

        JsonParserFactory factory = JsonParserFactory.getInstance();
        JSONParser parser = factory.newJsonParser();
        Map serverResult = parser.parseJson(jsonResult.toString());

        // Fill the bag details
        ArrayList<SingleItem> userPastItemsRet = new ArrayList<SingleItem>();
        ArrayList<Map> userPastItems = (ArrayList<Map>)serverResult.get("items");
        for (Map currItem : userPastItems) {
            String id = (String) currItem.get("id");
            String title = (String) currItem.get("title");
            Double price = Double.parseDouble((String) currItem.get("price"));
            String currency = currItem.get("currency") == null ? "$" : (String) currItem.get("currency");
            String imageUrl = (String) currItem.get("imageURL");
            String location = (String) currItem.get("location");
            String retDate = (String)currItem.get("date");

            Date date = null;
            try {
                date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(retDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SingleItem singleItem = new SingleItem(id, title, price, imageUrl, currency, date, location);

            // Add the size
            Map itemSize = (Map) currItem.get("size");
            String sizeName = (String) itemSize.get("name");
            String sizeTitle = (String) itemSize.get("title");
            String sizeStatus = (String) itemSize.get("status");
            singleItem.itemSizes.add(new SingleItem.ItemSize(sizeName, sizeTitle, sizeStatus));

            userPastItemsRet.add(singleItem);
        }

        return userPastItemsRet;
    }

    public static class Json {

        public static String getItemDetails(String iid, String lang, String currency) {

            String params = "store/0/items";
            params += "/" + iid;
            params += lang != null ? "/lang/" + lang : "";
            params += currency  != null ? "/currency /" + currency : "";

            String jsonResult = GET("http://" + ServerApi.serverHostName +  params + "?type=text");

            return jsonResult;
        }

        public static String getUserBag(String sid, long uid) {
            String params = "store/" + sid + "/users" + uid + "/inbag";

            String jsonResult = GET("http://" + ServerApi.serverHostName +  params + "?type=text");
            return jsonResult;

            /*return "{\n" +
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
                    "}";*/
        }

        public static String addItemToBag(String sid, long iid, long uid, String size) {
            String params = "store/" + sid + "/users" + uid + "/inbag";
            params += "/" + iid;

            String jsonResult = POST("http://" + ServerApi.serverHostName +  params + "?type=text");
            return jsonResult;
//
//            if (uid != 666)
//                return "{\n" +
//                        "\t    \"status\": \"success\",\n" +
//                        "\t    \"items in bag\": 4\n" +
//                        "}";
//
//            return "{\n" +
//                    "\t    \"status\": \"error\",\n" +
//                    "    \"items in bag\": 10\n" +
//                    "\t    \"error message\": \"Too many items in bag. Please remove some\"\n" +
//                    "}";
        }

        public static String removeItemFromBag(String sid, long iid, long uid, String size) {

            String params = "store/" + sid + "/users" + uid + "/inbag";
            params += "/" + iid;

            String jsonResult = DELETE("http://" + ServerApi.serverHostName + params + "?type=text");
            return jsonResult;

//            if (uid != 666)
//                return "{\n" +
//                        "\t    \"status\": \"success\",\n" +
//                        "\t    \"items in bag\": 4\n" +
//                        "}";
//
//            return "{\n" +
//                    "\t    \"status\": \"error\",\n" +
//                    "    \"items in bag\": 10\n" +
//                    "\t    \"error message\": \"item does not exist in bag\"\n" +
//                    "}";
        }

        public static String cleanUserBag(String sid, long uid) {

            String params = "store/" + sid + "/users" + uid + "/inbag";

            String jsonResult = DELETE("http://" + ServerApi.serverHostName + params + "?type=text");
            return jsonResult;

//            if (uid != 666)
//                return "{\n" +
//                        "\t    \"status\": \"success\",\n" +
//                        "}";
//
//            return "{\n" +
//                    "\t    \"status\": \"error\",\n" +
//                    "\t    \"error message\": \"user does not exist\"\n" +
//                    "}";
        }

        public static String getUserDetails(long uid) {
            String params = "users/" + uid;

            String jsonResult = GET("http://" + ServerApi.serverHostName +  params + "?type=text");
            return jsonResult;

//            return "{\n" +
//                    "     \"uid\": \"14932590\",\n" +
//                    "     \"name\": \"Tomer Shohet\",\n" +
//                    "     \"imageURL\": \"https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xtf1/v/t1.0-1/p160x160/10561763_10152659558942682_4962942110060764144_n.jpg?oh=fc3a189547e1d87d0e22ac1c13e975fb&oe=56217C4E&__gda__=1445122776_a533be467206cc4ec8cac28d84f98260\"      \n" +
//                    "}";
        }

        public static String getUserPastItems(String sid, long uid) {
            String params = "store/" + sid + "/users" + uid + "/pastBag";

            String jsonResult = GET("http://" + ServerApi.serverHostName +  params + "?type=text");
            return jsonResult;

//            return "{\n" +
//                    "     \"user\": \n" +
//                    "     {\n" +
//                    "          \"uid\": \"14932590\",\n" +
//                    "          \"name\": \"Tomer Shohet\",\n" +
//                    "          \"imageUrl\": \"https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xtf1/v/t1.0-1/p160x160/10561763_10152659558942682_4962942110060764144_n.jpg?oh=fc3a189547e1d87d0e22ac1c13e975fb&oe=56217C4E&__gda__=1445122776_a533be467206cc4ec8cac28d84f98260\"      \n" +
//                    "     },\n" +
//                    "    \"items\": \n" +
//                    "     [\n" +
//                    "       {\n" +
//                    "    \t          \"id\": 2,\n" +
//                    "\t          \"title\": \"Micro-patterned oxford shirt\",\n" +
//                    "          \"price\": 25.99,\n" +
//                    "\t          \"imageURL\":    \"http://static.zara.net/photos//2015/V/0/2/p/4036/260/403/2/w/1024/4036260403_1_1_1.jpg\",\n" +
//                    "          \"size\": \n" +
//                    "               {\n" +
//                    "                    \"name\": \"m\",\n" +
//                    "                    \"title\": \"M\"\n" +
//                    "              }\n" +
//                    "          \"location\": \"101 5th Avenue, New York, NY, United States\",\n" +
//                    "          \"Date\": \"10/03/2015 16:06:50\",\n" +
//                    "          },\n" +
//                    "          {\n" +
//                    "    \t         \"id\": 43,\n" +
//                    "         \"price\": 39.99,\n" +
//                    "         \"imageURL\":\n" +
//                    "\"http://static.zara.net/photos//2015/V/0/2/p/0706/405/700/2/w/1024/0706405700_1_1_1.jpg\",\n" +
//                    "         \"size\": \n" +
//                    "               {\n" +
//                    "                    \"name\": \"m\",\n" +
//                    "                    \"title\": \"M\"\n" +
//                    "              }\n" +
//                    "          \"location\": \"101 5th Avenue, New York, NY, United States\",\n" +
//                    "          \"Date\": \"10/03/2015 16:05:00\",\n" +
//                    "        }\n" +
//                    "     ]     \n" +
//                    "}";
        }

        public static String GET(String url){
            InputStream inputStream = null;
            String result = "";
            try {

                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return result;
        }
         
        public static String DELETE(String url){
            InputStream inputStream = null;
            String result = "";
            try {

                    // create HttpClient
                    HttpClient httpclient = new DefaultHttpClient();

                    // make GET request to the given URL
                    HttpResponse httpResponse = httpclient.execute(new HttpDelete(url));

                    // receive response as inputStream
                    inputStream = httpResponse.getEntity().getContent();

                    // convert inputstream to string
                    if(inputStream != null)
                        result = convertInputStreamToString(inputStream);
                    else
                        result = "Did not work!";

                } catch (Exception e) {
                    Log.d("InputStream", e.getLocalizedMessage());
                }

            return result;
        }

        public static String POST(String url){
            InputStream inputStream = null;
            String result = "";
            try {

                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();

                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpPost(url));

                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();

                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }

            return result;
        }

        private static String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

       /* public boolean isConnected(){
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected())
                    return true;
                else
                    return false;
        }*/

//        private static class HttpAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... urls) {
//                return GET(urls[0]);
//            }
//
//            // onPostExecute displays the results of the AsyncTask.
//            @Override
//            protected void onPostExecute(String result) {
//                    //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
//                    //etResponse.setText(result);
//               }
//        }
    }
}

