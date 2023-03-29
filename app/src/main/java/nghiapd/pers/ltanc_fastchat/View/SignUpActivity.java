package nghiapd.pers.ltanc_fastchat.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import nghiapd.pers.ltanc_fastchat.DIALOG_PROGRESS;
import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Presenter.SignUpPresenter;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.TRANSPARENT;

public class SignUpActivity extends AppCompatActivity implements SignUpView{

    //Khai báo view
    private MaterialButton sendOTP;
    private TextInputLayout phone;

    //Khai báo Progress Dialog:
    private DIALOG_PROGRESS progress;

    //Firebase manager:
    private FirebaseManager firebaseManager;

    //Presenter điều khiển hoạt động activity
    SignUpPresenter presenter;

    //Khai báo hàm start activity sau đó nhận kết quả trả về
    ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Làm cho ứng dụng tràn viền
        TRANSPARENT.setTransparent(this);

        //Ánh xạ
        Init();


        //Start activity sau đó nhận kết quả trả về
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //Nhận data trả về từ Register activity
                            Intent data = result.getData();
                            //Set result trả về cho Login activity
                            setResult(Activity.RESULT_OK, data);
                            finish();
                        }
                    }
                });


        //Sự kiện ấn nút Send OTP
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy số điện thoại từ Edit text
                String numberPhone = phone.getEditText().getText().toString().trim();
                presenter.sendOTP(numberPhone);
            }
        });
    }



    /**
     * Ánh xạ
     */
    private void Init() {
        firebaseManager = new FirebaseManager(this);
        sendOTP = findViewById(R.id.signUp_sendOTP);
        phone = findViewById(R.id.signUp_phone);
        progress = new DIALOG_PROGRESS(this);
        presenter = new SignUpPresenter(this, firebaseManager, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Nếu ấn quay lại thì set result code thành cancel
        setResult(Activity.RESULT_CANCELED);
    }

    @Override
    public void showPhoneInvalid() {
        phone.setError(getResources().getText(R.string.phone_error_10_nums));
    }

    @Override
    public void hidePhoneError() {
        phone.setErrorEnabled(false);
    }

    @Override
    public void showPhoneIsNotAvailable() {
        phone.setError(getResources().getText(R.string.phone_error_num_invalid));
    }

    @Override
    public void showPhoneIsExist() {
        phone.setError(getResources().getText(R.string.phone_error_exist));
    }

    @Override
    public void navigateInformation(String phone) {
        Intent i = new Intent(this, InformationActivity.class);
        i.putExtra("phone", phone);
        //Start intent và nhận về callback khi InformationActivity đóng
        mActivityResultLauncher.launch(i);
    }

    @Override
    public void showProgress() {
        progress.show();
    }

    @Override
    public void hideProgress() {
        progress.cancel();
    }
}