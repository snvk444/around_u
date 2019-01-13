package aroundu.snvk.com.aroundu_template_change.adapters;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import org.w3c.dom.Text;

import java.util.ArrayList;

import aroundu.snvk.com.aroundu_template_change.R;
import aroundu.snvk.com.aroundu_template_change.interfaces.BottomSheetClickListener;
import aroundu.snvk.com.aroundu_template_change.vo.IdentifierBusInfo;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {
    private ArrayList<IdentifierBusInfo> busList;
    private BottomSheetClickListener mListener;
    private int position;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView busName, busSource, busDestination, moreInfo;

        public ViewHolder(View v){
            super(v);
            busName = (TextView) v.findViewById(R.id.bus_name);
            busSource = (TextView) v.findViewById(R.id.bus_source);
            busDestination = (TextView) v.findViewById(R.id.bus_destination);
            moreInfo = (TextView) v.findViewById(R.id.more_info);
            Log.d("bottomsheetitem5", String.valueOf(busName.toString()));
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //IdentifierBusInfo item = busList.get(position);
                    mListener.onMoreInfoClick(busName.getText().toString(), busSource.getText().toString(), busDestination.getText().toString());
                    Log.d("bottomsheetitem2", String.valueOf(busName.getText().toString()));
                }
            });
        }

        @Override
        public void onClick(View v) {
            IdentifierBusInfo item = busList.get(position);
            mListener.onMoreInfoClick(item.busno, item.sourceLocation, item.destinationLocation);
            Log.d("bottomsheetitem2", String.valueOf(item.busno));
        }
    }

    public BottomSheetAdapter(ArrayList<IdentifierBusInfo> busList, BottomSheetClickListener listener){
        this.busList = busList;
        this.mListener = listener;
        Log.d("bottomsheetitem1", String.valueOf(busList));
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IdentifierBusInfo busInfo = busList.get(position);
        Log.d("bottomsheetitemselected", String.valueOf(busInfo.busno));
        holder.busName.setText(busInfo.busno);
        holder.busSource.setText(busInfo.sourceLocation);
        holder.busDestination.setText(busInfo.destinationLocation);
        holder.moreInfo.setPaintFlags(holder.moreInfo.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }
}
