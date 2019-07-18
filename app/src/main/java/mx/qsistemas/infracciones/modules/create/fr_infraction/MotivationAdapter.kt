package mx.qsistemas.infracciones.modules.create.fr_infraction

import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_infraction_motivation.view.*
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ItemInfractionMotivationBinding
import mx.qsistemas.infracciones.singletons.SingletonInfraction

class MotivationAdapter : RecyclerView.Adapter<ViewHolderMotivation>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMotivation {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemInfractionMotivationBinding.inflate(inflater, parent, false)
        return ViewHolderMotivation(binding.root)
    }

    override fun getItemCount(): Int {
        return SingletonInfraction.motivationList.size
    }

    override fun onBindViewHolder(holder: ViewHolderMotivation, position: Int) {
        holder.itemView.txt_article_item.text = SingletonInfraction.motivationList[position].article.article
        holder.itemView.txt_fraction_item.text = SingletonInfraction.motivationList[position].fraction.fraccion
        holder.itemView.txt_description_item.text = SingletonInfraction.motivationList[position].fraction.description
        holder.itemView.edt_motivation_item.setText(SingletonInfraction.motivationList[position].motivation)
        holder.itemView.edt_motivation_item.imeOptions = EditorInfo.IME_ACTION_DONE
        holder.itemView.edt_motivation_item.setRawInputType(InputType.TYPE_CLASS_TEXT)
        val spannable = SpannableString("U.M.A. ${SingletonInfraction.motivationList[position].fraction.minimum_wages}")
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(Application.getContext(), R.color.colorPrimaryDark)), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(Application.getContext(), R.color.colorBlack)), 6, spannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        holder.itemView.txt_umas.text = spannable
        holder.itemView.btn_close_item.setOnClickListener {
            SingletonInfraction.motivationList.removeAt(position)
            notifyDataSetChanged()
        }
        holder.itemView.edt_motivation_item.doOnTextChanged { text, start, count, after ->
            SingletonInfraction.motivationList[position].motivation = text?.trim().toString().toUpperCase()
        }
    }
}

class ViewHolderMotivation(itemView: View) : RecyclerView.ViewHolder(itemView)