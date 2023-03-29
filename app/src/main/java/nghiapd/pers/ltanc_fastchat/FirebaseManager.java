package nghiapd.pers.ltanc_fastchat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import nghiapd.pers.ltanc_fastchat.Model.ModelUser;

public class FirebaseManager {

    //Khai báo biến
    protected Activity activity;
    public static String userPhone;

    private String userName;

    //Constructor
    public FirebaseManager(Activity activity) {
        this.activity = activity;
        //Lấy thông tin login shared preference
        SharedPreferences login = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        userPhone = login.getString("phone", "");
        userName = login.getString("name", "");
    }

    /**
     * Request OTP và nhận callback từ Firebase
     *
     * @param mobile    số điện thoại cần gửi OTP
     * @param callbacks callback Firebase trả về để xác định trạng thái mã OTP
     */
    public void phoneAuthRequest(String mobile, PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks,
                                 @NonNull PhoneAuthProvider.ForceResendingToken resendingToken) {
        //Khởi tạo request cho Firebase
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                .setPhoneNumber("+84" + mobile)               //Số điện thoại nhận mã
                .setTimeout(60L, TimeUnit.SECONDS)   //Thời gian chờ
                .setActivity(activity)                      //Activity
                .setCallbacks(callbacks)                    //Callbacks của request
                .setForceResendingToken(resendingToken)     //Mã gửi lại OTP
                .build();

        //Gửi request lên Firebase
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    /**
     * Đăng nhập bằng thông tin Credetial
     *
     * @param credential Credential đăng nhập
     * @param onComplete callback xử lý sự kiện khi xác nhận OTP
     */
    public void signInWithCredential(PhoneAuthCredential credential, OnCompleteListener<AuthResult> onComplete) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(activity, onComplete);
    }

    /**
     * Hàm lấy dữ liệu bằng số điện thoại
     *
     * @param phone    số điện thoại để lấy dữ liệu
     * @param callback xử lý dữ liệu được trả về
     */
    public void getUserDocumentByPhone(String phone, OnCompleteListener<DocumentSnapshot> callback) {
        //Lấy đường dẫn tới Collection users
        CollectionReference users = FirebaseFirestore.getInstance().collection("users");
        //Lấy thông tin user bằng số điện thoại và xử lý callback
        users.document(phone).get().addOnCompleteListener(callback);
    }


    /**
     * Đẩy dữ liệu User lên firebase firestore
     *
     * @param user     model user chứa dữ liệu đẩy lên server
     * @param callback xử lý khi dữ liệu được thêm vào thành công
     */
    public void putUserInformation(ModelUser user, OnSuccessListener<Void> callback) {
        //Khởi tạo Map để chứa dữ liệu put lên firestore
        Map<String, Object> userData = new HashMap<>();
        userData.put("first_name", user.getFirstName());
        userData.put("last_name", user.getLastName());
        userData.put("password", user.getPassword());
        userData.put("phone_number", user.getPhone());
        userData.put("groups", Arrays.asList());

        //Thêm dữ liệu vào collection users với collection id = số điện thoại người dùng
        FirebaseFirestore.getInstance().collection("users").document(user.getPhone())
                .set(userData)
                .addOnSuccessListener(callback);
    }


