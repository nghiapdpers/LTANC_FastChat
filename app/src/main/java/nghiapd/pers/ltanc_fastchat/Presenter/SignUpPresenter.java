package nghiapd.pers.ltanc_fastchat.Presenter;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;

import nghiapd.pers.ltanc_fastchat.DIALOG_OTP;
import nghiapd.pers.ltanc_fastchat.DIALOG_OTP_CALLBACKS;
import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.View.SignUpView;

public class SignUpPresenter implements DIALOG_OTP_CALLBACKS {

    //Interface hiển thị lên view
    SignUpView signUpView;
    FirebaseManager firebaseManager;
    DIALOG_OTP dialog_otp;

    String number_phone;

    //Mã xác nhận credential
    private String mVerifyId;
    //Token resend OTP
    private PhoneAuthProvider.ForceResendingToken mResendToken;


    //Constructor
    public SignUpPresenter(SignUpView signUpView, FirebaseManager firebaseManager, Context context) {
        this.signUpView = signUpView;
        this.firebaseManager = firebaseManager;
        dialog_otp =  new DIALOG_OTP(context, this);
    }


    /**
     * Chức năng gửi OTP đến điện thoại
     * @param number_phone số điện thoại được gửi OTP
     */
    public void sendOTP(String number_phone){
        this.number_phone = number_phone;

        //Kiểm tra điều kiện số điện thoại hợp lệ
        if(TextUtils.isEmpty(number_phone) || number_phone.length()<10){
            //Không hợp lệ thì hiển thị lỗi
            signUpView.showPhoneInvalid();
        }else{
            //Hiển thị progressbar
            signUpView.showProgress();

            //Kiểm tra xem số điện thoại đã đăng ký tài khoản hay chưa:
            firebaseManager.getUserDocumentByPhone(number_phone, new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    //Ẩn progress bar
                    signUpView.hideProgress();

                    //Nếu tác vụ thành công
                    if(task.isSuccessful()){
                        //Lấy dữ liệu trả về
                        DocumentSnapshot document = task.getResult();
                        //Nếu tài khoản tồn tại thì báo lỗi
                        if(document.exists()){
                            signUpView.showPhoneIsExist();
                        }else{
                        //Nếu tài khoản chưa tồn tại thì
                            //Ẩn lỗi
                            signUpView.hidePhoneError();
                            //Gọi lệnh gửi OTP cho số điện thoại
                            firebaseManager.phoneAuthRequest(number_phone, phoneAuthRequestCallbacks, null);
                        }
                    }
                }
            });
        }
    }

    //Callbacks khi mã OTP được Firebase gửi về điện thoại
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
            //Hiển thị lỗi không phù hợp quốc gia
            signUpView.showPhoneIsNotAvailable();
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
            signUpView.hideProgress();
        }
    };

    @Override
    public void onVerify(String code) {
        //Khởi tạo credential để đăng nhập
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerifyId, code);
        //Đăng nhập bằng Credential
        firebaseManager.signInWithCredential(credential, onCompleteCallback);
    }

    @Override
    public void onResend() {
        //Nếu ấn resend thì hiển thị progress:
        signUpView.showProgress();
        //gọi lệnh gửi lại mã OTP:
        firebaseManager.phoneAuthRequest(number_phone, phoneAuthRequestCallbacks, mResendToken);
    }

    /**
     * Callback nếu OTP được xác nhận thành công.
     */
    private OnCompleteListener<AuthResult> onCompleteCallback = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            //Nếu OTP nhập đúng thì
            if (task.isSuccessful()) {
                dialog_otp.cancel();
                signUpView.navigateInformation(number_phone);
            } else {
                //Nếu otp nhập sai thì báo lỗi
                dialog_otp.setDialog_code_error();
            }
        }
    };
}
