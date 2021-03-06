package com.example.chriseze.cernlib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chriseze.cernlib.Models.StudentModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.chriseze.cernlib.Models.URLs.URL_STUDENTS_SIGNUP;

public class AddStudent extends AppCompatActivity {
    private EditText mEdName, mEdRegno, mEdPass, mEdLevel, mEdDept;
    private Button mBtnSave;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        mToolbar = (Toolbar)findViewById(R.id.custom_toolbar);
        setSupportActionBar(mToolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Cern - Add Student");

        mEdPass = (EditText)findViewById(R.id.password);
        mEdRegno = (EditText)findViewById(R.id.regno);
        mEdDept = (EditText)findViewById(R.id.department);
        mEdLevel = (EditText)findViewById(R.id.level);
        mEdName = (EditText)findViewById(R.id.name);
        mBtnSave = (Button)findViewById(R.id.save);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regno = mEdRegno.getText().toString();
                String pass = mEdPass.getText().toString();
                String dept = mEdDept.getText().toString();
                String level = mEdLevel.getText().toString();
                String name = mEdName.getText().toString();
                addStudentAction(regno, pass, dept, level, name);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addStudentAction(final String regno, final String password, final String dept, final String level, final String name){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_STUDENTS_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject student = jsonObject.getJSONObject("student");

                            StudentModel model = new StudentModel(student.getString("name"),
                                    student.getString("regno"),
                                    student.getString("department"),
                                    student.getString("level"),
                                    student.getString("_id"));
                            UserInfo userInfo = new UserInfo(getApplicationContext());
                            userInfo.setUser(model);


                            startActivity(new Intent(getApplicationContext(), Students.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("regno", regno);
                map.put("department", dept);
                map.put("level", level);
                map.put("password", password);
                return map;
            }
        } ;
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
