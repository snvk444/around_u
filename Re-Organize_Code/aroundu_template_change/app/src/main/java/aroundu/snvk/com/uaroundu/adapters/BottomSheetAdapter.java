package aroundu.snvk.com.uaroundu.adapters;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import aroundu.snvk.com.uaroundu.R;
import aroundu.snvk.com.uaroundu.interfaces.BottomSheetClickListener;
import aroundu.snvk.com.uaroundu.beans.IdentifierBusInfo;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {
    private ArrayList<IdentifierBusInfo> busList;
    private BottomSheetClickListener mListener;
    private int position;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView busName, lineId, busSource, busDestination, moreInfo;

        public ViewHolder(View v){
            super(v);
            busName = v.findViewById(R.id.bus_name);
            lineId = v.findViewById(R.id.line_id);
            busSource = v.findViewById(R.id.bus_source);
            busDestination = v.findViewById(R.id.bus_destination);
            moreInfo = v.findViewById(R.id.more_info);
            Log.d("Bottomsheet", String.valueOf(lineId.toString()) + "," + String.valueOf(busName.toString()) + "," + String.valueOf(busSource.toString()) + "," + String.valueOf(busDestination.toString()));
            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mListener.onMoreInfoClick(busName.getText().toString(), busSource.getText().toString(), busDestination.getText().toString(), lineId.getText().toString());
                    Log.d("bottomsheetitem2", String.valueOf(busName.getText().toString()));
                }
            });
        }

        @Override
        public void onClick(View v) {
            IdentifierBusInfo item = busList.get(position);
            mListener.onMoreInfoClick(item.busno, item.sourceLocation, item.destinationLocation, item.lineid);
        }
    }

    public BottomSheetAdapter(ArrayList<IdentifierBusInfo> busList, BottomSheetClickListener listener){
        this.busList = busList;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_bus_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IdentifierBusInfo busInfo = busList.get(position);
        holder.busName.setText(busInfo.busno);
        holder.lineId.setText(busInfo.lineid);
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