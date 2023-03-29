package nghiapd.pers.ltanc_fastchat.Model;

/**
 * Model Inchat hiển thị trong Inchat activity
 */
public class InChat_Message_Model {
    String message;
    String from_id;

    public InChat_Message_Model(String message, String from_id) {
        this.message = message;
        this.from_id = from_id;
    }

    public InChat_Message_Model(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }
}
