package nghiapd.pers.ltanc_fastchat.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.ModelUser;
import nghiapd.pers.ltanc_fastchat.Presenter.InformationPresenter;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.TRANSPARENT;

public class InformationActivity extends AppCompatActivity implements InformationView{

    TextInputLayout phone_number, first_name, last_name, password, cfpassword;
    MaterialButton register_btn;
    InformationPresenter presenter;
    FirebaseManager firebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        TRANSPARENT.setTransparent(this);


        //Ánh xạ
        Init();

        //Nhận Intent và set số điện thoại được chuyển đến, đồng thời disable edit text
        Intent i = getIntent();
        phone_number.getEditText().setText(i.getStringExtra("phone"));
        phone_number.getEditText().setEnabled(false);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lấy dữ liệu người dùng nhập vào
                String mPhone, mLastname, mFirstname, mpPassword, mCfpass;
                mPhone = phone_number.getEditText().getText().toString().trim();
                mLastname = last_name.getEditText().getText().toString().trim();
                mFirstname = first_name.getEditText().getText().toString().trim();
                mpPassword = password.getEditText().getText().toString().trim();
                mCfpass = cfpassword.getEditText().getText().toString().trim();
                //Khởi tạo user chứa dữ liệu
                ModelUser user = new ModelUser(mPhone, mpPassword, mFirstname, mLastname);

                //Gọi hàm đăng ký thông tin
                presenter.register(user, mCfpass);
            }
        });
    }

    private void Init(){
        phone_number = findViewById(R.id.information_phone);
        first_name = findViewById(R.id.information_firstName);
        last_name = findViewById(R.id.information_lastName);
        password = findViewById(R.id.information_password);
        cfpassword = findViewById(R.id.information_cf_password);
        register_btn = findViewById(R.id.information_register);
        firebase = new FirebaseManager(this);
        presenter = new InformationPresenter(this, firebase);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Set result code nếu ấn quay lại
        setResult(Activity.RESULT_CANCELED);
    }

    @Override
    public void showFirstNameIsRequired() {
        first_name.setError(getResources().getText(R.string.first_name_is_empty));
    }

    @Override
    public void hideFirstNameError() {
        first_name.setErrorEnabled(false);
    }

    @Override
    public void showLastNameIsRequired() {
        last_name.setError(getResources().getText(R.string.last_name_is_empty));
    }

    @Override
    public void hideLastNameError() {
        last_name.setErrorEnabled(false);
    }

    @Override
    public void showPasswordInvalid() {
        password.setError(getResources().getText(R.string.password_error_invalid));
    }

    @Override
    public void showConfirmPasswordIncorrect() {
        cfpassword.setError(getResources().getText(R.string.cf_password_error_incorrect));
    }

    @Override
    public void hidePasswordError() {
        password.setErrorEnabled(false);
    }

    @Override
    public void hideConfirmPasswordError() {
        cfpassword.setErrorEnabled(false);
    }

    @Override
    public void navigateSignUp(ModelUser user) {
        Intent i = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        i.putExtra("bundle", bundle);
        //Set result trả về cho SignUpActivity
        setResult(Activity.RESULT_OK, i);
        //Đóng Activity để callback được gọi
        finish();
    }
}