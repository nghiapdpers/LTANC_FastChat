package nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.FH_List_Chat_Model;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.View.InChatActivity;

public class FH_List_Chat_Adapter extends RecyclerView.Adapter<FH_List_Chat_Adapter.ListChatHolder> {

    List<FH_List_Chat_Model> data = new ArrayList<>();
    FirebaseManager firebase;
    Context context;

    public FH_List_Chat_Adapter(List<FH_List_Chat_Model> data, FirebaseManager fb) {
        firebase = fb;
        this.data = data;
    }


    @NonNull
    @Override
    public ListChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_listchat, null, false);
        return new ListChatHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatHolder holder, int position) {
        FH_List_Chat_Model model = data.get(position);
        holder.logo.setText(model.getLogo_name());
        holder.name.setText(model.getName());
        holder.latest_message.setText("Click to open chat!");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InChatActivity.class);
                i.putExtra("group_id", model.getGroup_id());
                i.putExtra("group_name", model.getName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ListChatHolder extends RecyclerView.ViewHolder{
        TextView logo, name, latest_message;
        public ListChatHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.item_home_lc_logo);
            name = itemView.findViewById(R.id.item_home_lc_name);
            latest_message = itemView.findViewById(R.id.item_home_lc_latestMessage);
        }
    }

}
