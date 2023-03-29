package nghiapd.pers.ltanc_fastchat.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import nghiapd.pers.ltanc_fastchat.DIALOG_GROUP;
import nghiapd.pers.ltanc_fastchat.DIALOG_GROUP_CALLBACK;
import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.R;

public class SettingsFragment extends Fragment implements DIALOG_GROUP_CALLBACK {

    MaterialButton btn_create_group, btn_log_out;
    DIALOG_GROUP dialog_group;

    FirebaseManager firebase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_create_group = view.findViewById(R.id.fs_btn_create_group);
        btn_log_out = view.findViewById(R.id.fs_btn_create_log_out);
        firebase = new FirebaseManager(getActivity());
        dialog_group = new DIALOG_GROUP(getContext(), this, firebase);

        btn_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences login = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = login.edit();
                editor.clear();
                editor.apply();
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        btn_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_group.show();
            }
        });
    }

    @Override
    public void onClickCreate(ArrayList<String> ids, String group_name) {
        if(ids.size()>=2) firebase.createGroups(ids, group_name);
        else
            Toast.makeText(getContext(), "Không đủ thành viên để tạo nhóm", Toast.LENGTH_SHORT).show();
        dialog_group.dismiss();
    }
}