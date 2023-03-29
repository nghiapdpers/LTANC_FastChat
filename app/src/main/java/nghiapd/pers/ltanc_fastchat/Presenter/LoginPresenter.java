package nghiapd.pers.ltanc_fastchat.Presenter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import nghiapd.pers.ltanc_fastchat.DIALOG_OTP;
import nghiapd.pers.ltanc_fastchat.DIALOG_OTP_CALLBACKS;
import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.ModelUser;
import nghiapd.pers.ltanc_fastchat.View.LoginView;

/**
 * Login Presenter điều khiển hoạt động của Login Activity
 */
public class LoginPresenter implements DIALOG_OTP_CALLBACKS {

    //Interface hiển thị thông tin lên activity
    LoginView loginView;
    FirebaseManager firebaseManager;

    DIALOG_OTP dialog_otp;

    //Model user chứa dữ liệu người dùng
    ModelUser user;

    //Mã xác nhận credential
    private String mVerifyId;
    //Token resend OTP
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    //Constructor
    public LoginPresenter(LoginView loginView, FirebaseManager firebaseManager, Context context) {
        this.loginView = loginView;
        this.firebaseManager = firebaseManager;
        dialog_otp = new DIALOG_OTP(context, this);
    }


    /**
     * Chức năng đăng nhập vào ứng dụng
     * @param user model user sử dụng để đăng nhập.
     */
    public void login(ModelUser user){
        this.user = user;

        //Kiểm tra các trường hợp không hợp lệ của số điện thoại
        if(!user.isPhoneValid()){
            loginView.showPhoneInvalid();
        }else{
            loginView.hidePhoneError();
        }

        //Kiểm tra các trường hợp không hợp lệ của số điện thoại
        if(!user.isPasswordValid()){
            loginView.showPasswordInvalid();
        }else{
            loginView.hidePasswordError();
        }


        //Nếu số điện thoại và mật khẩu thoả mãn
        if(user.isPasswordValid() && user.isPhoneValid()){
            //Hiển thị progress
            loginView.showProgress();

            //Kiểm tra thông tin trên firebase
            firebaseManager.getUserDocumentByPhone(user.getPhone(), onCompleteGetDocs);
        }
    }

    @Override
    public void onVerify(String code) {
        //Khởi tạo credential để đăng nhập
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerifyId, code);
        //Đăng nhập bằng Credential
        firebaseManager.signInWithCredential(credential, onCompleteLogIn);
    }

    @Override
    public void onResend() {
        //Nếu ấn resend thì hiển thị progress:
        loginView.showProgress();
        //gọi lệnh gửi lại mã OTP:
        firebaseManager.phoneAuthRequest(user.getPhone(), phoneAuthRequestCallbacks, mResendToken);
    }


    /**
     * Callback khi lấy thông tin user
     */
    private OnCompleteListener<DocumentSnapshot> onCompleteGetDocs = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            //Nếu lấy dữ liệu thành công
            if(task.isSuccessful()){
                //Biến nhận dữ liệu đc trả về
                DocumentSnapshot document = task.getResult();

                //Nếu dữ liệu tồn tại thì kiểm tra mật khẩu
                if(document.exists()){
                    //Lấy mật khẩu từ dữ liệu và so sánh với mật khẩu nhập vào
                    String password = document.getString("password");
                    if(password.equals(user.getPassword())){
                        //Lấy thông tin về người dùng.
                        user.setFirstName(document.getString("first_name"));
                        user.setLastName(document.getString("last_name"));

                        //Nếu đã có xác thực firebase auth thì
                        if(FirebaseAuth.getInstance().getCurrentUser() != null){
                            //ẩn progress bar và chuyển sang màn hình Home()
                            loginView.hideProgress();
                            loginView.hidePasswordError();
                            loginView.navigateHome(user);
                        }else{
                            //Nếu chưa có xác thực firebase auth thì:
                            //Gọi lệnh gửi OTP cho số điện thoại
                            firebaseManager.phoneAuthRequest(user.getPhone(), phoneAuthRequestCallbacks, null);
                        }
                    }else{
                        //Nếu mật khẩu từ dữ liệu và mật khẩu nhập vào khác nhau thì báo lỗi
                        loginView.hideProgress();
                        loginView.showPasswordInCorrect();
                    }
                }
                //Nếu dữ liệu không tồn tại thì thông báo lỗi
                else{
                    loginView.hideProgress();
                    loginView.showPhoneIsNotExist();
                }
            }
        }
    };


    /**
     * Callback nếu OTP được xác nhận thành công.
     */
    private OnCompleteListener<AuthResult> onCompleteLogIn = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            //Nếu OTP nhập đúng thì
            if (task.isSuccessful()) {
                dialog_otp.cancel();
                loginView.navigateHome(user);

            } else {
                //Nếu otp nhập sai thì báo lỗi
                dialog_otp.setDialog_code_error();
            }
        }
    };



    /**
     * Callbacks khi mã OTP được Firebase gửi về điện thoại
     */
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneAuthRequestCallbacks
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        //Điện thoại nhận được mã OTP
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //Tự động lấy OTP code từ tin nhắn
            String code = phoneAuthCredential.getSmsCode();
            //Set OTP cho edit text
            if (code != null) dialog_otp.setDialog_code(code);
        }

        //Firebase không thể gửi được mã OTP do lỗi
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
        }

        //OTP đã được gửi đến số điện thoại
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //Lấy mã xác nhận credential
            mVerifyId = s;
            mResendToken = forceResendingToken;

            //Hiển thị dialog nhập OTP
            dialog_otp.show();

            //Nếu Code được gửi thì tắt progress:
            loginView.hideProgress();
        }
    };
}
