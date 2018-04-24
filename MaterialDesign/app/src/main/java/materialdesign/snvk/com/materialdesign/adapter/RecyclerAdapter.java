package materialdesign.snvk.com.materialdesign.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import materialdesign.snvk.com.materialdesign.R;
import materialdesign.snvk.com.materialdesign.model.Landscape;

/**
 * Created by Venkata on 3/16/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static final String TAG = "RecyclerView";
    private List<Landscape> mData;
    private LayoutInflater mInflater;

    public RecyclerAdapter(Context context, List<Landscape> data) {
        Log.d(TAG, "Adapter constructor");
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
        Log.d(TAG, "Adapter end constructor");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "OnCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        //View view = mInflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder" + position);
        Landscape currentObj = mData.get(position);
        holder.setData(currentObj, position);

    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumb, imgDelete, imgAdd;
        int position;
        Landscape current;

        public MyViewHolder(View itemView) {
            super(itemView);

            //imgThumb = (ImageView) itemView.findViewById(R.id.first);
            //imgDelete = (ImageView) itemView.findViewById(R.id.second);
            //imgAdd = (ImageView) itemView.findViewById(R.id.third);
        }

        public void setData(Landscape current, int position) {
            //this.imgThumb.setImageResource(current.getImageID());
            this.position = position;
            this.current = current;
        }
    }
}
