package com.example.prj_2_1602414;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText sPoint;
    EditText ePoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sPoint = findViewById(R.id.spoint);
        ePoint = findViewById(R.id.epoint);
    }
    public void change(View view){
        String Startp = sPoint.getText().toString();
        String Endp = ePoint.getText().toString();
        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
        intent.putExtra("starting", Startp);
        intent.putExtra("ending", Endp);
        startActivity(intent);
    }
}