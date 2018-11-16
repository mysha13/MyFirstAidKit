package com.example.elasz.myfirstaidkit.Medicaments;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by elasz on 19.09.2018.
 */

public class ShortMedInfoItemAdapter extends RecyclerView.Adapter<ShortMedInfoItemAdapter.ViewHolder>  {

    private ArrayList<ShortMedInfoItem> shortMeds = new ArrayList<>();
    private ArrayList<ShortMedInfoItem> filteredList;
    private RecyclerViewClickListener listener;
    //private CustomFilter mFilter;

    public ShortMedInfoItemAdapter(ArrayList<ShortMedInfoItem> shortMeds, RecyclerViewClickListener listener) {
        this.shortMeds = shortMeds;
        this.listener = listener;
        filteredList = shortMeds;
       // mFilter = new CustomFilter(ShortMedInfoItemAdapter.this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_recycler_onemedicine, null);
        return new ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(ShortMedInfoItemAdapter.ViewHolder holder, int position) {
        holder.tv_Id.setText(String.valueOf(filteredList.get(position).getId()));
        holder.tv_Name.setText(filteredList.get(position).getName());
        holder.tv_EXPDate.setText(filteredList.get(position).getExpdate());
        holder.tv_Form.setText(filteredList.get(position).getForm());
        holder.tv_Purpose.setText(filteredList.get(position).getPurpose());
        holder.tv_Amount.setText(String.valueOf(filteredList.get(position).getAmount()));
        holder.tv_AmountForm.setText(String.valueOf(filteredList.get(position).getAmoutform()));
        holder.iv_Image.setImageBitmap(filteredList.get(position).getImage());
        holder.tv_Power.setText(filteredList.get(position).getPower());
        holder.tv_Code.setText(filteredList.get(position).getCode());
    }

    @Override
    public int getItemCount() {
        //return shortMeds.size();
        return filteredList.size();
    }

    public void filter(String query){

        filteredList = new ArrayList<>();
        for (ShortMedInfoItem shortMed : shortMeds) {

            if(shortMed.name.toLowerCase().contains(query.toLowerCase())){
                filteredList.add(shortMed);
            }
            /*if (shortMed.code.toLowerCase().contains(query.toLowerCase())){
                filteredList.add(shortMed);
            }*/
        }
        notifyDataSetChanged();
    }
    public void filterCode(String query){

        filteredList = new ArrayList<>();
        for (ShortMedInfoItem shortMed : shortMeds) {

            if (shortMed.code.toLowerCase().contains(query.toLowerCase())){
                filteredList.add(shortMed);
            }
        }
        notifyDataSetChanged();
    }

    public void filterDate(String query){
        filteredList = new ArrayList<>();
        for (ShortMedInfoItem shortMed : shortMeds) {

            if(shortMed.expdate.toLowerCase().contains(query.toLowerCase())){
                filteredList.add(shortMed);
            } /*else if (shortMed.code.toLowerCase().contains(query.toLowerCase())){
                filteredList.add(shortMed);
            }*/
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int bNumber = 0;
        RecyclerViewClickListener listener;

        @BindView(R.id.imageView_oneMedicineItem)
        ImageView iv_Image;

        @BindView(R.id.tv_id_item)
        TextView tv_Id;

        @BindView(R.id.tv_name_item)
        TextView tv_Name;

        @BindView(R.id.tv_expdate_item)
        TextView tv_EXPDate;

        @BindView(R.id.tv_form_item)
        TextView tv_Form;

        @BindView(R.id.tv_purpose_item)
        TextView tv_Purpose;

        @BindView(R.id.tv_amount_item)
        TextView tv_Amount;

        @BindView(R.id.tv_code_item)
        TextView tv_Code;

        @BindView(R.id.tv_amountform_item)
        TextView tv_AmountForm;

        @BindView(R.id.tv_poweronemed_item)
        TextView tv_Power;

        @OnClick(R.id.btn_update_item)
        void UpdateItem(){
            bNumber = 1;
            onClick(itemView);
        }
        @OnClick(R.id.btn_delete_item)
        void DeleteItem(){
            bNumber = 2;
            onClick(itemView);
        }

        @OnClick(R.id.btn_moreInfo_item)
        void MoreInfoItem(){
            bNumber = 3;
            onClick(itemView);
        }


        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), tv_Id.getText().toString(),bNumber);
        }
    }


}