import org.apache.http.client.fluent.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.omg.PortableInterceptor.USER_EXCEPTION;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;

public class User {

    public static final String MAIN_URL = "http://chitchat.andrej.com/";
    public static final String LIST_URL = MAIN_URL + "users";

    private final Instant lastActive;
    private final String username;

    public User(String username, Instant lastActive) {

        this.username = username;
        this.lastActive = lastActive;
    }

    public static ArrayList<User> listActiveUsers() {
        try {
            String response = Request.Get(User.LIST_URL)
                    .execute().returnContent().asString();


            JSONArray jsonArray = new JSONArray(response);

            ArrayList<User> activeUsers = new ArrayList<>(jsonArray.length());

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                activeUsers.add(
                        new User(jsonItem.getString("username"), Instant.parse(jsonItem.getString("last_active")))
                );
            }

            return activeUsers;

        } catch (IOException | JSONException e) {
            return null;
        }
    }

}
