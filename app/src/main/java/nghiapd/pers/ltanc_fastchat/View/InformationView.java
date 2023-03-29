package nghiapd.pers.ltanc_fastchat.View;

import nghiapd.pers.ltanc_fastchat.Model.ModelUser;

/**
 * Interface hiển thị nội dung lên information activity
 */
public interface InformationView {

    /**
     * Hiển thị yêu cầu không bỏ trống tên
     */
    void showFirstNameIsRequired();


    /**
     * Ẩn yêu cầu bỏ trống tên
     */
    void hideFirstNameError();


    /**
     * Hiển thị yêu cầu không bỏ trống tên
     */
    void showLastNameIsRequired();


    /**
     * Ẩn yêu cầu bỏ trống tên
     */
    void hideLastNameError();


    /**
     * Hiển thị lỗi mật khẩu không hợp lệ
     */
    void showPasswordInvalid();


    /**
     * Hiển thị lỗi xác nhận mật khẩu sai
     */
    void showConfirmPasswordIncorrect();


    /**
     * Ẩn các lỗi liên quan đến mật khẩu
     */
    void hidePasswordError();


    /**
     * Ẩn các lỗi liên quan đến xác nhận mật khẩu
     */
    void hideConfirmPasswordError();


    /**
     * Chuyển màn hình tới màn hình chính
     * @param user model chứa thông tin user
     */
    void navigateSignUp(ModelUser user);
}
