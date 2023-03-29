package nghiapd.pers.ltanc_fastchat.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Comparator;
import java.util.List;

import nghiapd.pers.ltanc_fastchat.DIALOG_PROGRESS;
import nghiapd.pers.ltanc_fastchat.DIALOG_SEARCH;
import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.FH_List_Chat_Model;
import nghiapd.pers.ltanc_fastchat.Model.ModelUser;
import nghiapd.pers.ltanc_fastchat.Presenter.HomeFragmentPresenter;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.FH_List_Chat_Adapter;
import nghiapd.pers.ltanc_fastchat.SpacesItemDecoration;

public class HomeFragment extends Fragment implements HomeFragmentView{

    EditText edt_search;
    RecyclerView list_chat;
    FH_List_Chat_Adapter chat_adapter;
    TextView list_chat_notification;
    DIALOG_PROGRESS progress;
    HomeFragmentPresenter presenter;
    FirebaseManager firebase;


    //Khởi tạo view cho fragment
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
    }

    //Fragment khởi tạo view xong
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Lưu thông tin đăng nhập vào shared preference
        SharedPreferences login = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = login.edit();
        if(!login.getBoolean("islogin", false)){
            //Lấy dữ liệu được truyền từ Login activity qua.
            Intent i = getActivity().getIntent();
            Bundle b = i.getBundleExtra("bundle");
            ModelUser user = (ModelUser) b.getSerializable("user");

            editor.putBoolean("islogin", true);
            editor.putString("phone", user.getPhone());
            editor.putString("name", user.getFirstName()+" "+user.getLastName());
            editor.putString("password", user.getPhone());
            editor.commit();
        }

        //Ánh xạ
        Init(view);

        //Sự kiện ấn nút enter của keyboard trong khung search
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                SharedPreferences login = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                //Nếu nút ấn là Enter thì:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Xử lý sự kiện
                    presenter.search(edt_search.getText().toString(), login.getString("phone",""));
                    edt_search.getText().clear();
                    return true;
                }
                return false;
            }
        });


    }

    private void Init(View view) {
        edt_search = view.findViewById(R.id.fragment_home_search);
        list_chat = view.findViewById(R.id.fragment_home_listChat);
        list_chat_notification = view.findViewById(R.id.fragment_home_list_chat_notification);
        firebase = new FirebaseManager(getActivity());
        presenter = new HomeFragmentPresenter(firebase, this, getContext(), getViewLifecycleOwner());
        progress = new DIALOG_PROGRESS(getContext());
        list_chat.addItemDecoration(new SpacesItemDecoration(50, 50, 20, 20));
    }

    @Override
    public void showListChat(List<FH_List_Chat_Model> data) {
        chat_adapter = new FH_List_Chat_Adapter(data, firebase);
        list_chat.setAdapter(chat_adapter);
        list_chat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onStart() {
        presenter.getListChatAdapter();
        super.onStart();
    }

    @Override
    public void showSearchError() {
        edt_search.setError(getActivity().getResources().getText(R.string.phone_is_not_exist));
    }

    @Override
    public void showSearchInvalid() {
        edt_search.setError(getActivity().getResources().getText(R.string.phone_invalid_search));
    }

    @Override
    public void hideSearchError() {
        edt_search.clearFocus();
    }

    @Override
    public void showListChatNotification() {
        list_chat_notification.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideListChatNotification() {
        list_chat_notification.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progress.show();
    }

    @Override
    public void hideProgress() {
        progress.cancel();
    }

    @Override
    public void clearSearch() {
        edt_search.setText("");
        edt_search.clearFocus();
    }
}