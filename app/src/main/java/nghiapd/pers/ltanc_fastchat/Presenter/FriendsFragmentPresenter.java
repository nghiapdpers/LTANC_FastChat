package nghiapd.pers.ltanc_fastchat.Presenter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.FF_List_Friends_Model;
import nghiapd.pers.ltanc_fastchat.Model.FF_List_Request_Model;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.FF_List_Friends_Adapter;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.FF_List_Request_Adapter;
import nghiapd.pers.ltanc_fastchat.View.FriendsFragmentView;

public class FriendsFragmentPresenter {
    FirebaseManager firebase;
    FriendsFragmentView view;

    LifecycleOwner lifecycleOwner;


    //Constructor
    public FriendsFragmentPresenter(FirebaseManager firebase, FriendsFragmentView view, LifecycleOwner lifecycleOwner) {
        this.firebase = firebase;
        this.view = view;
        this.lifecycleOwner = lifecycleOwner;
    }


    /**
     * Lấy adapter cho Requset list
     * @return adapter cho request list
     */
    public FF_List_Request_Adapter getRequestAdapter() {
        Query query = firebase.getRequestQuery();
        //Listener của adapter
        FirestoreRecyclerOptions<FF_List_Request_Model> options =
                new FirestoreRecyclerOptions.Builder<FF_List_Request_Model>()
                        //Set lifecycler owner để auto startListening và stopListening
                        .setLifecycleOwner(lifecycleOwner)
                        //Set đường dẫn snapshot và custom callback
                        .setQuery(query, new SnapshotParser<FF_List_Request_Model>() {
                            @NonNull
                            @Override
                            public FF_List_Request_Model parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                String name = snapshot.getString("nick_name");
                                String phone = snapshot.getId();
                                return new FF_List_Request_Model(String.valueOf(name.charAt(0)), name, phone);
                            }
                        })
                        .build();
        return new FF_List_Request_Adapter(options, firebase){
            //Khi có dữ liệu thay đổi trên database
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                //Nếu có dữ liệu trả về từ snapshot
                if(getItemCount()==0){
                    //Ẩn Request title
                    view.hideRequestTitle();
                }else{
                    //Nếu không thì hiển thị request title.
                    view.showRequestTitle();
                }
            }
        };
    }


    /**
     * Lấy adapter cho Friends list
     * @return adapter cho Friends list
     */
    public FF_List_Friends_Adapter getFriendAdapter() {
        Query query = firebase.getFriendQuery();
        FirestoreRecyclerOptions<FF_List_Friends_Model> options =
                new FirestoreRecyclerOptions.Builder<FF_List_Friends_Model>()
                        .setLifecycleOwner(lifecycleOwner)
                        .setQuery(query, new SnapshotParser<FF_List_Friends_Model>() {
                            @NonNull
                            @Override
                            public FF_List_Friends_Model parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                String name = snapshot.getString("nick_name");
                                return new FF_List_Friends_Model(String.valueOf(name.charAt(0)), name);
                            }
                        })
                        .build();
        return new FF_List_Friends_Adapter(options){
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(getItemCount()==0){
                    view.showFriendNotification();
                }else{
                    view.hideFriendNotification();
                }
            }
        };
    }
}