    /**
     * Gửi request tới một người khác
     *
     * @param number_phone số điện thoại sẽ gửi request tới
     * @param name         nickname đặt cho người đó
     * @param callback     xử lý sau khi gửi request xong
     */
    public void sendFriendRequest(String number_phone, String name,
                                  OnCompleteListener<DocumentReference> callback) {
        //Khởi tạo Map để chứa dữ liệu sẽ được ghi vào database của người gửi request
        Map<String, Object> newFriend = new HashMap<>();
        newFriend.put("nick_name", name);
        newFriend.put("status", "Requested");
        newFriend.put("friend_ref", (DocumentReference) FirebaseFirestore.getInstance().collection("users").document(number_phone));

        //Thêm dữ liệu vào collection của người gửi request
        DocumentReference myRef = FirebaseFirestore.getInstance().collection("users").document(userPhone)
                .collection("friends").document(number_phone);
        myRef.set(newFriend).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map<String, Object> status = new HashMap<>();
                status.put("status", "Requested");
                status.put("time", FieldValue.serverTimestamp());

                myRef.collection("statusLog").add(status);
            }
        });


        //Khởi tạo Map để chứa dữ liệu ghi vào database của người nhận request
        Map<String, Object> friendData = new HashMap<>();
        friendData.put("nick_name", userName);
        friendData.put("status", "isRequested");
        friendData.put("friend_ref", (DocumentReference) FirebaseFirestore.getInstance().collection("users").document(userPhone));

        //Thêm dữ liệu vào collection users của người nhận request
        DocumentReference friendRef = FirebaseFirestore.getInstance().collection("users").document(number_phone)
                .collection("friends").document(userPhone);
        friendRef.set(friendData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map<String, Object> status = new HashMap<>();
                status.put("status", "isRequested");
                status.put("time", FieldValue.serverTimestamp());

                friendRef.collection("statusLog").add(status).addOnCompleteListener(callback);
            }
        });
    }


    /**
     * Chấp nhận yêu cầu kết bạn từ người dùng
     *
     * @param number_phone số điện thoại người gửi request
     * @param callback     xử lý sau khi chấp nhận request
     */
    public void acceptRequest(String number_phone,  String name, OnCompleteListener<DocumentReference> callback) {
        //Cập nhật thông tin friend của người dùng
        DocumentReference myRef = FirebaseFirestore.getInstance().collection("users").document(userPhone);

        myRef.collection("friends")
                .document(number_phone).update("status", "Friend")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Map<String, Object> status = new HashMap<>();
                        status.put("status", "Friend");
                        status.put("time", FieldValue.serverTimestamp());

                        myRef.collection("friends").document(number_phone)
                                .collection("statusLog").add(status);
                    }
                });

        //Cập nhật thông tin friend với người gửi request
        DocumentReference friendRef = FirebaseFirestore.getInstance().collection("users").document(number_phone);

        friendRef.collection("friends")
                .document(userPhone).update("status", "Friend")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Map<String, Object> status = new HashMap<>();
                        status.put("status", "Friend");
                        status.put("time", FieldValue.serverTimestamp());

                        friendRef.collection("friends")
                                .document(userPhone).collection("statusLog")
                                .add(status).addOnCompleteListener(callback);
                    }
                });

        Map<String, Object> chat = new HashMap<>();

        FirebaseFirestore.getInstance().collection("groups").add(chat)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            //Lấy đường dẫn Reference của đoạn chat
                            DocumentReference doc = task.getResult();

                            //Cập nhật đoạn hội thoại
                            myRef.update("groups", FieldValue.arrayUnion(doc));
                            friendRef.update("groups", FieldValue.arrayUnion(doc));
                            //Thêm bản thân vào collection members của groups.
                            Map<String, Object> me = new HashMap<>();
                            me.put("user_ref", myRef);
                            me.put("group_name", name);
                            me.put("joint_time", FieldValue.serverTimestamp());
                            doc.collection("members").document(userPhone).set(me);
                            //Thêm bạn bè vào collection members của groups
                            Map<String, Object> friend = new HashMap<>();
                            friend.put("user_ref", friendRef);
                            friend.put("group_name", userName);
                            friend.put("joint_time", FieldValue.serverTimestamp());
                            doc.collection("members").document(number_phone).set(friend);
                        }
                    }
                });
    }


    public void denyRequest(String number_phone, OnCompleteListener<DocumentReference> callback) {
        //Cập nhật thông tin Denied của người dùng
        DocumentReference myRef = FirebaseFirestore.getInstance().collection("users").document(userPhone);

        myRef.collection("friends")
                .document(number_phone).update("status", "Denied")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Map<String, Object> status = new HashMap<>();
                        status.put("status", "Denied");
                        status.put("time", FieldValue.serverTimestamp());

                        myRef.collection("friends")
                                .document(number_phone).collection("statusLog").add(status);
                    }
                });

        //Cập nhật thông tin denied với người gửi request
        DocumentReference friendRef = FirebaseFirestore.getInstance().collection("users").document(number_phone);

        friendRef.collection("friends")
                .document(userPhone).update("status", "Denied").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Map<String, Object> status = new HashMap<>();
                        status.put("status", "Denied");
                        status.put("time", FieldValue.serverTimestamp());

                        friendRef.collection("friends")
                                .document(userPhone).collection("statusLog")
                                .add(status).addOnCompleteListener(callback);
                    }
                });
    }


    /**
     * Lấy query cho FirestoreRecyclerViewOptions của request list
     * @return Query của request list
     */
    public Query getRequestQuery(){
        return FirebaseFirestore.getInstance().collection("users")
                .document(userPhone)
                .collection("friends")
                .whereEqualTo("status", "isRequested");
    }


    /**
     * Lấy query cho FirestoreRecyclerViewOptions của friend list
     * @return Query của request list
     */
    public Query getFriendQuery(){
        return FirebaseFirestore.getInstance().collection("users")
                .document(userPhone)
                .collection("friends")
                .whereEqualTo("status", "Friend");
    }



    /**
     * Lấy query cho FirestoreRecyclerViewOptions của list chat
     * @return Query của list chat
     */
    public Query getListChatQuery(){
        return FirebaseFirestore.getInstance().collectionGroup("members")
                .whereEqualTo("user_ref", FirebaseFirestore.getInstance().collection("users").document(userPhone));
    }


    /**
     * Lấy query cho FirestoreRecyclerViewOptions của message list
     * @return Query của message list
     */
    public Query getMessageListQuery(String  group_id){
        return FirebaseFirestore.getInstance().collection("groups").document(group_id).collection("messages")
                .orderBy("sent_time", Query.Direction.DESCENDING).limit(30);
    }



    /**
     * Lấy query cho FirestoreRecyclerViewOptions của message list
     * @return Query của message list
     */
    public void sendMessage(String message, String group_id){
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("from_id", userPhone);
        newMessage.put("message_text", message);
        newMessage.put("sent_time", FieldValue.serverTimestamp());
        FirebaseFirestore.getInstance().collection("groups").document(group_id).collection("messages")
                .add(newMessage);
    }


    public void createGroups(ArrayList<String> ids, String group_name){
        Map<String, Object> chat = new HashMap<>();
        FirebaseFirestore.getInstance().collection("groups").add(chat)
        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    //Lấy đường dẫn Reference của đoạn chat
                    DocumentReference doc = task.getResult();
                    for (int i = 0; i < ids.size(); i++) {
                        //Cập nhật đoạn hội thoại
                        DocumentReference user_ref = FirebaseFirestore.getInstance().collection("users").document(ids.get(i));
                        user_ref.update("groups", FieldValue.arrayUnion(doc));
                        //Thêm bản thân vào collection members của groups.
                        Map<String, Object> user_infor = new HashMap<>();
                        user_infor.put("user_ref", user_ref);
                        user_infor.put("group_name", group_name);
                        user_infor.put("joint_time", FieldValue.serverTimestamp());
                        doc.collection("members").document(ids.get(i)).set(user_infor);
                    }
                    //Cập nhật đoạn hội thoại
                    DocumentReference myRef = FirebaseFirestore.getInstance().collection("users").document(userPhone);
                    myRef.update("groups", FieldValue.arrayUnion(doc));
                    //Thêm bản thân vào collection members của groups.
                    Map<String, Object> myInfor = new HashMap<>();
                    myInfor.put("user_ref", myRef);
                    myInfor.put("group_name", group_name);
                    myInfor.put("joint_time", FieldValue.serverTimestamp());
                    doc.collection("members").document(userPhone).set(myInfor);
                }
            }
        });
    }
}
