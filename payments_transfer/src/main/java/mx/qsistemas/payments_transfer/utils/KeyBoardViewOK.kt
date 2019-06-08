package mx.qsistemas.payments_transfer.utils

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import android.widget.TextView
import com.basewin.utils.BCDHelper
import mx.qsistemas.payments_transfer.R

/**
 * Copyright © 2016 快钱支付清算信息有限公司. All rights reserved.
 *
 * @ClassName: KeyBoardViewOK
 * @Description: TODO
 * @author: liudeyu
 * @date: 2016年3月2日 下午3:19:11
 */
class KeyBoardViewOK(private val context: Context) : ICupBaseView, OnClickListener {
    private var numKeyListener: OnNumKeyListener? = null
    internal var keylayout = ByteArray(104)
    internal var pos = 0
    lateinit var num0: TextView
    lateinit var num1: TextView
    lateinit var num2: TextView
    lateinit var num3: TextView
    lateinit var num4: TextView
    lateinit var num5: TextView
    lateinit var num6: TextView
    lateinit var num7: TextView
    lateinit var num8: TextView
    lateinit var num9: TextView
    lateinit var numBack: LinearLayout
    lateinit var numok: LinearLayout
    private var topLine: View? = null
    private val layout = ByteArray(96)
    override val keyBoardView: View
        get() {
            val parent = View.inflate(context, R.layout.keyboadview2, null)
            numok = parent.findViewById<View>(R.id.num_ok) as LinearLayout
            num0 = parent.findViewById<View>(R.id.num0) as TextView
            num1 = parent.findViewById<View>(R.id.num1) as TextView
            num2 = parent.findViewById<View>(R.id.num2) as TextView
            num3 = parent.findViewById<View>(R.id.num3) as TextView
            num4 = parent.findViewById<View>(R.id.num4) as TextView
            num5 = parent.findViewById<View>(R.id.num5) as TextView
            num6 = parent.findViewById<View>(R.id.num6) as TextView
            num7 = parent.findViewById<View>(R.id.num7) as TextView
            num8 = parent.findViewById<View>(R.id.num8) as TextView
            num9 = parent.findViewById<View>(R.id.num9) as TextView
            numBack = parent.findViewById<View>(R.id.num_back) as LinearLayout
            topLine = parent.findViewById(R.id.topline) as View
            numok.setOnClickListener(this)
            num0.setOnClickListener(this)
            num1.setOnClickListener(this)
            num2.setOnClickListener(this)
            num3.setOnClickListener(this)
            num4.setOnClickListener(this)
            num5.setOnClickListener(this)
            num6.setOnClickListener(this)
            num7.setOnClickListener(this)
            num8.setOnClickListener(this)
            num9.setOnClickListener(this)
            numBack.setOnClickListener(this)

            return parent
        }

