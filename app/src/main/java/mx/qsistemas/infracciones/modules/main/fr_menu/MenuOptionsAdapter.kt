package mx.qsistemas.infracciones.modules.main.fr_menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import mx.qsistemas.infracciones.R
import mx.qsistemas.infracciones.databinding.ItemHomeBinding
import mx.qsistemas.infracciones.helpers.BindingViewHolder
import mx.qsistemas.infracciones.net.catalogs.HomeOptions

class MenuOptionsAdapter(private val list: MutableList<HomeOptions>, private val listener: MenuContracts.OnHomeOptionListener)
    : RecyclerView.Adapter<BindingViewHolder<ItemHomeBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder<ItemHomeBinding> {
        return BindingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ItemHomeBinding>, position: Int) {
        holder.getBinding().txtTitleHomeOption.text = list[position].title
        holder.getBinding().txtSubtitleHomeOption.text = list[position].subtitle
        Picasso.get().load(list[position].icon).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.getBinding().imgHomeOption)
        holder.getBinding().btnHomeOption.text = list[position].button
        holder.getBinding().btnHomeOption.setOnClickListener { listener.onClickOption(list[position].idReference) }
    }
}