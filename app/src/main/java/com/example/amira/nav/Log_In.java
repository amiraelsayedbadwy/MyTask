package com.example.amira.nav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Log_In extends AppCompatActivity {

    EditText enter;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);
        enter=(EditText)findViewById(R.id.editTextPassword);
        login=(Button)findViewById(R.id.button);
    }
    public void OnLogin(View view) {

        String password = enter.getText().toString();
        String type = "login";
        server backgroundWorker = new server(this);
        backgroundWorker.execute(type, password);
    }
}
