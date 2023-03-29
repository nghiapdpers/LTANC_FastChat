package nghiapd.pers.ltanc_fastchat;

import com.alimuzaffar.lib.pin.PinEntryEditText;

/**
 * Callback hai button Verify vaf Resend của OTP Dialog
 */
public interface DIALOG_OTP_CALLBACKS {

    void onVerify(String code);

    void onResend();
}
