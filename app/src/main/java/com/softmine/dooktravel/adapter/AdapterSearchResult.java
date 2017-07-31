package com.softmine.dooktravel.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softmine.dooktravel.view.ActivityHome;
import com.softmine.dooktravel.R;
import com.softmine.dooktravel.pojos.Profile;
import com.softmine.dooktravel.util.C;
import com.softmine.dooktravel.util.Utils;

import java.util.List;

/**
 * Created by GAURAV on 7/1/2017.
 */

public class AdapterSearchResult extends RecyclerView
        .Adapter<AdapterSearchResult
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private List<Profile> mDataset;
    private static ClickListener mClickListener;
    static Context context;
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView tvName;
        TextView tvCompany;
        TextView tvAddress;


        public DataObjectHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCompany = (TextView) itemView.findViewById(R.id.tvCompany);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvName.setTypeface(Utils.getSemiBoldTypeFace(context));
            tvCompany.setTypeface(Utils.getRegularTypeFace(context));
            tvAddress.setTypeface(Utils.getRegularTypeFace(context));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onItemClick(getAdapterPosition(), v);
        }


        public void setOnItemClickListener(ClickListener mClick) {
            mClickListener = mClick;
        }
    }
    public AdapterSearchResult(List<Profile> myDataset, Context context) {
        mDataset = myDataset;
        this.context=context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.serach_result_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.tvName.setText(mDataset.get(position).getFirstName()+" "+mDataset.get(position).getLastName());
        holder.tvCompany.setText(mDataset.get(position).getOrganization());
        holder.tvAddress.setText(mDataset.get(position).getStateName()+", "+mDataset.get(position).getCountryName());
        holder.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Bundle bundle=new Bundle();
                bundle.putSerializable(C.DATA,mDataset.get(position));
                bundle.putBoolean(C.IS_SEARCH_RESULT,true);
                ((ActivityHome)context).fragmnetLoader(C.FRAGMENT_PROFILE_DETAIL,bundle);

            }
        });
    }

    public void addItem(Profile dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface ClickListener {
        public void onItemClick(int position, View v);
    }
}
