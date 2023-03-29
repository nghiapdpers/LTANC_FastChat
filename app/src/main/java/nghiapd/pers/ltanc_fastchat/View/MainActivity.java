package nghiapd.pers.ltanc_fastchat.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import nghiapd.pers.ltanc_fastchat.BuildConfig;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.TRANSPARENT;

public class MainActivity extends AppCompatActivity {

    private MotionLayout splash_screen;
    private WebView wv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Xây dựng ứng dụng trên Firebase emulator (máy ảo)
        //Xoá để chạy ứng dụng trên firebase thực
        if (BuildConfig.DEBUG) {
            FirebaseDatabase.getInstance().useEmulator("10.0.2.2", 9000);
            FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099);
            FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080);
        }

        /*---------------------*/


        TRANSPARENT.setTransparent(MainActivity.this);


        splash_screen = findViewById(R.id.splash_screen);
        SharedPreferences login = getSharedPreferences("login", Context.MODE_PRIVATE);


        splash_screen.setTransitionListener(new MotionLayout.TransitionListener() {
            //Animation bắt đầu
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                wv_logo = findViewById(R.id.wv_logo);

                //webview tự điều chỉnh kích thước với màn hình
                wv_logo.getSettings().setUseWideViewPort(true);
                //Load ảnh full size mà k cần scroll
                wv_logo.getSettings().setLoadWithOverviewMode(true);
                //Set background webview thành transparent để hiển thị màu background
                wv_logo.setBackgroundColor(0x00000000);

                //Hiển thị logo từ local file.
                wv_logo.loadUrl("file:///android_asset/logo.svg");
            }

            //Animation đang thay đổi
            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
            }

            //Hoàn thành Animation
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                //Chuyển Activity.
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                Intent i2 = new Intent(MainActivity.this, ChatActivity.class);
                //Chuyển activity
                    //Kiểm tra xem đã login trước đó hay chưa
                if(login.getBoolean("islogin", false)){
                    startActivity(i2);
                }else{
                    startActivity(i);
                }
                //Tắt activity hiện tại
                finish();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }   
}