package nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import nghiapd.pers.ltanc_fastchat.Model.FF_List_Friends_Model;
import nghiapd.pers.ltanc_fastchat.R;

public class DIALOG_GROUP_Adapter
        extends FirestoreRecyclerAdapter<FF_List_Friends_Model, DIALOG_GROUP_Adapter.DIALOGHOLDER> {

    IsCheckedInterface listener;

    public void setIsCheckedListener(IsCheckedInterface listener){
        this.listener = listener;
    }

    public DIALOG_GROUP_Adapter(@NonNull FirestoreRecyclerOptions<FF_List_Friends_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DIALOGHOLDER holder, int position, @NonNull FF_List_Friends_Model model) {
        holder.logo.setText(model.getLogo());
        holder.name.setText(model.getName());
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.box.isChecked() && listener!=null){
                    listener.isCheckedEvent(model.getId());
                }
            }
        });
    }

    @NonNull
    @Override
    public DIALOGHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_listactivate, null, false);
        return new DIALOGHOLDER(v);
    }

    class DIALOGHOLDER extends RecyclerView.ViewHolder{

        TextView logo, name;
        CheckBox box;

        public DIALOGHOLDER(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.item_friends_la_logo);
            name = itemView.findViewById(R.id.item_friends_la_name);
            box = itemView.findViewById(R.id.item_friends_la_createGroup);
            box.setVisibility(View.VISIBLE);
        }
    }

    //Callback khi một checkbox được check (isChecked = true)
    public interface IsCheckedInterface{
        void isCheckedEvent(String id);
    }
}
