package nghiapd.pers.ltanc_fastchat.View;

import nghiapd.pers.ltanc_fastchat.Model.ModelUser;

/**
 * Interface điều khiển việc hiển thị thông tin lên màn hình Activity
 */
public interface LoginView {

    /**
     * Hiển thị lỗi số điện thoại không hợp lệ
     */
    void showPhoneInvalid();


    /**
     * Ẩn các lỗi liên quan đến số điện thoại
     */
    void hidePhoneError();


    /**
     * Hiển thị lỗi mật khẩu không hợp lệ
     */
    void showPasswordInvalid();


    /**
     * Ẩn các lỗi liên quan đến mật khẩu
     */
    void hidePasswordError();


    /**
     * Hiển thị lỗi tài khoản không tồn tại
     */
    void showPhoneIsNotExist();


    /**
     * Hiển thị lỗi mật khẩu nhập sai
     */
    void showPasswordInCorrect();


    /**
     * Chuyển sang màn hình chính (chat)
     */
    void navigateHome(ModelUser user);


    /**
     * Hiện thanh progress
     */
    void showProgress();


    /**
     * Ẩn thanh progress
     */
    void hideProgress();
}