    /**
     * 获取按键布局位置
     *
     * @return
     */
    fun getKeyLayout(closeBtn: ByteArray): ByteArray {
        Log.d(TAG, "getKeyLayout")
        // pos = addToByteArray(getWidgetPosition(num0), keylayout, pos);
        // pos = addToByteArray(getWidgetPosition(num1), keylayout, pos);
        // pos = addToByteArray(getWidgetPosition(num2), keylayout, pos);
        // pos = addToByteArray(getWidgetPosition(num3), keylayout, pos);
        //
        // pos = addToByteArray(getWidgetPosition(num4), keylayout, pos);
        // pos = addToByteArray(getWidgetPosition(num5), keylayout, pos);
        // pos = addToByteArray(getWidgetPosition(num6), keylayout, pos);
        //
        // pos = addToByteArray(getWidgetPosition(num7), keylayout, pos);
        // pos = addToByteArray(getWidgetPosition(num8), keylayout, pos);
        // pos = addToByteArray(getWidgetPosition(num9), keylayout, pos);
        //
        // pos = addToByteArray(getWidgetPosition(numBack), keylayout, pos);

        // pos = addToByteArray(getWidgetPosition(numok), keylayout, pos);

        pos = 0
        val keylayout = ByteArray(104)

        pos = addToByteArray(getWidgetPosition(num1), keylayout, pos)
        Log.d(TAG, "getKeyLayout1")
        pos = addToByteArray(getWidgetPosition(num2), keylayout, pos)
        Log.d(TAG, "getKeyLayout2")
        pos = addToByteArray(getWidgetPosition(num3), keylayout, pos)
        Log.d(TAG, "getKeyLayout3")

        pos = addToByteArray(getWidgetPosition(num4), keylayout, pos)
        Log.d(TAG, "getKeyLayout4")
        pos = addToByteArray(getWidgetPosition(num5), keylayout, pos)
        Log.d(TAG, "getKeyLayout5")
        pos = addToByteArray(getWidgetPosition(num6), keylayout, pos)
        Log.d(TAG, "getKeyLayout6")

        pos = addToByteArray(getWidgetPosition(num7), keylayout, pos)
        Log.d(TAG, "getKeyLayout7")
        pos = addToByteArray(getWidgetPosition(num8), keylayout, pos)
        Log.d(TAG, "getKeyLayout8")
        pos = addToByteArray(getWidgetPosition(num9), keylayout, pos)
        Log.d(TAG, "getKeyLayout9")

        pos = addToByteArray(closeBtn, keylayout, pos)
        Log.d(TAG, "getKeyLayout10")
        pos = addToByteArray(getWidgetPosition(num0), keylayout, pos)
        Log.d(TAG, "getKeyLayout11")
        pos = addToByteArray(getWidgetPosition(numok), keylayout, pos)
        Log.d(TAG, "getKeyLayout12")
        pos = addToByteArray(getWidgetPosition(numBack), keylayout, pos)
        Log.d(TAG, "getKeyLayout13")

        Log.d(TAG, "getKeyLayout = " + BCDHelper.hex2DebugHexString(keylayout, keylayout.size))
        return keylayout

    }

    /**
     * 设置键盘布局
     * Set the keyboard layout
     */
    fun setKeyShow(keys: ByteArray?, listener: OnGetLayoutSucListener) {
        val handler = Handler()
        handler.post {
            if (keys != null) {
                Log.d("AIDL", "setKeyShow = " + BCDHelper.hex2DebugHexString(keys, keys.size))
                // TODO Auto-generated method stub
                num1.text = (keys[0] - 0x30).toString()
                num2.text = (keys[1] - 0x30).toString()
                num3.text = (keys[2] - 0x30).toString()

                num4.text = (keys[3] - 0x30).toString()
                num5.text = (keys[4] - 0x30).toString()
                num6.text = (keys[5] - 0x30).toString()

                num7.text = (keys[6] - 0x30).toString()
                num8.text = (keys[7] - 0x30).toString()
                num9.text = (keys[8] - 0x30).toString()

                num0.text = (keys[10] - 0x30).toString()
                //回显按键顺便获取按键位置
                //According to random keyboard, and obtain the location of the keys
                var pos = 0
                pos = addToByteArray(getWidgetPosition(num1), layout, pos)
                pos = addToByteArray(getWidgetPosition(num2), layout, pos)
                pos = addToByteArray(getWidgetPosition(num3), layout, pos)

                pos = addToByteArray(getWidgetPosition(num4), layout, pos)
                pos = addToByteArray(getWidgetPosition(num5), layout, pos)
                pos = addToByteArray(getWidgetPosition(num6), layout, pos)

                pos = addToByteArray(getWidgetPosition(num7), layout, pos)
                pos = addToByteArray(getWidgetPosition(num8), layout, pos)
                pos = addToByteArray(getWidgetPosition(num9), layout, pos)

                pos = addToByteArray(getWidgetPosition(numBack), layout, pos)
                pos = addToByteArray(getWidgetPosition(num0), layout, pos)
                pos = addToByteArray(getWidgetPosition(numok), layout, pos)
                listener.onSuc()
            }
        }

    }

