package mx.qsistemas.payments_transfer.dialogs;

import android.content.Context;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import mx.qsistemas.payments_transfer.R;

/**
 * @author liudeyu
 * @version 1.0.0
 * @function
 * @lastmodify
 */
public class StatusDialog extends BaseDialog {

    private TextView title;
    private RelativeLayout ll_close;
    private LottieAnimationView lottie;
    private String TAG = "StatusDialog";

    public StatusDialog(Context context, String title) {
        super(context, R.layout.status_dialog, Gravity.TOP, true);
        initview();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        if (title != null) {
            if (title.length() > 0) {
                this.title.setText(title);
            }
        }
    }

    public void initview() {
        title = findViewById(R.id.txt_status);
        lottie = findViewById(R.id.animation_view);
        lottie.setRepeatMode(LottieDrawable.RESTART);
        lottie.loop(true);
        lottie.setAnimation(R.raw.loader);
        lottie.playAnimation();
    }

    public void updateTitle(String title) {
        this.title.setText(title);
    }

    public void dismissSelf() {
        this.dismiss();
    }
}
