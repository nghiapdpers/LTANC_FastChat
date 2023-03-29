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
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import nghiapd.pers.ltanc_fastchat.Model.FF_List_Friends_Model;
import nghiapd.pers.ltanc_fastchat.Model.FH_List_Chat_Model;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.DIALOG_GROUP_Adapter;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.FF_List_Friends_Adapter;

public class DIALOG_GROUP extends Dialog {

    RecyclerView friend_rcv;
    DIALOG_GROUP_Adapter adapter;
    MaterialButton btn_create;

    EditText edt_name;

    DIALOG_GROUP_CALLBACK callback;
    FirebaseManager firebase;

    ArrayList<String> ids = new ArrayList<>();
    String group_name;

    public DIALOG_GROUP(@NonNull Context context, DIALOG_GROUP_CALLBACK callback, FirebaseManager firebase) {
        super(context);
        this.callback = callback;
        this.firebase = firebase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Lấy kích thước màn hình
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        //Lấy kích thước chiều ngang
        int width = metrics.widthPixels;

        //Set view cho dialog
        setContentView(R.layout.dialog_group);
        //Không tắt dialog khi ấn ngoài màn hình
        setCanceledOnTouchOutside(false);
        //Cài đặt chiều ngang dialog = 9/10 màn hình hiện tại, chiều dọc wrap content
        getWindow().setLayout((9 * width) / 10, WindowManager.LayoutParams.WRAP_CONTENT);
        //Set background dialog là vô hình
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //Ánh xạ của dialog:
        Init();

        setUpFriendRcv();

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                group_name = edt_name.getText().toString().trim();
                if(group_name.isEmpty()) group_name = "New Group Chat";
                callback.onClickCreate(ids, group_name);
                edt_name.getText().clear();
            }
        });
    }

    private void Init() {
        friend_rcv = findViewById(R.id.dialog_group_rcv);
        btn_create = findViewById(R.id.dialog_group_create);
        edt_name = findViewById(R.id.dialog_group_name);
    }

    public void setUpFriendRcv(){
        adapter = getFriendAdapter();
        friend_rcv.setAdapter(adapter);
        friend_rcv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        friend_rcv.addItemDecoration(new SpacesItemDecoration(10, 10, 20, 15));

        adapter.setIsCheckedListener(new DIALOG_GROUP_Adapter.IsCheckedInterface() {
            @Override
            public void isCheckedEvent(String id) {
                if(!ids.contains(id)){
                    ids.add(id);
                }
            }
        });
    }

    public DIALOG_GROUP_Adapter getFriendAdapter() {
        Query query = firebase.getFriendQuery();
        FirestoreRecyclerOptions<FF_List_Friends_Model> options =
                new FirestoreRecyclerOptions.Builder<FF_List_Friends_Model>()
                    .setQuery(query, new SnapshotParser<FF_List_Friends_Model>() {
                        @NonNull
                        @Override
                        public FF_List_Friends_Model parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            String name = snapshot.getString("nick_name");
                            FF_List_Friends_Model model = new FF_List_Friends_Model(String.valueOf(name.charAt(0)), name);
                            model.setId(snapshot.getId());
                            return model;
                        }
                    })
                    .build();
        return new DIALOG_GROUP_Adapter(options);
    }

    @Override
    public void dismiss() {
        adapter.stopListening();
        super.dismiss();
    }

    @Override
    protected void onStart() {
        adapter.startListening();
        super.onStart();
    }
}
