package com.aniruddhattu.symptomchecker.adapter;

import androidx.annotation.Nullable;

import androidx.recyclerview.selection.ItemDetailsLookup;

import com.aniruddhattu.symptomchecker.data.Symptom;


public class SymptomDetail extends ItemDetailsLookup.ItemDetails {
    private final int adapterPosition;
    private final Symptom selectionKey;

    public SymptomDetail(int adapterPosition, Symptom selectionKey) {
        this.adapterPosition = adapterPosition;
        this.selectionKey = selectionKey;
    }

    @Override
    public int getPosition() {
        return adapterPosition;
    }

    @Nullable
    @Override
    public Object getSelectionKey() {
        return selectionKey;
    }
}
