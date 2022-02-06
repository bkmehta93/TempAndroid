package com.aniruddhattu.symptomchecker.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aniruddhattu.symptomchecker.R;
import com.aniruddhattu.symptomchecker.adapter.ActionModeController;
import com.aniruddhattu.symptomchecker.adapter.SymptomKeyProvider;
import com.aniruddhattu.symptomchecker.adapter.SymptomLookup;
import com.aniruddhattu.symptomchecker.adapter.SymptomsListAdapter;
import com.aniruddhattu.symptomchecker.data.Globals;
import com.aniruddhattu.symptomchecker.data.Symptom;
import com.aniruddhattu.symptomchecker.helpers.AppController;
import com.aniruddhattu.symptomchecker.helpers.HelperFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoreSymptomsActivity extends AppCompatActivity {

    private RecyclerView rvSymptoms;
    private RecyclerView.LayoutManager mLayoutManager;
    private SymptomsListAdapter mAdapter;
    private List<Symptom> symptomsList;
    private static final String TAG = "MoreSymptomsActivity";
    private SelectionTracker selectionTracker;
    private ActionMode actionMode;
    private Bundle mSavedInstance;
    private static String params,additionalSymptom;
    private static String queryParams;
    private ProgressBar progressBar;
    private EditText etAddSymptom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);
        queryParams = getIntent().getStringExtra(Globals.EXTRA_PARAMS);
        initControls(savedInstanceState);
        getSymptoms();

    }

    private void initControls(Bundle savedInstanceState) {
        //List Stuff
        symptomsList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(this);

        etAddSymptom = (EditText) findViewById(R.id.etAddSymptom);
        progressBar = (ProgressBar) findViewById(R.id.pbLoading);
        rvSymptoms = (RecyclerView) findViewById(R.id.rvSymptoms);
        rvSymptoms.setLayoutManager(mLayoutManager);
        rvSymptoms.setItemAnimator(new DefaultItemAnimator());
        rvSymptoms.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mSavedInstance = savedInstanceState;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        selectionTracker.onSaveInstanceState(outState);
    }

    private void getSymptoms() {
        symptomsList = new ArrayList<>();
        showProgressBar();
        String url = "https://schecker.azurewebsites.net/api/getSymptoms/" + queryParams;
        Log.e(TAG, url);
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Iterator iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String id = (String) iter.next();
                        String value = jsonObject.getString(id);
                        symptomsList.add(new Symptom(Integer.parseInt(id), value));
                    }
                    Log.e("Added", symptomsList.size() + "");
                    updateList();
                    hideProgressBar();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                try {
                    Log.e("Error: ", error.getMessage());


                } catch (Exception e) {

                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "symptoms_request");
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void updateList() {
        mAdapter = new SymptomsListAdapter(symptomsList);
        rvSymptoms.setAdapter(mAdapter);
        selectionTracker = new SelectionTracker.Builder<>(
                "my-selection-id",
                rvSymptoms,
                new SymptomKeyProvider(1, symptomsList),
                new SymptomLookup(rvSymptoms),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build();
        mAdapter.setSelectionTracker(selectionTracker);

        selectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onItemStateChanged(@NonNull Object key, boolean selected) {
                Log.e("SELECTED", selected + "");
                super.onItemStateChanged(key, selected);
            }

            @Override
            public void onSelectionRefresh() {
                super.onSelectionRefresh();
            }

            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (selectionTracker.hasSelection() && actionMode == null) {
                    actionMode = startSupportActionMode(new ActionModeController(MoreSymptomsActivity.this, selectionTracker));
                    //DO SOMETHING HERE
                } else if (!selectionTracker.hasSelection() && actionMode != null) {
                    actionMode.finish();
                    actionMode = null;
                } else {
                    //DO SOMETHING HERE
                }
                Iterator<Symptom> itemIterable = selectionTracker.getSelection().iterator();
                String str = "";
                while (itemIterable.hasNext()) {
                    str += itemIterable.next().getId() + ",";
                }
                params = str;
            }

            @Override
            public void onSelectionRestored() {
                super.onSelectionRestored();
            }
        });

        if (mSavedInstance != null) {
            selectionTracker.onRestoreInstanceState(mSavedInstance);
        }
        mAdapter.notifyDataSetChanged();
    }


    public void nextStep(View view) {
        Intent i = new Intent(MoreSymptomsActivity.this, ConditionActivity.class);
        i.putExtra(Globals.EXTRA_PARAMS, params.substring(0, params.length() - 1));
        startActivity(i);
    }

    //Method to be updated to make API call before updating the list
    public void addSymptom(View view) {
        if(!validateSymptom()){
            return;
        }
        Symptom newSymptom = new Symptom(99, additionalSymptom);
        symptomsList.add(0, newSymptom);
        rvSymptoms.setAdapter(mAdapter);
        mAdapter.notifyItemInserted(0);
        mAdapter.getSelectionTracker().select(newSymptom);
        etAddSymptom.setText("");
        additionalSymptom="";
    }

    private boolean validateSymptom() {
        additionalSymptom = etAddSymptom.getText().toString().trim();
        if (additionalSymptom.isEmpty()) {
            Toast.makeText(MoreSymptomsActivity.this, R.string.error_symptom_empty, Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (HelperFunctions.containsDigit(additionalSymptom)) {
                Toast.makeText(MoreSymptomsActivity.this, R.string.error_symptom_contains_numbers, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}