package nghiapd.pers.ltanc_fastchat;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class DIALOG_SEARCH extends Dialog implements View.OnClickListener {

    MaterialTextView logo, name, message;
    MaterialButton request;

    DIALOG_SEARCH_CALLBACKS callbacks;
    public DIALOG_SEARCH(@NonNull Context context, DIALOG_SEARCH_CALLBACKS callbacks) {
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
        setContentView(R.layout.dialog_search_friend);
        //Cài đặt chiều ngang dialog = 8/10 màn hình hiện tại, chiều dọc wrap content
        getWindow().setLayout((9 * width) / 10, WindowManager.LayoutParams.WRAP_CONTENT);
        //Set background dialog là vô hình
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //Ánh xạ của dialog:
        Init();


        request.clearFocus();


        //Sự kiện Onlick nút request
        request.setOnClickListener(this);

    }

    private void Init(){
        logo = findViewById(R.id.dialog_search_user_logo);
        name = findViewById(R.id.dialog_search_username);
        request = findViewById(R.id.dialog_search_request);
        message = findViewById(R.id.dialog_search_request_message);
    }

    public void setLogo(char l) {
        logo.setText(l+"");
    }


    public void setUserName(String username) {
        name.setText(username+"");
    }

    public void disapleRequestBtn(){
        request.setVisibility(View.GONE);
    }

    public void showRequestBtn(){
        request.setVisibility(View.VISIBLE);
    }

    public void setMessage(String msg){
        message.setText(msg);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.dialog_search_request){
            callbacks.onRequest();
        }
    }
}
