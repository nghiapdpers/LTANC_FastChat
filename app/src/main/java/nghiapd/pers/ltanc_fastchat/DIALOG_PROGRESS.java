package nghiapd.pers.ltanc_fastchat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;

public class DIALOG_PROGRESS  extends Dialog {

    public DIALOG_PROGRESS(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ẩn title cho dialog
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Set view cho dialog
        setContentView(R.layout.dialog_progress);
        //Không thể tắt dialog
        setCancelable(false);
        //Set background dialog là vô hình
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
