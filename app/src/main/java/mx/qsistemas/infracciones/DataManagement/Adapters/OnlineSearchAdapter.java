package mx.qsistemas.infracciones.DataManagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mx.qsistemas.infracciones.DataManagement.Models.GetSaveInfra;
import mx.qsistemas.infracciones.DataManagement.Models.SearchTerms;
import mx.qsistemas.infracciones.R;

/**
 * Developed by ingmtz on 10/31/16.
 */

public class OnlineSearchAdapter extends ArrayAdapter<SearchTerms>{

    private final ArrayList<SearchTerms> getSaveInfra;
    private ViewHolder holder;

    public OnlineSearchAdapter(Context context, ArrayList<SearchTerms> getSaveInfra) {
        super(context, 0, getSaveInfra);
        this.getSaveInfra = getSaveInfra;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        SearchTerms infraccion = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_fraccion, parent, false);
        }

        holder = new ViewHolder();
        holder.tvListArt = (TextView) convertView.findViewById(R.id.tvListArt);
        holder.tvListFracc = (TextView) convertView.findViewById(R.id.tvListFracc);
        holder.tvListDesc = (TextView) convertView.findViewById(R.id.tvListDesc);
        holder.tvListTitleOne = (TextView) convertView.findViewById(R.id.tvListTitleOne);
        holder.tvListTitleTwo = (TextView) convertView.findViewById(R.id.tvListTitleTwo);
        holder.tvListTitleThree = (TextView) convertView.findViewById(R.id.tvListTitleThree);
        holder.llListDesc = (LinearLayout) convertView.findViewById(R.id.llListDesc);


        assert infraccion != null;
        holder.tvListArt.setText(infraccion.getFolio());
        holder.tvListFracc.setText(infraccion.getIdentifica());
        holder.tvListDesc.setText(infraccion.getNumidentifica());
        holder.tvListTitleOne.setText("Folio:");
        holder.tvListTitleTwo.setText("I.D.");
        holder.tvListTitleThree.setText("Num I.D.");
        holder.ibListDelete = (ImageButton) convertView.findViewById(R.id.ibListDelete);
        holder.ibListDelete.setVisibility(View.INVISIBLE);
        holder.ibListDelete.setClickable(false);



        return convertView;
    }

    private class ViewHolder {
        TextView tvListArt, tvListFracc, tvListDesc, tvListTitleOne, tvListTitleTwo, tvListTitleThree;
        ImageButton ibListDelete;
        LinearLayout llListDesc;
    }
}
