package com.example.elasz.myfirstaidkit.Medicaments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShortMedInfoItemAdapter extends RecyclerView.Adapter<ShortMedInfoItemAdapter.ViewHolder>{

    private ArrayList<ShortMedInfoItem> shortMeds = new ArrayList<>();
    private RecyclerViewClickListener listener;

    public ShortMedInfoItemAdapter(ArrayList<ShortMedInfoItem> shortMeds, RecyclerViewClickListener listener) {
        this.shortMeds = shortMeds;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_medicine_item, null);
        return new ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(ShortMedInfoItemAdapter.ViewHolder holder, int position) {
        holder.tv_Id.setText(String.valueOf(shortMeds.get(position).getId()));
        holder.tv_Name.setText(shortMeds.get(position).getName());
        holder.tv_EXPDate.setText(shortMeds.get(position).getExpdate());
        holder.tv_Form.setText(shortMeds.get(position).getForm());
        holder.tv_Purpose.setText(shortMeds.get(position).getPurpose());
        holder.tv_Amount.setText(String.valueOf(shortMeds.get(position).getAmount()));
        //holder.tv_Power.setText(userMeds.get(position).getPower());
    }

    @Override
    public int getItemCount() {
        return shortMeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int bNumber = 0;
        RecyclerViewClickListener listener;

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

       /* @BindView(R.id.spin_amountform_allinfo)
        TextView tv_AmountForm;*/

        @BindView(R.id.tv_power_item)
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