package nghiapd.pers.ltanc_fastchat.Model;

import java.util.Date;

/**
 * Model list chat ở fragment home
 */
public class FH_List_Chat_Model {
    String logo_name, name, latest_message, group_id;
    Date latest_message_time;

    public FH_List_Chat_Model(String logo_name, String name, String latest_message, String group_id) {
        this.logo_name = logo_name;
        this.name = name;
        this.latest_message = latest_message;
        this.group_id = group_id;
    }

    public FH_List_Chat_Model() {
    }

    public String getLogo_name() {
        return logo_name;
    }

    public void setLogo_name(String logo_name) {
        this.logo_name = logo_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatest_message() {
        return latest_message;
    }

    public void setLatest_message(String latest_message) {
        this.latest_message = latest_message;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public Date getLatest_message_time() {
        return latest_message_time;
    }

    public void setLatest_message_time(Date latest_message_time) {
        this.latest_message_time = latest_message_time;
    }
}
