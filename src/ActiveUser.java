import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ActiveUser {

    private final String username;

    private static final String LOGIN_URL = User.LIST_URL;
    private static final String LOGOUT_URL = LOGIN_URL;

    private static final String RECEIVE_MESSAGE_URL = User.MAIN_URL + "messages";
    private static final String SEND_MESSAGE_URL = User.MAIN_URL + "messages";

    public ActiveUser(String username) {
        this.username = username;
    }

    public static ActiveUser loginUser(String username) {

        try {
            String random_cache_setting = 31415926535080979L + "";

            URI uri = new URIBuilder(LOGOUT_URL).addParameter("username", username).
                    addParameter("stop_cache", random_cache_setting).build();
            String response = Request.Post(uri).execute().returnContent().asString();
            System.out.println(response);
            // Nothing beats wrong documentation..... https://github.com/lodrantl/ChitChat/blob/master/API.md#log-in
            if (new JSONObject(response).getString("status").equals("User logged in")) {
                return new ActiveUser(username);
            } else {
                return null;
            }

        } catch (IOException | URISyntaxException | JSONException e) {
        	e.printStackTrace();
            return null;
        }
    }

    public void logout() {
        try {
            String random_cache_setting = 31415926535080979L + "";

            URI uri = new URIBuilder(LOGOUT_URL).addParameter("username", username).
                    addParameter("stop_cache", random_cache_setting).build();
            String response = Request.Delete(uri).execute().returnContent().asString();
            System.out.println(response);
            
        } catch (IOException | URISyntaxException e) {
            System.out.println("neki");
        }
    }

    public void sendMessage(String message, String receiver) {
        try {
            URI uri = new URIBuilder(SEND_MESSAGE_URL)
                    .addParameter("username", this.getUsername())
                    .build();

            String jsonMessage;

            if (receiver == null) {
                jsonMessage = "{\"global\" : true, \"text\" : \"" + message + "\"  }";
            } else {
                jsonMessage = "{\"global\" : false, \"recipient\" : \"" + receiver + "\",\"text\" :\"" + message + "\"}";
            }
            String responseBody = Request.Post(uri)
                    .bodyString(jsonMessage, ContentType.APPLICATION_JSON)
                    .execute()
                    .returnContent()
                    .asString();

            System.out.println(jsonMessage);
            System.out.println(responseBody);
            
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Message> listMessages() {
        try {
            URI uri = new URIBuilder(RECEIVE_MESSAGE_URL)
                    .addParameter("username", this.getUsername())
                    .build();

            String responseBody = Request.Get(uri)
                    .execute()
                    .returnContent()
                    .asString();


            JSONArray jsonArray = new JSONArray(responseBody);
            ArrayList<Message> messages = new ArrayList<>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); ++i) {
                messages.add(Message.fromJsonObject(jsonArray.getJSONObject(i)));
            }

            return messages;

        } catch (IOException | URISyntaxException | JSONException e) {
            return null;
        }
    }


    public String getUsername() {
        return username;
    }
}
