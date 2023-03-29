package nghiapd.pers.ltanc_fastchat.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Presenter.InChatPresenter;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.InChat_Message_Adapter;

public class InChatActivity extends AppCompatActivity implements InChatView {

    Toolbar toolbar;
    RecyclerView chat_view;
    InChat_Message_Adapter adapter;
    TextInputLayout message_edt;
    TextView notification_txt;

    InChatPresenter presenter;
    FirebaseManager firebase;
    String group_id, group_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_chat);

        Init();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(group_name);

        setUpMessageRcv();
        
        message_edt.setEndIconActivated(true);
        message_edt.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = message_edt.getEditText().getText().toString().trim();
                firebase.sendMessage(message, group_id);
                message_edt.getEditText().getText().clear();
            }
        });
    }

    private void Init(){
        Intent i = getIntent();
        group_id = i.getStringExtra("group_id");
        group_name = i.getStringExtra("group_name");
        toolbar = findViewById(R.id.inChat_Toolbar);
        chat_view = findViewById(R.id.in_chat_rcv);
        message_edt = findViewById(R.id.in_chat_message);
        notification_txt = findViewById(R.id.in_chat_notification);
        firebase = new FirebaseManager(this);
        presenter = new InChatPresenter(firebase, this);
    }

    private void setUpMessageRcv(){
        adapter = presenter.getMessageAdapter(group_id);
        chat_view.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chat_view.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        adapter.stopListening();
        super.onStop();
    }

    @Override
    public void showNotification() {
        notification_txt.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNotification() {
        notification_txt.setVisibility(View.GONE);
    }

    @Override
    public void scrollToChat() {
        chat_view.smoothScrollToPosition(0);
    }
}