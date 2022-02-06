package com.aniruddhattu.symptomchecker.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.aniruddhattu.symptomchecker.data.Symptom;

import java.util.List;

import androidx.recyclerview.selection.ItemKeyProvider;


public class SymptomKeyProvider extends ItemKeyProvider {
    private final List<Symptom> symptomsList;

    public SymptomKeyProvider(int scope, List<Symptom> symptomsList) {
        super(scope);
        this.symptomsList = symptomsList;
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return symptomsList.get(position);
    }

    @Override
    public int getPosition(@NonNull Object key) {
        return symptomsList.indexOf(key);
    }
}
