package aroundu.snvk.com.aroundu_template_change.adapters;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        public ViewHolder(View v, BottomSheetClickListener listener){
            super(v);
            busName = (TextView) v.findViewById(R.id.bus_name);
            busSource = (TextView) v.findViewById(R.id.bus_source);
            busDestination = (TextView) v.findViewById(R.id.bus_destination);
            moreInfo = (TextView) v.findViewById(R.id.more_info);

            mListener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            IdentifierBusInfo item = busList.get(position);
            mListener.onMoreInfoClick(item.busno, item.sourceLocation, item.destinationLocation);
        }
    }

    public BottomSheetAdapter(ArrayList<IdentifierBusInfo> busList, BottomSheetClickListener listener){
        this.busList = busList;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bus_list_row, parent, false);

        return new ViewHolder(itemView, mListener
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IdentifierBusInfo busInfo = busList.get(position);
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
