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
import kotlin.properties.Delegates

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolderInfra>, Filterable {
    private var listener: SearchContracts.OnIncidenceDetailClick? = null
    private val infractions: MutableList<ResumeInfraItem>
    private var infractionsFiltered: MutableList<ResumeInfraItem>
    private var lastPosition = -1

    constructor(infractions: MutableList<ResumeInfraItem>, listener: SearchContracts.OnIncidenceDetailClick){
        this.infractions = infractions
        this.infractionsFiltered = infractions
        this.listener = listener

    }
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
    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                infractionsFiltered = if(charString.isEmpty()){
                    infractions
                }else{
                    val filteredList = mutableListOf<ResumeInfraItem>()
                    for(row in infractions){
                        if(row.folio.contains(charString)){
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = infractionsFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                infractionsFiltered = filterResults.values as MutableList<ResumeInfraItem>
                notifyDataSetChanged()
            }


        }
    }

    var sourceData: List<ResumeInfraItem?> by Delegates.observable(emptyList()){_,_,_ -> notifyDataSetChanged()}








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