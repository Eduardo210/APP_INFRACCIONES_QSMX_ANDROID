package mx.qsistemas.infracciones.modules.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_resume_infra.view.*
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.db.entities.InfractionItem
import mx.qsistemas.infracciones.modules.search.SearchContracts
import java.text.FieldPosition

var PRINT_LOCAL = 101
var PAYMENT_LOCAL = 201
class HistoricalAdapter(private val infraction: MutableList<InfractionItem>
                        , listener: SearchContracts.OnInfractionClick) : RecyclerView.Adapter<HistoricalAdapter.ViewHolderInfra>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInfra {
        return ViewHolderInfra(LayoutInflater.from(parent.context).inflate(R.layout.item_resume_infra, parent, false))
    }

    override fun getItemCount(): Int = infraction.size

    override fun onBindViewHolder(holder: ViewHolderInfra, position: Int) {
        infraction[position].let {
            listener?.let { it1 ->
                holder.bindView(it, it1, position)
            }
        }
    }

    private var lastPosition = -1
    private var listener: SearchContracts.OnInfractionClick? = listener


    class ViewHolderInfra(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(data: InfractionItem, listener: SearchContracts.OnInfractionClick, position: Int) {
            with(data) {
                itemView.txt_vehicle_header?.text = title_vehicle
                itemView.txt_folio?.text = folio
                if (send) {
                    itemView.txt_status_infra.text = itemView.context.getString(R.string.status_send)
                    itemView.txt_status_infra.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.colorGreen))
                    itemView.txt_status_infra.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(Application.getContext(), R.drawable.green_circle), null, null, null)
                } else {
                    itemView.txt_status_infra.text = itemView.context.getString(R.string.status_pending_local)
                    itemView.txt_status_infra.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.colorYellow))
                    itemView.txt_status_infra.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(Application.getContext(), R.drawable.yellow_circle), null, null, null)
                }

                itemView.txt_resume_art_frac.text = motivation
                itemView.txt_date_infra.text = date_infra
                itemView.btn_payment.visibility = View.GONE
                itemView.btn_print.setOnClickListener { view -> listener.onPrintClick(view, position, PRINT_LOCAL) }
                itemView.btn_payment.setOnClickListener { view -> listener.onPaymentClick(view, position, PAYMENT_LOCAL) }
                itemView.txt_status_payment.visibility = View.GONE
                ID_INFRACTION = id_infraction.toString()

            }
        }
    }

}