import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;

public class Message {

    private final boolean global;
    private final String recipient;
    private final String sender;
    private final String text;
    private final Instant sent_at;

    public Message(boolean global, String recipient, String sender, String text, Instant sent_at) {
        this.global = global;
        this.recipient = recipient;
        this.sender = sender;
        this.text = text;
        this.sent_at = sent_at;
    }

    static Message fromJsonObject(JSONObject jsonObject) {
        try {
            return new Message(jsonObject.getBoolean("global"),
                    jsonObject.getString("recipient"),
                    jsonObject.getString("sender"),
                    jsonObject.getString("text"),
                    Instant.parse(jsonObject.getString("sent_at")));
        } catch (JSONException e) {
            return null;
        }
    }

    public String getSender() {
        return sender;
    }


    public String getText() {
        return text;
    }
}
