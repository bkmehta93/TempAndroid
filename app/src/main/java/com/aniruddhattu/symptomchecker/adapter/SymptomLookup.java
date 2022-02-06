package com.aniruddhattu.symptomchecker.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.selection.ItemDetailsLookup;

public class SymptomLookup extends ItemDetailsLookup {

    private final RecyclerView recyclerView;

    public SymptomLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof SymptomsListAdapter.SymptomsListViewHolder) {
                return ((SymptomsListAdapter.SymptomsListViewHolder) viewHolder).getItemDetails();
            }
        }

        return null;
    }
}