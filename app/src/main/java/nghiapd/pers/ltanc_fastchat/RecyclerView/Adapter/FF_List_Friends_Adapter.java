package nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.FF_List_Friends_Model;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.View.FriendsFragmentView;

public class FF_List_Friends_Adapter
        extends FirestoreRecyclerAdapter<FF_List_Friends_Model, FF_List_Friends_Adapter.FriendHolder> {

    public FF_List_Friends_Adapter(
            @NonNull FirestoreRecyclerOptions<FF_List_Friends_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendHolder holder, int position, @NonNull FF_List_Friends_Model model) {
        holder.logo.setText(model.getLogo());
        holder.name.setText(model.getName());
    }

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_listactivate, null, false);
        return new FriendHolder(v);
    }

    class FriendHolder extends RecyclerView.ViewHolder{
        TextView logo, name;

        public FriendHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.item_friends_la_logo);
            name = itemView.findViewById(R.id.item_friends_la_name);
        }
    }
}
