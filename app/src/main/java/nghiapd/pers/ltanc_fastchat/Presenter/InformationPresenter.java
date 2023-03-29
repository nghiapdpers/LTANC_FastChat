package nghiapd.pers.ltanc_fastchat.Presenter;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.ModelUser;
import nghiapd.pers.ltanc_fastchat.View.InformationView;

public class InformationPresenter {
    InformationView informationView;
    FirebaseManager firebaseManager;

    public InformationPresenter(InformationView informationView, FirebaseManager firebaseManager) {
        this.informationView = informationView;
        this.firebaseManager = firebaseManager;
    }


    /**
     * Chức năng đăng ký thông tin
     * @param user model chứa thông tin người dùng
     * @param cfpass xác nhận mật khẩu
     */
    public void register(ModelUser user, String cfpass){
        //Kiểm tra lỗi và hiển thị nếu có
        if(user.getFirstName().isEmpty()) informationView.showFirstNameIsRequired();
        else informationView.hideFirstNameError();

        if(user.getLastName().isEmpty()) informationView.showLastNameIsRequired();
        else informationView.hideLastNameError();

        if(!user.isPasswordValid()) informationView.showPasswordInvalid();
        else informationView.hidePasswordError();

        if(!cfpass.equals(user.getPassword())) informationView.showConfirmPasswordIncorrect();
        else informationView.hideConfirmPasswordError();


        //Nếu thông tin hợp lệ và xác nhận mật khẩu hợp lệ
        if(user.userValid() && cfpass.equals(user.getPassword())){
            //Ẩn các thông báo lỗi
            informationView.hideConfirmPasswordError();
            informationView.hidePasswordError();
            informationView.hideFirstNameError();
            informationView.hideLastNameError();

            //Thêm dữ liệu vào firebase và chuyển sang màn hình Home
            firebaseManager.putUserInformation(user, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    informationView.navigateSignUp(user);
                }
            });
        }
    }
}
