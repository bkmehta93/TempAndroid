package com.aniruddhattu.symptomchecker.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aniruddhattu.symptomchecker.R;
import com.aniruddhattu.symptomchecker.helpers.AppController;

public class ConditionActivity extends AppCompatActivity {

    static String params = "";
    ImageView ivCondition;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
        params = getIntent().getStringExtra("params");
        Log.e("PARAMS", params);
        initControls();
        getCondition();
    }

    private void initControls() {
        ivCondition = (ImageView) findViewById(R.id.ivCondition);
        progressBar = (ProgressBar) findViewById(R.id.pbLoading);
    }

    public void getCondition() {
        showProgressBar();
        String url = "https://schecker.azurewebsites.net/api/getCondition/" + params;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();
                Log.e("Response: ", response);
                switch (response) {
                    case "Red":
                        ivCondition.setVisibility(View.VISIBLE);
                        ivCondition.setImageResource(R.drawable.circle_red);
                        break;
                    case "Green":
                        ivCondition.setVisibility(View.VISIBLE);
                        ivCondition.setImageResource(R.drawable.circle_green);
                        break;
                    case "Yellow":
                        ivCondition.setVisibility(View.VISIBLE);
                        ivCondition.setImageResource(R.drawable.circle_yellow);
                        break;
                    default:
                        Toast.makeText(ConditionActivity.this, "Undefined Condition", Toast.LENGTH_LONG).show();

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
        AppController.getInstance().addToRequestQueue(strReq, "conditions_request");
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}