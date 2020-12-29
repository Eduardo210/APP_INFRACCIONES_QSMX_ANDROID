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
import mx.qsistemas.infracciones.helpers.BindingViewHolder
import mx.qsistemas.infracciones.modules.search.SearchContracts
import mx.qsistemas.infracciones.net.result_web.search_result.DataItem

var PRINT_ONLINE = 102
var PAYMENT_ONLINE = 202

class SearchAdapter(private val infractions: MutableList<DataItem>,
                    private val listener: SearchContracts.OnInfractionClick) : RecyclerView.Adapter<BindingViewHolder<ItemResumeInfraBinding>>() {

    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemResumeInfraBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_resume_infra, parent, false))
    }

    override fun getItemCount(): Int = infractions.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder<ItemResumeInfraBinding>, position: Int) {
        with(infractions[position]) {
            holder.getBinding().txtVehicleHeader.text = "${vehicle?.brand} ${vehicle?.subBrand} ${vehicle?.colour}"
            holder.getBinding().txtFolio.text = folio
            holder.getBinding().txtStatusInfra.text = Application.getContext().getString(R.string.status_send)
            holder.getBinding().txtStatusInfra.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.colorGreen))
            holder.getBinding().txtStatusInfra.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(Application.getContext(), R.drawable.green_circle), null, null, null)
            if (fractions != null && fractions.isNotEmpty()) {
                holder.getBinding().txtResumeArtFrac.text = fractions[0]?.reason ?: ""
            } else {
                holder.getBinding().txtResumeArtFrac.text = "Sin artículos"
            }
            holder.getBinding().txtDateInfra.text = "${date}:${time}"
            holder.getBinding().btnPrint.setOnClickListener { view -> listener.onPrintClick(view, position, PRINT_ONLINE) }
            holder.getBinding().btnPayment.setOnClickListener { view -> listener.onPaymentClick(view, position, PAYMENT_ONLINE) }
            if (is_paid!!) { // TODO: --> data.is_paid!!
                holder.getBinding().btnPayment.visibility = View.GONE
                holder.getBinding().txtStatusPayment.text = Application.getContext().getString(R.string.paid)
                holder.getBinding().txtStatusPayment.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.colorRed))
                holder.getBinding().txtStatusPayment.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(Application.getContext(), R.drawable.red_circle), null, null, null)
            } else {
                holder.getBinding().btnPayment.visibility = View.VISIBLE
            }
        }
    }
}

