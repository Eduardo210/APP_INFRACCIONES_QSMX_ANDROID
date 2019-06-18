package mx.qsistemas.infracciones.modules.infringement.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_resume_infra.view.*
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.R
import kotlin.properties.Delegates

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolderInfra>() {
    var sourceData: List<ResumeInfraItem?> by Delegates.observable(emptyList()){_,_,_ -> notifyDataSetChanged()}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderInfra {
        return ViewHolderInfra(LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_resume_infra,parent,false ))
    }

    override fun getItemCount(): Int = sourceData.size

    override fun onBindViewHolder(holder: ViewHolderInfra, position: Int) {
        sourceData[position]?.let {
            holder.bindView(it)
        }
    }

    class ViewHolderInfra(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindView(data: ResumeInfraItem){
            with(data){
                itemView.txt_vehicle_header?.text = title_vehicle
                itemView.txt_folio?.text = folio
                if(send){
                    itemView.txt_status_infra.text = itemView.context.getString(R.string.status_send)
                    itemView.txt_status_infra.setTextColor(ContextCompat.getColor(Application.getContext(), R.color.colorGreen))
                    itemView.txt_status_infra.setCompoundDrawables(ContextCompat.getDrawable(Application.getContext(), R.drawable.green_circle), null, null, null)
                }
            }
        }
    }
}

data class ResumeInfraItem(
        val title_vehicle: String,
        val folio:String,
        val send: Boolean,
        val description: String,
        val doc_ident: String,
        val date_infra: String)