package aroundu.snvk.com.aroundu_template_change.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aroundu.snvk.com.aroundu_template_change.R;
import aroundu.snvk.com.aroundu_template_change.vo.IdentifierBusInfo;

public class BottomSheetAdapter extends RecyclerView.Adapter<BottomSheetAdapter.ViewHolder> {
    private ArrayList<IdentifierBusInfo> busList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView busName, busSource, busDestination;

        public ViewHolder(View v){
            super(v);
            busName = (TextView) v.findViewById(R.id.bus_name);
            busSource = (TextView) v.findViewById(R.id.bus_source);
            busDestination = (TextView) v.findViewById(R.id.bus_destination);
        }
    }

    public BottomSheetAdapter(ArrayList<IdentifierBusInfo> busList){
        this.busList = busList;
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
        holder.busName.setText(busInfo.busno);
        holder.busSource.setText(busInfo.sourceLocation);
        holder.busDestination.setText(busInfo.destinationLocation);
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }
}
