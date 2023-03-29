package nghiapd.pers.ltanc_fastchat;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Tràn màn hình
 */
public class TRANSPARENT {

    public static void setTransparent(Activity activity){
        //Setting tràn màn hình và thanh điều hướng
        /*Chỉ định FLAG_TRANSLUCENT_NAVIGATION thành màn hình chính
        và thay đổi FLAG_TRANSLUCENT_STATUS trên màn hình này.
        Nghĩa là đồng thời làm cho thanh trạng thái trở nên trong suốt và
        thanh điều hướng cũng sẽ trong suốt.*/
        /*Hoặc có thể sử dụng
        * addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        * để mang lại kết quả tương tự.*/
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Set color cho thanh điều hướng.
        activity.getWindow().setNavigationBarColor(activity.getResources()
                .getColor(R.color.bgColor, activity.getTheme()));
    }
}
