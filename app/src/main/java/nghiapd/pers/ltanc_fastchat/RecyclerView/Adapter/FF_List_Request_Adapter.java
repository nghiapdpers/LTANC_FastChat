package nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.button.MaterialButton;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.FF_List_Request_Model;
import nghiapd.pers.ltanc_fastchat.R;

public class FF_List_Request_Adapter
        extends FirestoreRecyclerAdapter<FF_List_Request_Model, FF_List_Request_Adapter.RequestHolder> {
    FirebaseManager firebase;
    public FF_List_Request_Adapter(@NonNull FirestoreRecyclerOptions<FF_List_Request_Model> options
            , FirebaseManager firebase) {
        super(options);
        this.firebase = firebase;
    }

    @Override
    protected void onBindViewHolder(@NonNull RequestHolder holder, int position, @NonNull FF_List_Request_Model model) {
        holder.logo.setText(model.getLogo());
        holder.name.setText(model.getName());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.acceptRequest(model.getPhone(), model.getName(), null);
            }
        });
        holder.deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.denyRequest(model.getPhone(), null);
            }
        });
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_request, null, false);
        return new RequestHolder(v);
    }

    class RequestHolder extends RecyclerView.ViewHolder {

        TextView logo, name;
        MaterialButton accept, deny;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.item_friends_rq_logo);
            name = itemView.findViewById(R.id.item_friends_rq_name);
            accept = itemView.findViewById(R.id.item_friends_rq_accept);
            deny = itemView.findViewById(R.id.item_friends_rq_deny);
        }
    }
}
