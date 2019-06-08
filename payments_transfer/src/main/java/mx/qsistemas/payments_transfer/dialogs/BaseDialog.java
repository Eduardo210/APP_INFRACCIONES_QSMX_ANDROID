package mx.qsistemas.payments_transfer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import mx.qsistemas.payments_transfer.R;


/**
 * @author liudeyu
 * @version 1.0.0
 * @function
 * @lastmodify
 */
public abstract class BaseDialog extends Dialog {

    protected Context iContext;

    /**
     * @param context
     * @param layoutResID
     * @param gravity
     * @Title:BaseDialog
     * @Description:TODO
     */
    public BaseDialog(Context context, int layoutResID, int gravity) {
        super(context, R.style.DialogStyle);
        iContext = context;
        setContentView(layoutResID);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Window dialogWindow = getWindow();
        // dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        lp.width = wm.getDefaultDisplay().getWidth();
        dialogWindow.setAttributes(lp);
        //Set the animation
        if (gravity != Gravity.CENTER) {
            dialogWindow.setWindowAnimations(R.style.dialog_anim_bottom);
        } else {
            dialogWindow.setWindowAnimations(R.style.dialog_anim_center);
        }

        show();
    }

    /**
     * @param context
     * @param layoutResID
     * @param gravity
     * @param fullscreen
     * @Title:BaseDialog
     * @Description:TODO
     */
    public BaseDialog(Context context, int layoutResID, int gravity, boolean fullscreen) {
        super(context, R.style.DialogStyle);
        iContext = context;
        setContentView(layoutResID);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Window dialogWindow = getWindow();
        // dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        lp.width = wm.getDefaultDisplay().getWidth();
        if (fullscreen)
            lp.height = wm.getDefaultDisplay().getHeight();
        dialogWindow.setAttributes(lp);
        if (gravity != Gravity.CENTER) {
            dialogWindow.setWindowAnimations(R.style.dialog_anim_bottom);
        } else {
            dialogWindow.setWindowAnimations(R.style.dialog_anim_center);
        }
        show();
    }

    /**
     * @param context
     * @param layoutResID
     * @param x
     * @param gravity
     * @Title:BaseDialog
     * @Description:TODO
     */
    public BaseDialog(Context context, int layoutResID, int x, int y, int gravity) {
        super(context, R.style.DialogStyle);
        iContext = context;
        setContentView(layoutResID);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Window dialogWindow = getWindow();
        // dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(gravity);
        lp.x = x;
        lp.y = y;
        dialogWindow.setAttributes(lp);
        dialogWindow.setWindowAnimations(R.style.dialog_anim);
        show();
    }
}
