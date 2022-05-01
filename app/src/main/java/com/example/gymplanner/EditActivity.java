package com.example.gymplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity implements PlanAdapter.RemovePlan,PlanAdapter.Accomplished {
    private static final String TAG = "";

    @Override
    public void setAccomplished(Plan plan) {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(EditActivity.this);
            SQLiteDatabase sqLiteDatabase=dataBaseHelper.getWritableDatabase();
            dataBaseHelper.setIsAccomplished(sqLiteDatabase,"plans",String.valueOf(plan.getId()));
            sqLiteDatabase.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRemovePlanResult(Plan plan) {
        //Utils.removePlan(plan);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(EditActivity.this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        dataBaseHelper.delete(sqLiteDatabase,"plans",String.valueOf(plan.getId()));
        adapter.setPlans(getPlansByDay(plan.getDay()));

    }

    private TextView txtDay;
    private RecyclerView recyclerView;
    private Button btnAddPlan;
    private PlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initViews();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#00aaff"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new PlanAdapter(this);
        adapter.setType("edit");
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        if (null != intent) {
            String day = intent.getStringExtra("day");
            if (null != day) {
                txtDay.setText(day);
                ArrayList<Plan> plans = getPlansByDay(day);
                adapter.setPlans(plans);
            }
        }
        btnAddPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditActivity.this, AllTrainingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        txtDay = findViewById(R.id.txtDay);
        recyclerView = findViewById(R.id.recyclerView);
        btnAddPlan = findViewById(R.id.btnAddPlan);
    }

    private ArrayList<Plan> getPlansByDay(String day) {
        ArrayList<Plan> plans = new ArrayList<>();
        ArrayList<Plan> allPlans = getAllPlans();
        if (allPlans != null) {
            for (Plan p : allPlans) {
                if (p.getDay() != null) {
                    if (p.getDay().equalsIgnoreCase(day)) {
                        plans.add(p);
                    }
                }
            }
            return plans;
        }
        return null;
    }
        @Override
        public void onBackPressed () {
            Intent intent = new Intent(this, PlanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        @Override
        public boolean onOptionsItemSelected (@NonNull MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    break;
                default:
                    break;
            }
            return super.onOptionsItemSelected(item);
        }
    public ArrayList<Plan> getAllPlans(){
            ArrayList<Plan> plans = new ArrayList<>();
            DataBaseHelper dataBaseHelper = new DataBaseHelper(EditActivity.this);
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM plans",null);
            if(c!=null){
                if(c.moveToFirst()){
                    for(int i =0;i<c.getCount();i++){
                        Plan p = new Plan();
                        int id = c.getInt(c.getColumnIndexOrThrow("id"));
                        int trainingId = c.getInt(c.getColumnIndexOrThrow("trainingId"));
                        Training t = trainingById(trainingId);
                        int minutes = c.getInt(c.getColumnIndexOrThrow("minutes"));
                        String day = c.getString(c.getColumnIndexOrThrow("day"));
                        int isAccomplished = c.getInt(c.getColumnIndexOrThrow("isAccomplished"));
                        p.setAccomplished(isAccomplished);
                        p.setDay(day);
                        p.setTraining(t);
                        p.setMinutes(minutes);
                        p.setId(id);
                        plans.add(p);
                        c.moveToNext();

                    }
                    c.close();
                    dataBaseHelper.close();
                }else{
                    c.close();
                    dataBaseHelper.close();
                }
            }else{
                dataBaseHelper.close();
            }
            return plans;
        }
    public  Training trainingById(int id){
        String ID= String.valueOf(id);  //sql takes commands in strings
        DataBaseHelper dataBaseHelper = new DataBaseHelper(EditActivity.this);
        try{
            SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(" select * from trainings where id="+ID,null);
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    Training t = new Training();
                    int trainingID=cursor.getInt(cursor.getColumnIndex("id"));
                    String name=cursor.getString(cursor.getColumnIndex("name"));
                    String shortDesc=cursor.getString(cursor.getColumnIndex("shortDesc"));
                    String longDesc=cursor.getString(cursor.getColumnIndex("longDesc"));
                    String imageURL=cursor.getString(cursor.getColumnIndex("imageUrl"));
                    t.setImageURL(imageURL);
                    t.setLongDesc(longDesc);
                    t.setName(name);
                    t.setID(trainingID);
                    t.setShortDesc(shortDesc);
                    cursor.close();
                    sqLiteDatabase.close();
                    return t;

                }else{
                    cursor.close();
                    sqLiteDatabase.close();
                }
            }else{
                sqLiteDatabase.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}