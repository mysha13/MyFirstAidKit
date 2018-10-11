package com.example.elasz.myfirstaidkit.Medicaments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by elasz on 11.10.2018.
 */

public class TakeMedItemAdapter extends RecyclerView.Adapter<TakeMedItemAdapter.ViewHolder>{

    private ArrayList<TakeMedItem> takeMeds = new ArrayList<>();
    private RecyclerViewClickListener listener;

    public TakeMedItemAdapter(ArrayList<TakeMedItem> takeMeds, RecyclerViewClickListener listener) {
        this.takeMeds = takeMeds;
        this.listener = listener;
    }

    @Override
    public TakeMedItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.takemedicine_newitem, null);
        return new TakeMedItemAdapter.ViewHolder(itemLayoutView, listener);
    }
    @Override
    public void onBindViewHolder(TakeMedItemAdapter.ViewHolder holder, int position) {
        holder.tv_Id.setText(String.valueOf(takeMeds.get(position).getId()));
        holder.tv_Name.setText(takeMeds.get(position).getName());
        holder.tv_Power.setText(takeMeds.get(position).getPower());
        holder.tv_AmountForm.setText(takeMeds.get(position).getAmoutform());
        holder.tv_AmountForm2.setText(takeMeds.get(position).getAmoutform());
        holder.tv_Amount.setText(String.valueOf(takeMeds.get(position).getAmount()));
        holder.iv_Image.setImageBitmap(takeMeds.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return takeMeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int bNumber = 0;
        RecyclerViewClickListener listener;

        @BindView(R.id.imageView_takenMed_item)
        ImageView iv_Image;

        @BindView(R.id.tv_idtakeMed_item)
        TextView tv_Id;

        @BindView(R.id.tv_takenMed_name_item)
        TextView tv_Name;

        @BindView(R.id.tv_takenMed_power_item)
        TextView tv_Power;

        @BindView(R.id.tv_takenMed_amount_item)
        TextView tv_Amount;

        @BindView(R.id.tv_takenMed_amountform_item)
        TextView tv_AmountForm;

        @BindView(R.id.tv_takenMed_amountForm_item)
        TextView tv_AmountForm2;


        @OnClick(R.id.btn_takeMen_item)
        void TakeMedItem(){
            bNumber = 1;
            onClick(itemView);
        }
        @OnClick(R.id.btn_editamount_item)
        void EditAmountItem(){
            bNumber = 2;
            onClick(itemView);
        }

        @OnClick(R.id.btn_cancelMed_item)
        void CancelItem(){
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
