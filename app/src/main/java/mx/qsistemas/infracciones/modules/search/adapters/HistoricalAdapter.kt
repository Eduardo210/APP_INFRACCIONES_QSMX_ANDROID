package mx.qsistemas.infracciones.modules.search.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ItemResumeInfraBinding
import mx.qsistemas.infracciones.db_web.entities.InfractionItem
import mx.qsistemas.infracciones.helpers.BindingViewHolder
import mx.qsistemas.infracciones.modules.search.SearchContracts

var PRINT_LOCAL = 101
var PAYMENT_LOCAL = 201

class HistoricalAdapter(private val infraction: MutableList<InfractionItem>, private val listener: SearchContracts.OnInfractionClick) : RecyclerView.Adapter<BindingViewHolder<ItemResumeInfraBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemResumeInfraBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_resume_infra, parent, false))
    }

    override fun getItemCount(): Int = infraction.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder<ItemResumeInfraBinding>, position: Int) {
        with(infraction[position]) {
            holder.getBinding().txtVehicleHeader.text = "$brand $sub_brand $colour"
            holder.getBinding().txtFolio.text = folio
            if (sync) {
                holder.getBinding().txtStatusInfra.text = Application.getContext().getString(R.string.t_status_send)
                holder.getBinding().txtStatusInfra.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.colorGreen))
                holder.getBinding().txtStatusInfra.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(Application.getContext(), R.drawable.green_circle), null, null, null)
            } else {
                holder.getBinding().txtStatusInfra.text = Application.getContext().getString(R.string.t_status_pending_local)
                holder.getBinding().txtStatusInfra.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.colorYellow))
                holder.getBinding().txtStatusInfra.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(Application.getContext(), R.drawable.yellow_circle), null, null, null)
            }
            holder.getBinding().txtResumeArtFrac.text = reason
            holder.getBinding().txtDateInfra.text = date
            holder.getBinding().btnPayment.visibility = View.GONE
            holder.getBinding().btnPrint.setOnClickListener { view -> listener.onPrintClick(view, position, PRINT_LOCAL) }
            holder.getBinding().btnPayment.setOnClickListener { view -> listener.onPaymentClick(view, position, PAYMENT_LOCAL) }
            holder.getBinding().txtStatusPayment.visibility = View.GONE
        }
    }
}