package nghiapd.pers.ltanc_fastchat.Model;

import android.text.TextUtils;

import com.google.firebase.firestore.CollectionReference;

import java.io.Serializable;
import java.util.List;

/**
 * Model user chứa thông người dùng.
 */
public class ModelUser implements Serializable {
    private String phone, password, firstName, lastName;

    public ModelUser(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public ModelUser(String phone, String password, String firstName, String lastName) {
        this.phone = phone;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Kiểm tra số điện thoại có hợp lệ hay không
     * @return true nếu số điện thoại thoả mãn: không rỗng và nhiều hơn hoặc bằng 10 ký tự
     */
    public boolean isPhoneValid(){
        return !TextUtils.isEmpty(phone) && phone.length()>=10;
    }

    /**
     * Kiểm tra mật khẩu có hợp lệ hay không
     * @return true nếu mật khẩu thoả mãn: không rỗng và nhiều hơn hoặc bằng 6 ký tự
     */
    public boolean isPasswordValid(){
        return !TextUtils.isEmpty(password) && password.length()>=6;
    }


    /**
     * Kiểm tra xem thông tin tài khoản có hợp lệ hay không
     * @return true nếu hợp lệ, false nếu không
     */
    public boolean userValid(){
        return isPhoneValid() && isPasswordValid() && !TextUtils.isEmpty(firstName)
                && !TextUtils.isEmpty(lastName);
    }
}
