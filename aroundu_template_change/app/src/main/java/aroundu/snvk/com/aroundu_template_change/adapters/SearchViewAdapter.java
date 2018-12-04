package aroundu.snvk.com.aroundu_template_change.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aroundu.snvk.com.aroundu_template_change.R;
import aroundu.snvk.com.aroundu_template_change.interfaces.RecyclerViewClickListener;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewAdapter.ViewHolder> {
    private ArrayList<String> destinationList;
    private RecyclerViewClickListener mListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView destination;
        private RecyclerViewClickListener mListener;

        public ViewHolder(View v, RecyclerViewClickListener listener){
            super(v);
            Log.d("SearchViewAdapter", "In ViewHolder");
            destination = (TextView) v.findViewById(R.id.destination);
            mListener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("SearchViewAdapter", "In onClick");
            mListener.onDestinationSearchClick(destinationList.get(getAdapterPosition()));
        }
    }

    public SearchViewAdapter(ArrayList<String> destinationList, RecyclerViewClickListener listener){
        Log.d("SearchViewAdapter", "In SearchViewAdapter Constructor");
        this.destinationList = destinationList;
        this.mListener = listener;
    }

    @Override
    public SearchViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destination_list_row, parent, false);
        Log.d("SearchViewAdapter", "In onCreateViewHolder");
        return new SearchViewAdapter.ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(SearchViewAdapter.ViewHolder holder, int position) {
        String destination = destinationList.get(position);
        holder.destination.setText(destination);
        Log.d("SearchViewAdapter", "In onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public void updateData(ArrayList<String> list) {
        destinationList = list;
    }
}
