package com.example.elasz.myfirstaidkit.AmountFormsList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elasz.myfirstaidkit.FormsList.FormsList;
import com.example.elasz.myfirstaidkit.FormsList.FormsListAdapter;
import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by elasz on 30.09.2018.
 */

public class AmountFormsListAdapter extends RecyclerView.Adapter<AmountFormsListAdapter.ViewHolder> {

    private ArrayList<AmountFormsList> amountFormsLists = new ArrayList<>();
    public RecyclerViewClickListener listener;

    public AmountFormsListAdapter(ArrayList<AmountFormsList> amountFormsLists, RecyclerViewClickListener listener) {
        this.amountFormsLists = amountFormsLists;
        this.listener = listener;
    }

    @Override
    public AmountFormsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.editlist_item, null);
        return new AmountFormsListAdapter.ViewHolder(itemLayoutView, listener);
    }
    @Override
    public void onBindViewHolder(AmountFormsListAdapter.ViewHolder holder, int position) {
        holder.tvIdAmountForm.setText(String.valueOf(amountFormsLists.get(position).getId()));
        holder.tvAmountFormName.setText(amountFormsLists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return amountFormsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RecyclerViewClickListener listener;
        int bNumber = 0;

        @BindView(R.id.tv_id)
        TextView tvIdAmountForm;

        @BindView(R.id.tv_name)
        TextView tvAmountFormName;

        @OnClick(R.id.btn_delete)
        void delete(){
            bNumber = 2;
            onClick(itemView);
        }

        ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), tvIdAmountForm.getText().toString(), bNumber);
        }
    }
}
