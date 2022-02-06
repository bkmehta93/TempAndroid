package com.aniruddhattu.symptomchecker.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;

import com.aniruddhattu.symptomchecker.R;
import com.aniruddhattu.symptomchecker.data.Symptom;


public class SymptomsListAdapter extends RecyclerView.Adapter<SymptomsListAdapter.SymptomsListViewHolder> {

    private final List<Symptom> symptomsList;
    private SelectionTracker selectionTracker;

    public SymptomsListAdapter(List<Symptom> symptomsList) {
        this.symptomsList = symptomsList;
    }

    public SelectionTracker getSelectionTracker() {
        return selectionTracker;
    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }

    @NonNull
    @Override
    public SymptomsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_symptom, parent, false);
        return new SymptomsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SymptomsListViewHolder holder, int position) {
        Symptom symptom = symptomsList.get(position);

        holder.bind(symptom, selectionTracker.isSelected(symptom));

    }

    @Override
    public int getItemCount() {
        return symptomsList.size();
    }

    public class SymptomsListViewHolder extends RecyclerView.ViewHolder implements SymptomsViewHolderWithDetails {
        TextView symptomValue;

        public SymptomsListViewHolder(@NonNull View itemView) {
            super(itemView);
            symptomValue = itemView.findViewById(R.id.tvSymptom);
        }

        public final void bind(Symptom symptom, boolean isActive) {
            Log.e("BIND", isActive+"");
            itemView.setActivated(isActive);
            symptomValue.setText(symptom.getValue());
        }

        @Override
        public ItemDetailsLookup.ItemDetails getItemDetails() {
            return new SymptomDetail(getAdapterPosition(), symptomsList.get(getAdapterPosition()));
        }
    }
}
