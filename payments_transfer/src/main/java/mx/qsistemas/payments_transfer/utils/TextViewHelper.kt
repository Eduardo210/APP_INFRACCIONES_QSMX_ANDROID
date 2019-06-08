package mx.qsistemas.payments_transfer.utils


import android.widget.EditText

/**
 * Copyright © 2016 快钱支付清算信息有限公司. All rights reserved.
 *
 * @author liudeyu
 * @date 2016年3月1日 上午11:35:28
 * @version 1.0.0
 * @function 实现TextView数组的字符录入，回退等功能
 * @lastmodify
 */
class TextViewHelper(et: EditText) : TextViewHelperInterface {

    private val TAG = "TextViewHelper"
    private var pins: EditText? = null

    override// TODO Auto-generated method stub
    val isFinished: Boolean
        get() = false

    init {
        pins = et
    }

    override fun add(tx: String) {
        // TODO Auto-generated method stub

    }

    override fun back() {
        // TODO Auto-generated method stub
    }


    override fun addPins(len: Int, key: Int) {
        // TODO Auto-generated method stub
        clean()
        var keys = ""
        for (i in 0 until len) {
            keys += '*'.toString()
        }
        pins!!.setText(keys)
    }

    override fun clean() {
        // TODO Auto-generated method stub
        pins!!.setText("")
    }

    override fun isPwdCorrect(correct: String): Boolean {
        // TODO Auto-generated method stub
        return false
    }


}

internal interface TextViewHelperInterface {
    val isFinished: Boolean
    fun add(tx: String)
    fun addPins(len: Int, key: Int)
    fun back()
    fun clean()
    fun isPwdCorrect(correct: String): Boolean
}