package nghiapd.pers.ltanc_fastchat.Model;

/**
 * Model List Quest hiển thị ở fragment friends
 */
public class FF_List_Request_Model {
    String logo, name, phone;

    public FF_List_Request_Model(String logo, String name, String phone) {
        this.logo = logo;
        this.name = name;
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
