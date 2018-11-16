package com.example.elasz.myfirstaidkit.PersonList;

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
 * Created by elasz on 04.10.2018.
 */

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.ViewHolder> {

    private ArrayList<PersonList> personLists = new ArrayList<>();
    public RecyclerViewClickListener listener;

    public PersonListAdapter(ArrayList<PersonList> personLists, RecyclerViewClickListener listener) {
        this.personLists = personLists;
        this.listener = listener;
    }

    @Override
    public PersonListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.editlist_item, null);
        return new PersonListAdapter.ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(PersonListAdapter.ViewHolder holder, int position) {
        holder.tvIdPerson.setText(String.valueOf(personLists.get(position).getId()));
        holder.tvPersonName.setText(personLists.get(position).getName());
    }
    @Override
    public int getItemCount() {
        return personLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RecyclerViewClickListener listener;
        int bNumber = 0;

        @BindView(R.id.tv_id)
        TextView tvIdPerson;

        @BindView(R.id.tv_name)
        TextView tvPersonName;

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
            listener.onClick(view, getAdapterPosition(), tvIdPerson.getText().toString(), bNumber);
        }
    }
}
