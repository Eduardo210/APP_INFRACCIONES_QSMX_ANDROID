package mx.qsistemas.infracciones.DataManagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import mx.qsistemas.infracciones.DataManagement.Models.ArtFraccion;
import mx.qsistemas.infracciones.R;

/**
 * Developed by ingmtz on 10/31/16.
 */

public class FraccionAdapter extends ArrayAdapter<ArtFraccion>{

    private final ArrayList<ArtFraccion> artFraccions;
    private ViewHolder holder;

    public FraccionAdapter(Context context, ArrayList<ArtFraccion> artFraccions) {
        super(context, 0, artFraccions);
        this.artFraccions = artFraccions;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        ArtFraccion fraccion = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_fraccion, parent, false);
        }

        holder = new ViewHolder();
        holder.tvListArt = (TextView) convertView.findViewById(R.id.tvListArt);
        holder.tvListFracc = (TextView) convertView.findViewById(R.id.tvListFracc);
        holder.tvListDesc = (TextView) convertView.findViewById(R.id.tvListDesc);

        assert fraccion != null;
        holder.tvListArt.setText(fraccion.getArticulo());
        holder.tvListFracc.setText(fraccion.getFraccion());
        holder.tvListDesc.setText(fraccion.getDescripcion());

        holder.ibListDelete = (ImageButton) convertView.findViewById(R.id.ibListDelete);
        holder.ibListDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artFraccions.remove(position);
                notifyDataSetChanged();
            }
        });

        if (fraccion.getIsSearch() == 1){
            holder.ibListDelete.setVisibility(View.INVISIBLE);
            holder.ibListDelete.setClickable(false);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvListArt, tvListFracc, tvListDesc;
        ImageButton ibListDelete;
    }
}
