package aroundu.snvk.com.aroundu_template_change.adapters;

import android.support.v7.widget.RecyclerView;
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
            destination = (TextView) v.findViewById(R.id.destination);
            mListener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(destinationList.get(getAdapterPosition()));
        }
    }

    public SearchViewAdapter(ArrayList<String> destinationList, RecyclerViewClickListener listener){
        this.destinationList = destinationList;
        this.mListener = listener;
    }

    @Override
    public SearchViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.destination_list_row, parent, false);

        return new SearchViewAdapter.ViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(SearchViewAdapter.ViewHolder holder, int position) {
        String destination = destinationList.get(position);
        holder.destination.setText(destination);
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }
}
