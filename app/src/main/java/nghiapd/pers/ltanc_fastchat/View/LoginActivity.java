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
import nghiapd.pers.ltanc_fastchat.Model.ModelUser;
import nghiapd.pers.ltanc_fastchat.Presenter.LoginPresenter;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.TRANSPARENT;

public class LoginActivity extends AppCompatActivity implements LoginView,View.OnClickListener {

    MaterialButton login_btn, register_btn;
    TextInputLayout phone_text, password_text;

    //Firebase manager điều khiển các hoạt động liên quan đến Firebase
    FirebaseManager firebaseManager;

    //Presenter điều khiển activity
    LoginPresenter presenter;

    //thanh tiến trình
    DIALOG_PROGRESS progress;

    //Khai báo hàm start activity sau đó nhận kết quả trả về
    ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Ánh xạ
        Init();

        //Tràn viền màn hình
        TRANSPARENT.setTransparent(this);


        //Start activity sau đó nhận kết quả trả về
        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            //Nhận dữ liệu trả về từ SignUpActivity
                            Intent data = result.getData();
                            Bundle bundle = data.getBundleExtra("bundle");
                            ModelUser mUser = (ModelUser) bundle.getSerializable("user");
                            //Set số điện thoại và mật khẩu từ data gửi về
                            phone_text.getEditText().setText(mUser.getPhone());
                            password_text.getEditText().setText(mUser.getPassword());
                        }
                    }
                });


        //Sự kiện onlick các nút
        login_btn.setOnClickListener(this);
        register_btn.setOnClickListener(this);

    }

    private void Init(){
        login_btn = findViewById(R.id.login_login_btn);
        register_btn = findViewById(R.id.login_register_btn);
        phone_text = findViewById(R.id.login_phone);
        password_text = findViewById(R.id.login_password);
        firebaseManager = new FirebaseManager(this);
        presenter = new LoginPresenter(this, firebaseManager, this);
        progress = new DIALOG_PROGRESS(this);
    }


    //Sự kiện Onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_login_btn:
                String phone, pass;
                phone = phone_text.getEditText().getText().toString().trim();
                pass = password_text.getEditText().getText().toString().trim();
                presenter.login(new ModelUser(phone, pass));
                break;
            case R.id.login_register_btn:
                Intent i = new Intent(this, SignUpActivity.class);
                //Start intent và nhận callback khi SignUpActivity đóng
                mActivityResultLauncher.launch(i);
                break;
        }
    }

    @Override
    public void showPhoneInvalid() {
        phone_text.setError(getResources().getText(R.string.phone_error_10_nums));
    }

    @Override
    public void hidePhoneError() {
        phone_text.setErrorEnabled(false);
    }

    @Override
    public void showPasswordInvalid() {
        password_text.setError(getResources().getText(R.string.password_error_invalid));
    }

    @Override
    public void hidePasswordError() {
        password_text.setErrorEnabled(false);
    }

    @Override
    public void showPhoneIsNotExist() {
        phone_text.setError(getResources().getText(R.string.phone_is_not_exist));
    }

    @Override
    public void showPasswordInCorrect() {
        password_text.setError(getResources().getText(R.string.password_error_incorrect));
    }

    @Override
    public void navigateHome(ModelUser user) {
        Intent i = new Intent(this, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        i.putExtra("bundle", bundle);
        startActivity(i);
        finish();
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