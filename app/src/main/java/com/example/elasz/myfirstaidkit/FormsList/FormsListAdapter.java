package com.example.elasz.myfirstaidkit.FormsList;

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
 * Created by elasz on 30.09.2018.
 */

public class FormsListAdapter extends RecyclerView.Adapter<FormsListAdapter.ViewHolder>{

    private ArrayList<FormsList> formsLists = new ArrayList<>();
    public RecyclerViewClickListener listener;

    public FormsListAdapter(ArrayList<FormsList> formsLists, RecyclerViewClickListener listener) {
        this.formsLists = formsLists;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.editformslist_item, null);
        return new ViewHolder(itemLayoutView, listener);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvIdForm.setText(String.valueOf(formsLists.get(position).getId()));
        holder.tvFormName.setText(formsLists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return formsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RecyclerViewClickListener listener;
        int bNumber = 0;

        @BindView(R.id.tv_idform)
        TextView tvIdForm;

        @BindView(R.id.tv_formname)
        TextView tvFormName;

        /*@OnClick(R.id.btn_delete_editformslist)
        void go(){
            bNumber = 1;
            onClick(itemView);
        }*/
        @OnClick(R.id.btn_delete_editformslist)
        void delete(){
            bNumber = 2;
            onClick(itemView);
        }
        /*@OnClick(R.id.bGoToContent)
        void content(){
            bNumber = 3;
            onClick(itemView);
        }*/

        ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), tvIdForm.getText().toString(), bNumber);
        }
    }
}
