package com.example.elasz.myfirstaidkit.Medicaments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.elasz.myfirstaidkit.Interfaces.RecyclerViewClickListener;
import com.example.elasz.myfirstaidkit.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by elasz on 17.09.2018.
 */

public class AllMedInfoItemAdapter extends RecyclerView.Adapter<AllMedInfoItemAdapter.ViewHolder>{

    private ArrayList<AllMedInfoItem> userMeds = new ArrayList<>();
    private RecyclerViewClickListener listener;

    public AllMedInfoItemAdapter(ArrayList<AllMedInfoItem> userMeds, RecyclerViewClickListener listener) {
        this.userMeds = userMeds;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_view_all_info_medicine, null);
        return new ViewHolder(itemLayoutView, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_Id.setText(String.valueOf(userMeds.get(position).getId()));
        holder.tv_Name.setText(userMeds.get(position).getName());
        holder.tv_EXPDate.setText(userMeds.get(position).getExpdate());
        holder.tv_OpenDate.setText(userMeds.get(position).getOpendate());
        holder.tv_Form.setText(userMeds.get(position).getForm());
        holder.tv_Purpose.setText(userMeds.get(position).getPurpose());
        holder.tv_Amount.setText(String.valueOf(userMeds.get(position).getAmount()));
        holder.tv_AmountForm.setText(userMeds.get(position).getAmoutform());
        holder.tv_Person.setText(userMeds.get(position).getAmoutform());
        //holder.tv_Power.setText(userMeds.get(position).getPower());
        //holder.tv_SubsActive.setText(userMeds.get(position).getSubsActive());
        //holder.tv_Code.setText(userMeds.get(position).getCode());
       // holder.tv_Producer.setText(userMeds.get(position).getProducer());
        holder.tv_Note.setText(userMeds.get(position).getNote());

    }

    @Override
    public int getItemCount() {
        return userMeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int bNumber = 0;
        RecyclerViewClickListener listener;

        @BindView(R.id.tv_id_allinfo)
        TextView tv_Id;

        @BindView(R.id.et_name_allinfo)
        TextView tv_Name;

        @BindView(R.id.tv_expdate_allinfo)
        TextView tv_EXPDate;

        @BindView(R.id.tv_expdate_item)
        TextView tv_OpenDate;

        @BindView(R.id.spin_form_allinfo)
        TextView tv_Form;

        @BindView(R.id.spin_purpose_allinfo)
        TextView tv_Purpose;

        @BindView(R.id.et_amount_allinfo)
        TextView tv_Amount;

        @BindView(R.id.spin_amountform_allinfo)
        TextView tv_AmountForm;

        @BindView(R.id.spin_person_allinfo)
        TextView tv_Person;

        @BindView(R.id.et_power_allinfo)
        TextView tv_Power;

        @BindView(R.id.et_subsActive_allinfo)
        TextView tv_SubsActive;

        @BindView(R.id.et_code_allinfo)
        TextView tv_Code;

        @BindView(R.id.et_producer_allinfo)
        TextView tv_Producer;

        @BindView(R.id.et_note_allinfo)
        TextView tv_Note;



        public ViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.listener = listener;
            // goToUpdate.setOnClickListener(this);
            //deleteMedFromListDialog.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition(), tv_Id.getText().toString(),bNumber);
        }
    }
}
