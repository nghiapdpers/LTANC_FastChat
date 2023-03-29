package nghiapd.pers.ltanc_fastchat.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.system.Os;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import nghiapd.pers.ltanc_fastchat.DIALOG_SEARCH;
import nghiapd.pers.ltanc_fastchat.DIALOG_SEARCH_CALLBACKS;
import nghiapd.pers.ltanc_fastchat.FirebaseManager;
import nghiapd.pers.ltanc_fastchat.Model.FH_List_Chat_Model;
import nghiapd.pers.ltanc_fastchat.View.HomeFragmentView;

public class HomeFragmentPresenter implements DIALOG_SEARCH_CALLBACKS {

    FirebaseManager firebaseManager;
    HomeFragmentView view;
    DIALOG_SEARCH dialog_search;
    LifecycleOwner lifecycleOwner;
    Context context;

    String name,
            number_phone;
    String latest_message,
            group_name;

    public HomeFragmentPresenter(FirebaseManager firebaseManager, HomeFragmentView view,
                                 Context context, LifecycleOwner lifecycleOwner) {
        this.firebaseManager = firebaseManager;
        this.view = view;
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.dialog_search = new DIALOG_SEARCH(context, this);
    }

    public void search(String number_phone, String userPhone){
        this.number_phone = number_phone;
        if(number_phone.isEmpty() || number_phone.length()<10 ||number_phone.equals(userPhone)){
            view.showSearchInvalid();
            return;
        }else{
            view.hideSearchError();
        }


        //Hiển thị progess
        view.showProgress();
        //Tìm kiếm thông tin và trả về
        firebaseManager.getUserDocumentByPhone(number_phone, onCompleteGetDocs);
    }


    /**
     * Lấy Adapter cho list chat
     * @return adapter cho list chat
     */
    public void getListChatAdapter(){
        List<FH_List_Chat_Model> data = new ArrayList<>();
        Query query = firebaseManager.getListChatQuery();
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                view.hideListChatNotification();
                data.clear();
                if(!value.isEmpty()){
                    for (DocumentSnapshot dc : value.getDocuments()) {
                        FH_List_Chat_Model model = new FH_List_Chat_Model();
                        model.setGroup_id(dc.getReference().getParent().getParent().getId());
                        group_name = dc.getString("group_name");
                        model.setName(group_name);
                        model.setLogo_name(String.valueOf(group_name.charAt(0)));
                        data.add(model);
                    }
                    view.showListChat(data);
                }else{
//                    view.showListChatNotification();
                }
            }
        });
    }

    private OnCompleteListener<DocumentSnapshot> onCompleteGetDocs = new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            //Nếu lấy dữ liệu thành công
            if(task.isSuccessful()){
                //Biến nhận dữ liệu đc trả về
                DocumentSnapshot document = task.getResult();

                //Nếu dữ liệu tồn tại thì lấy thông tin và gửi về dialog search
                if(document.exists()){
                    //Lấy thông tin đăng nhập của người dùng.
                    SharedPreferences login = context.getSharedPreferences("login", Context.MODE_PRIVATE);
                    String userPhone = login.getString("phone", "");

                    //Hiển thị dialog search và ẩn progress
                    dialog_search.show();

                    view.hideProgress();

                    //Lấy tên và set vào dialog
                    name = document.getString("first_name")+" "+document.getString("last_name");
                    dialog_search.setLogo(name.charAt(0));
                    dialog_search.setUserName(name);

                    //Kiểm tra xem 2 người đã là bạn bè chưa
                    document.getReference().collection("friends")
                        .document(userPhone).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    //Lấy document trả về
                                    DocumentSnapshot doc = task.getResult();
                                    //Nếu là bạn bè thì:
                                    if(doc.exists() && doc.getString("status").equals("Friend")){
                                        //Hiển thị thông tin đã là bạn bè
                                        dialog_search.setMessage("You've been friends");
                                        //Ẩn nút request
                                        dialog_search.disapleRequestBtn();
                                    }else{
                                        dialog_search.showRequestBtn();
                                        dialog_search.setMessage("Let's make friends!?");
                                    }
                                }
                            }
                        });
                }
                //Nếu dữ liệu không tồn tại thì thông báo lỗi
                else{
                    view.hideProgress();
                    view.showSearchError();
                }
            }
        }
    };

    @Override
    public void onRequest() {
        firebaseManager.sendFriendRequest(number_phone, name, new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                //Nếu gửi request thành công thì tắt dialog search
                dialog_search.cancel();
            }
        });
        //Sau khi ấn tìm kiếm thì xoá search edit text
        view.clearSearch();
    }
}