    override fun onClick(v: View) {
        if (numKeyListener != null) {
            numKeyListener!!.onClick(v)
        }
        Log.d(TAG, "onClick 获取按键位置 = " + BCDHelper.hex2DebugHexString(getWidgetPosition(v), getWidgetPosition(v).size))
    }

    /**
     * 设置键盘监听
     *
     * @param listener
     */
    fun setOnNumKeyListener(listener: OnNumKeyListener) {
        this.numKeyListener = listener
    }

    /**
     *
     * @Title: showTopLine
     * @Description: 显示顶部横线
     * @return: void
     */
    fun showTopLine(ifshow: Boolean) {
        if (ifshow) {
            topLine!!.visibility = View.VISIBLE
        } else {
            topLine!!.visibility = View.INVISIBLE
        }
    }

    /**
     * 将src的数组接到des的数组
     *
     * @param src
     * @param dest
     * @param possition
     * @return
     */
    fun addToByteArray(src: ByteArray, dest: ByteArray, p: Int): Int {
        var position = p
        Log.d(TAG, "addToByteArray:position before = $position")
        System.arraycopy(src, 0, dest, position, src.size)
        position += src.size
        return position
    }

    /**
     * 获取控件的位置
     *
     * @param widget
     * @return
     */
    fun getWidgetPosition(widget: View): ByteArray {
        val location = IntArray(2)
        // widget.getLocationInWindow(location);
        widget.getLocationOnScreen(location)
        val leftx: Int
        val lefty: Int
        val rightx: Int
        val righty: Int
        leftx = location[0]
        lefty = location[1]
        rightx = location[0] + widget.width
        righty = location[1] + widget.height
        Log.d(TAG, "getWidgetPosition: leftx = $leftx lefty = $lefty rightx = $rightx righty = $righty")
        val pos = ByteArray(8)
        // 0,768 0x0000 0x02fc
        // 0x00,0x00,0x02,0xfc

        val tmp = BCDHelper.intToBytes2(leftx)
        Log.d(TAG, "getWidgetPosition: tmp = " + BCDHelper.hex2DebugHexString(tmp, tmp.size))
        val tmp1 = BCDHelper.intToBytes2(lefty)
        Log.d(TAG, "getWidgetPosition: tmp1 = " + BCDHelper.hex2DebugHexString(tmp1, tmp1.size))
        val tmp2 = BCDHelper.intToBytes2(rightx)
        Log.d(TAG, "getWidgetPosition: tmp2 = " + BCDHelper.hex2DebugHexString(tmp2, tmp2.size))
        val tmp3 = BCDHelper.intToBytes2(righty)
        Log.d(TAG, "getWidgetPosition: tmp3 = " + BCDHelper.hex2DebugHexString(tmp3, tmp3.size))
        // 178,910 0x00b2 0x038e
        // 0x00,0xb2,0x03,0x8e
        // 左上x高位
        pos[0] = tmp[2]
        // 左上x低位
        pos[1] = tmp[3]
        // 左上y高位
        pos[2] = tmp1[2]
        // 左上y低位
        pos[3] = tmp1[3]
        // 右下x高位
        pos[4] = tmp2[2]
        // 右下x低位
        pos[5] = tmp2[3]
        // 右下y高位
        pos[6] = tmp3[2]
        // 右下y低位
        pos[7] = tmp3[3]
        Log.d(TAG, "getWidgetPosition: position = " + BCDHelper.hex2DebugHexString(pos, pos.size))
        return pos
    }

    companion object {
        private val TAG = "KeyBoardViewOK"
    }
}


interface ICupBaseView {
    val keyBoardView: View
}

interface OnGetLayoutSucListener {
    fun onSuc()
}

interface OnNumKeyListener {
    fun onClick(view: View)
}
