package mx.qsistemas.payments_transfer.dialogs;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.basewin.utils.BCDHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import mx.qsistemas.payments_transfer.Interfaces;
import mx.qsistemas.payments_transfer.R;
import mx.qsistemas.payments_transfer.utils.KeyBoardViewOK;
import mx.qsistemas.payments_transfer.utils.OnGetLayoutSucListener;
import mx.qsistemas.payments_transfer.utils.TextViewHelper;

/**
 * @author liudeyu
 * @version 1.0.0
 * @function
 * @lastmodify
 */
public class PinInputDialog extends BaseDialog {

    private EditText et_pin;
    private TextView title, cardno;
    private RelativeLayout ll_close;
    private TextViewHelper pinhelper = null;
    private Interfaces.OnPinDialogListener listener;
    private List<TextView> list_pins = null;
    private KeyBoardViewOK keyBoardView;
    private FrameLayout fl_keyboard = null;
    private CountDownLatch latch = null;
    private RelativeLayout iv_close;
    private String TAG = "PinInputDialog";
    private Context mcContext = null;

    public PinInputDialog(Context context, String cardno, String title, String amt, Interfaces.OnPinDialogListener listener) {
        super(context, R.layout.pin_input_dialog, Gravity.TOP, true);
        // TODO Auto-generated constructor stub
        this.listener = listener;
        this.mcContext = context;
        initview();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        if (title != null) {
            if (title.length() > 0) {
                this.title.setText(title);
            }
        }
        if (cardno != null) {
            this.cardno.setText("**** **** **** " + cardno.substring(cardno.length() - 4));
        } else {
            this.cardno.setVisibility(View.GONE);
        }
//		this.tx_amt.setText(amt);
        pinhelper = new TextViewHelper(et_pin);
    }

    public void initview() {
        et_pin = (EditText) findViewById(R.id.et_pin);
        title = (TextView) findViewById(R.id.tx_title);
        cardno = (TextView) findViewById(R.id.tx_cardno);
        ll_close = (RelativeLayout) findViewById(R.id.rl_close);
        ll_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                dismiss();
            }
        });

        // iv_close.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        // // TODO Auto-generated method stub
        // if (listener != null)
        // listener.OnPinInput(OnPinInputListener.CANCEL);
        // dismiss();
        // }
        // });

        fl_keyboard = (FrameLayout) findViewById(R.id.fl_keyboard);
        fl_keyboard.removeAllViews();
        keyBoardView = new KeyBoardViewOK(iContext);
        fl_keyboard.addView(keyBoardView.getKeyBoardView(), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                listener.OnCreateOver();
            }
        }, 200);

    }

    /**
     * @param len
     * @param key
     * @Title: setPins
     * @Description: TODO
     * @return: void
     */
    public void setPins(int len, int key) {
        pinhelper.addPins(len, key);
    }

    public void backPin() {
        pinhelper.back();
    }

    /**
     * @return
     */
    public byte[] getKeyLayout() {
        return keyBoardView.getKeyLayout(getCloseLayout());
    }

    public void setKeyShow(final byte[] keys, OnGetLayoutSucListener listener) {
        // latch = new CountDownLatch(1);
        Log.e("AIDL", "设置PINPAD布局了 --- step 22");
        keyBoardView.setKeyShow(keys, listener);
        // try {
        // latch.wait();
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // listener.OnCreateOver();
    }

    /**
     * @return
     * @Title: getcloseLayout
     * @return: byte[]
     */
    public byte[] getCloseLayout() {
        byte[] keylayout = new byte[8];
        int pos = 0;
        pos = addToByteArray(getWidgetPosition(ll_close), keylayout, pos);
        return keylayout;
    }

    /**
     *
     * @param src
     * @param dest
     * @return
     */
    public int addToByteArray(byte[] src, byte[] dest, int position) {
        Log.d(TAG, "addToByteArray:position before = " + position);
        System.arraycopy(src, 0, dest, position, src.length);
        return position += src.length;
    }

    /**
     *
     * @param widget
     * @return
     */
    public byte[] getWidgetPosition(View widget) {
        int[] location = new int[2];
//		widget.getLocationInWindow(location);
        widget.getLocationOnScreen(location);
        int leftx, lefty, rightx, righty;
        leftx = location[0];
        lefty = location[1];
        rightx = location[0] + widget.getWidth();
        righty = location[1] + widget.getHeight();
        Log.d(TAG, "getWidgetPosition: leftx = " + leftx + " lefty = " + lefty + " rightx = " + rightx + " righty = " + righty);
        byte[] pos = new byte[8];
        // 0,768 0x0000 0x02fc
        // 0x00,0x00,0x02,0xfc

        byte[] tmp = BCDHelper.intToBytes2(leftx);
        Log.d(TAG, "getWidgetPosition: tmp = " + BCDHelper.hex2DebugHexString(tmp, tmp.length));
        byte[] tmp1 = BCDHelper.intToBytes2(lefty);
        Log.d(TAG, "getWidgetPosition: tmp1 = " + BCDHelper.hex2DebugHexString(tmp1, tmp1.length));
        byte[] tmp2 = BCDHelper.intToBytes2(rightx);
        Log.d(TAG, "getWidgetPosition: tmp2 = " + BCDHelper.hex2DebugHexString(tmp2, tmp2.length));
        byte[] tmp3 = BCDHelper.intToBytes2(righty);
        Log.d(TAG, "getWidgetPosition: tmp3 = " + BCDHelper.hex2DebugHexString(tmp3, tmp3.length));
        // 178,910 0x00b2 0x038e
        // 0x00,0xb2,0x03,0x8e
        pos[0] = tmp[2];
        pos[1] = tmp[3];
        pos[2] = tmp1[2];
        pos[3] = tmp1[3];
        pos[4] = tmp2[2];
        pos[5] = tmp2[3];
        pos[6] = tmp3[2];
        pos[7] = tmp3[3];
        Log.d(TAG, "getWidgetPosition: position = " + BCDHelper.hex2DebugHexString(pos, pos.length));
        return pos;
    }

    public void dismissSelf() {
        this.dismiss();
    }
}
