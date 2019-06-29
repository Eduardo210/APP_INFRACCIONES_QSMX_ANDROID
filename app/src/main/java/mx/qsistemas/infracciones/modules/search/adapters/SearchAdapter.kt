package mx.qsistemas.infracciones.modules.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_resume_infra.view.*
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.modules.search.SearchContracts
import mx.qsistemas.infracciones.net.catalogs.InfractionList
import kotlin.properties.Delegates
var ID_INFRACTION: String = ""

class SearchAdapter(private val infractions: MutableList<InfractionList.Results>,
                    listener: SearchContracts.OnInfractionClick) : RecyclerView.Adapter<SearchAdapter.ViewHolderInfra>() {

    private var lastPosition = -1
    private var listener: SearchContracts.OnInfractionClick? = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInfra {
        return ViewHolderInfra(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_resume_infra, parent, false))
    }

    override fun getItemCount(): Int = infractions.size

    override fun onBindViewHolder(holder: ViewHolderInfra, position: Int) {
        infractions[position].let {
            listener?.let { it1 -> holder.bindView(it, it1,position) }
        }
    }

    /**
     * Here is the key method to apply the animation
     */
    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(Application.getContext(), R.anim.recycler_item_animation)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    class ViewHolderInfra(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(data: InfractionList.Results, listener: SearchContracts.OnInfractionClick, position: Int) {
            with(data) {
                itemView.txt_vehicle_header?.text = title_vehicle
                itemView.txt_folio?.text = folio
                itemView.txt_status_infra.text = itemView.context.getString(R.string.status_send)
                itemView.txt_status_infra.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.colorGreen))
                itemView.txt_status_infra.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(Application.getContext(), R.drawable.green_circle), null, null, null)
                itemView.txt_resume_art_frac.text = motivation
                itemView.txt_date_infra.text = date_infra
                itemView.btn_print.setOnClickListener { view -> listener.onPrintClick(view, position ) }
                itemView.btn_payment.setOnClickListener{view -> listener.onPaymentClick(view, position)}
                ID_INFRACTION = id_infraction.toString()
                if (it_is_paid == 1) {
                    itemView.btn_payment.visibility = View.GONE
                } else {
                    itemView.btn_payment.visibility = View.VISIBLE
                }
            }
        }
    }
}
