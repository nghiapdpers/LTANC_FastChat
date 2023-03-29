package nghiapd.pers.ltanc_fastchat.View;

import java.util.List;

import nghiapd.pers.ltanc_fastchat.Model.FH_List_Chat_Model;

/**
 * Interface hiển thị dữ liệu lên fragment home
 */
public interface HomeFragmentView {

    /**
     * Hiển thị lỗi không tìm thấy tài khoản
     */
    void showSearchError();

    /**
     * Hiển thị lỗi số điện thoại không hợp lệ
     */
    void showSearchInvalid();


    /**
     * Xoá dữ liệu trên Edit Text search
     */
    void clearSearch();


    /**
     * Ẩn lỗi tìm kiếm
     */
    void hideSearchError();


    /**
     * Hiển thị list chat
     */
    void showListChat(List<FH_List_Chat_Model> data);



    /**
     * Hiển thị lỗi không có đoạn hội thoại
     */
    void showListChatNotification();


    /**
     * Ẩn lỗi không có đoạn hội thoại
     */
    void hideListChatNotification();


    /**
     * Hiện thanh progress
     */
    void showProgress();


    /**
     * Ẩn thanh progress
     */
    void hideProgress();
}
