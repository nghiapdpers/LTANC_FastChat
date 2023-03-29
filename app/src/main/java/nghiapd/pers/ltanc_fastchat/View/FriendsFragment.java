package nghiapd.pers.ltanc_fastchat.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.FF_List_Request_Model;
import nghiapd.pers.ltanc_fastchat.Presenter.FriendsFragmentPresenter;
import nghiapd.pers.ltanc_fastchat.R;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.FF_List_Friends_Adapter;
import nghiapd.pers.ltanc_fastchat.RecyclerView.Adapter.FF_List_Request_Adapter;
import nghiapd.pers.ltanc_fastchat.SpacesItemDecoration;

public class FriendsFragment extends Fragment implements FriendsFragmentView{

    RecyclerView requestRcv, friendRcv;
    FF_List_Request_Adapter request_adapter;
    FF_List_Friends_Adapter friends_adapter;

    FriendsFragmentPresenter presenter;

    FirebaseManager firebase;

    TextView requestTitle, friendNotification;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Init(view);
    }

    @Override
    public void onStart() {
        super.onStart();

        setUpRequestRcv();

        setUpFriendRcv();
    }

    private void Init(View view){
        requestTitle = view.findViewById(R.id.fragment_friends_request_title);
        requestRcv = view.findViewById(R.id.fragment_friends_requsetList);
        friendRcv = view.findViewById(R.id.fragment_friends_friendsList);
        friendNotification = view.findViewById(R.id.fragment_friends_notification);
        firebase = new FirebaseManager(getActivity());
        presenter = new FriendsFragmentPresenter(firebase, this, getViewLifecycleOwner());
        requestRcv.addItemDecoration(new SpacesItemDecoration(80, 80, 40, 15));
        friendRcv.addItemDecoration(new SpacesItemDecoration(10, 10, 20, 15));
    }

    private void setUpRequestRcv(){
        request_adapter = presenter.getRequestAdapter();
        requestRcv.setAdapter(request_adapter);
        requestRcv.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    private void setUpFriendRcv(){
        friends_adapter = presenter.getFriendAdapter();
        friendRcv.setAdapter(friends_adapter);
        friendRcv.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showRequestTitle() {
        requestTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRequestTitle() {
        requestTitle.setVisibility(View.GONE);
    }

    @Override
    public void showFriendNotification() {
        friendNotification.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFriendNotification() {
        friendNotification.setVisibility(View.GONE);
    }
}