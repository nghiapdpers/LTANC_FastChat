package nghiapd.pers.ltanc_fastchat.Presenter;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.InChat_Message_Model;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.InChat_Message_Adapter;
import nghiapd.pers.ltanc_fastchat.View.InChatView;

public class InChatPresenter {
    FirebaseManager firebase;
    InChatView view;

    public InChatPresenter(FirebaseManager firebase, InChatView view) {
        this.firebase = firebase;
        this.view = view;
    }

    public InChat_Message_Adapter getMessageAdapter(String group_id){
        Query query = firebase.getMessageListQuery(group_id);
        FirestoreRecyclerOptions<InChat_Message_Model> options = new
            FirestoreRecyclerOptions.Builder<InChat_Message_Model>()
            .setQuery(query, new SnapshotParser<InChat_Message_Model>() {
                @NonNull
                @Override
                public InChat_Message_Model parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                    String message = snapshot.getString("message_text");
                    String from_id = snapshot.getString("from_id");
                    return new InChat_Message_Model(message, from_id);
                }
            }).build();
        return new InChat_Message_Adapter(options, firebase){
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if(getItemCount()==0){
                    view.showNotification();
                }else{
                    view.scrollToChat();
                    view.hideNotification();
                }
            }
        };
    }
}
