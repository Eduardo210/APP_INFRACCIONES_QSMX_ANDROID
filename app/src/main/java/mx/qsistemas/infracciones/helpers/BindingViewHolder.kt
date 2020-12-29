package mx.qsistemas.infracciones.helpers

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class BindingViewHolder<T> : RecyclerView.ViewHolder {

    private val binding: T

    constructor(itemView: View) : super(itemView) {
        binding = DataBindingUtil.bind(itemView)!!
    }

    fun getBinding(): T = binding
}