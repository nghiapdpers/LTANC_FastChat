package nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.InChat_Message_Model;
import nghiapd.pers.ltanc_fastchat.R;

public class InChat_Message_Adapter
    extends FirestoreRecyclerAdapter<InChat_Message_Model, InChat_Message_Adapter.InChatHolder> {

    FirebaseManager firebase;

    static final int SELF = 123456789;

    public InChat_Message_Adapter(@NonNull FirestoreRecyclerOptions<InChat_Message_Model> options,   FirebaseManager firebase) {
        super(options);
        this.firebase = firebase;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).getFrom_id().equals(FirebaseManager.userPhone)){
            return SELF;
        }
        return position;
    }

    @Override
    protected void onBindViewHolder(@NonNull InChatHolder holder, int position, @NonNull InChat_Message_Model model) {
        holder.message.setText(model.getMessage());
        firebase.getUserDocumentByPhone(model.getFrom_id(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    holder.name.setText(doc.getString("first_name")+" "+doc.getString("last_name"));
                }
            }
        });
    }

    @NonNull
    @Override
    public InChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType == SELF){
            v = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_inchat_me, null, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_inchat_other, null, false);
        }
        return new InChatHolder(v);
    }

    class InChatHolder extends RecyclerView.ViewHolder{

        TextView message, name;

        public InChatHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.item_inChat_message);
            name = itemView.findViewById(R.id.item_inChat_name);
        }
    }
}
