package com.aniruddhattu.symptomchecker.activities;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aniruddhattu.symptomchecker.R;
import com.aniruddhattu.symptomchecker.data.Globals;
import com.aniruddhattu.symptomchecker.helpers.HelperFunctions;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etAge;
    private RadioButton rbMale, rbFemale, rbTransgender;
    private Context mContext;
    private RadioGroup rgGender;
    private static String name, gender;
    private static int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
    }

    private void initControls() {
        mContext = MainActivity.this;
        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        rbMale = (RadioButton) findViewById(R.id.rbImmediately);
        rbFemale = (RadioButton) findViewById(R.id.rbAfterFewDays);
        rbTransgender = (RadioButton) findViewById(R.id.rbNever);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);

    }

    public void nextStep(View view) {
        /*if(!validateName() || !validateAge() || !validateGender()) {
            return;
        }*/

        Intent i = new Intent(MainActivity.this, SymptomsActivity.class);
        i.putExtra(Globals.EXTRA_NAME, name);
        i.putExtra(Globals.EXTRA_AGE, age);
        i.putExtra(Globals.EXTRA_GENDER, gender);
        startActivity(i);
    }

    private boolean validateAge() {
        if(!etAge.getText().toString().trim().isEmpty()) {
            age = Integer.parseInt(etAge.getText().toString().trim());
            if (age > 0) {
                return true;
            } else {
                Toast.makeText(mContext, R.string.error_age_invalid, Toast.LENGTH_LONG).show();
                return false;
            }
        } else{
            Toast.makeText(mContext, R.string.error_age_empty, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean validateGender() {
        if (rgGender.getCheckedRadioButtonId() == -1) {
            Toast.makeText(mContext, R.string.error_gender_unchecked, Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (rbMale.isChecked()) {
                gender = Globals.VALUE_GENDER_MALE;
            } else if (rbFemale.isChecked()) {
                gender = Globals.VALUE_GENDER_FEMALE;
            } else {
                gender = Globals.VALUE_GENDER_TRANSGENDER;
            }
            return true;
        }
    }

    private boolean validateName() {
        name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(mContext, R.string.error_name_empty, Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (HelperFunctions.containsDigit(name)) {
                Toast.makeText(mContext, R.string.error_name_contains_numbers, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }
}