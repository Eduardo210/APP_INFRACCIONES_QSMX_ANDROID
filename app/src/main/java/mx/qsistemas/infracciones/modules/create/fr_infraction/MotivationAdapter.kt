package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ItemInfractionMotivationBinding
import mx.qsistemas.infracciones.helpers.BindingViewHolder
import mx.qsistemas.infracciones.singletons.SingletonInfraction

class MotivationAdapter : RecyclerView.Adapter<BindingViewHolder<ItemInfractionMotivationBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemInfractionMotivationBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_infraction_motivation, parent, false))
    }

    override fun getItemCount(): Int {
        return SingletonInfraction.motivationList.size
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemInfractionMotivationBinding>, position: Int) {
        holder.getBinding().txtArticleItem.text = SingletonInfraction.motivationList[position].article.number
        holder.getBinding().txtFractionItem.text = SingletonInfraction.motivationList[position].fraction.number
        holder.getBinding().txtDescriptionItem.text = SingletonInfraction.motivationList[position].fraction.description
        holder.getBinding().edtMotivationItem.setText(SingletonInfraction.motivationList[position].motivation)
        holder.getBinding().edtMotivationItem.imeOptions = EditorInfo.IME_ACTION_DONE
        holder.getBinding().edtMotivationItem.setRawInputType(InputType.TYPE_CLASS_TEXT)
        val spannable = SpannableString("U.M.A. ${SingletonInfraction.motivationList[position].fraction.uma}")
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(Application.getContext(), R.color.colorPrimaryDark)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(Application.getContext(), R.color.colorBlack)), 6, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.getBinding().txtUmas.text = spannable
        holder.getBinding().btnCloseItem.setOnClickListener {
            SingletonInfraction.motivationList.removeAt(position)
            notifyDataSetChanged()
        }
        holder.getBinding().edtMotivationItem.doOnTextChanged { text, _, _, _ ->
            SingletonInfraction.motivationList[position].motivation = text?.trim().toString()
        }
    }
}