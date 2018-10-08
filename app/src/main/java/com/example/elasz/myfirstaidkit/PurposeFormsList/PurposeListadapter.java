package com.example.elasz.myfirstaidkit.PurposeFormsList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class PurposeListadapter extends RecyclerView.Adapter<PurposeListadapter.ViewHolder>{

    private ArrayList<PurposeList> purposeLists = new ArrayList<>();
    public RecyclerViewClickListener listener;

    public PurposeListadapter(ArrayList<PurposeList> purposeLists, RecyclerViewClickListener listener) {
        this.purposeLists = purposeLists;
        this.listener = listener;
    }
    @Override
    public PurposeListadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.editpurposelist_item, null);
        return new PurposeListadapter.ViewHolder(itemLayoutView, listener);
    }
    @Override
    public void onBindViewHolder(PurposeListadapter.ViewHolder holder, int position) {
        holder.tvIdPurpose.setText(String.valueOf(purposeLists.get(position).getId()));
        holder.tvPurposeName.setText(purposeLists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return purposeLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RecyclerViewClickListener listener;
        int bNumber = 0;

        @BindView(R.id.tv_idpurpose)
        TextView tvIdPurpose;

        @BindView(R.id.tv_purposename)
        TextView tvPurposeName;

        @OnClick(R.id.btn_delete_editpurposelist)
        void delete(){
            bNumber = 1;
            onClick(itemView);
        }

        ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), tvIdPurpose.getText().toString(), bNumber);
        }
    }
}
