package nghiapd.pers.ltanc_fastchat.View;

import nghiapd.pers.ltanc_fastchat.Model.ModelUser;

/**
 * Interface View hiển thị nội dung lên activity
 */
public interface SignUpView {

    /**
     * Hiển thị lỗi số điện thoại không hợp lệ
     */
    void showPhoneInvalid();


    /**
     * Ẩn các lỗi liên quan đến số điện thoại
     */
    void hidePhoneError();


    /**
     * Hiển thị lỗi số điện thoại không được hỗ trợ
     */
    void showPhoneIsNotAvailable();

    /**
     * Hiển thị lỗi số điện thoại đã tồn tại
     */
    void showPhoneIsExist();


    /**
     * Chuyển đến trang điền thông tin
     */
    void navigateInformation(String phone);


    /**
     * Hiện thanh progress
     */
    void showProgress();


    /**
     * Ẩn thanh progress
     */
    void hideProgress();
}
