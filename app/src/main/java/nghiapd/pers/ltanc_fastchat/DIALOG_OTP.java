package nghiapd.pers.ltanc_fastchat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.material.button.MaterialButton;

public class DIALOG_OTP extends Dialog implements View.OnClickListener {

    private PinEntryEditText dialog_code;
    private MaterialButton dialog_verify, dialog_resend;

    DIALOG_OTP_CALLBACKS callbacks;


    public DIALOG_OTP(@NonNull Context context, DIALOG_OTP_CALLBACKS callbacks) {
        super(context);
        this.callbacks = callbacks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Lấy kích thước màn hình
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        //Lấy kích thước chiều ngang
        int width = metrics.widthPixels;

        //Set view cho dialog
        setContentView(R.layout.dialog_otp);
        //Không tắt dialog khi ấn ngoài màn hình
        setCanceledOnTouchOutside(false);
        //Cài đặt chiều ngang dialog = 9/10 màn hình hiện tại, chiều dọc wrap content
        getWindow().setLayout((9 * width) / 10, WindowManager.LayoutParams.WRAP_CONTENT);
        //Set background dialog là vô hình
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //Ánh xạ của dialog:
        Init();


        //Sự kiện Onlick nút Verify
        dialog_verify.setOnClickListener(this);

        //Sự kiện Onlick nút resend:
        dialog_resend.setOnClickListener(this);

    }

    private void Init(){
        dialog_verify = findViewById(R.id.otpDialog_verify);
        dialog_code = findViewById(R.id.otpDialog_code);
        dialog_resend = findViewById(R.id.otpDialog_resend);
    }

    @Override
    public void show() {
        super.show();
        //đếm ngược resend khi hiển thị Dialog
        countDownProgress.start();
    }

    /**
     * Bộ đếm ngược thời gian
     *     millisInfuture là khoảng thời gian đếm ngược
     *     countDownInterval khoảng thời gian lặp lại
     */
    private CountDownTimer countDownProgress = new CountDownTimer(60000, 1000) {

        //Sau mỗi 1 khoảng thời gian countDownInterval thì thực hiện công việc trong hàm onTick()
        @Override
        public void onTick(long millisUntilFinished) {
            //Đếm thời gian resend
            dialog_resend.setText(String.valueOf(millisUntilFinished/1000));
            //Không thể ấn vào nút resend
            dialog_resend.setEnabled(false);
        }

        //Sau khi đếm hết thời gian thì thực hiện hàm onFinish()
        @Override
        public void onFinish() {
            //Hiển thị nút resend
            dialog_resend.setText("Resend");
            //Có thể ấn vào nút Resend
            dialog_resend.setEnabled(true);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.otpDialog_verify:
                if (dialog_code.length() > 0) {
                    callbacks.onVerify(dialog_code.getText().toString());
                }
                break;
            case R.id.otpDialog_resend:
                callbacks.onResend();
                break;
        }
    }

    public void setDialog_code(String code){
        dialog_code.setText(code);
    }

    public void setDialog_code_error(){
        dialog_code.setError(getContext().getResources().getText(R.string.otp_incorrect));
    }
}
