package nghiapd.pers.ltanc_fastchat.Model;

/**
 * Model list friend hiển thị ở fragment friend và dialog
 */
public class FF_List_Friends_Model {
    String logo, name, id;

    public FF_List_Friends_Model(String logo, String name) {
        this.logo = logo;
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
