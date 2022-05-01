package com.example.gymplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PlanActivity extends AppCompatActivity {
    private static final String TAG = "PlanActivity";
    private TextView mondayEdit, tuesdayEdit, wednesdayEdit, thursdayEdit, fridayEdit, saturdayEdit, sundayEdit;
    private RecyclerView mondayRecView, tuesdayRecView, wednesdayRecView, thursdayRecView,
            fridayRecView, saturdayRecView, sundayRecView;
    private RelativeLayout noPlanRelLayout;
    private NestedScrollView nestedScrollView;
    private Button btnAddPlan;

    private PlanAdapter mondayAdapter, tuesdayAdapter, wednesdayAdapter, thursdayAdapter, fridayAdapter, saturdayAdapter, sundayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        initViews();
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#00aaff"));
        actionBar.setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setEditOnClickListeners();
        ArrayList<Plan> plans = new ArrayList<>();
        new GetAllPlans().execute();
    }

    private void initViews() {
        Log.d(TAG, "initViews: started");
        mondayEdit = findViewById(R.id.mondayEdit);
        tuesdayEdit = findViewById(R.id.tuesdayEdit);
        wednesdayEdit = findViewById(R.id.wednesdayEdit);
        thursdayEdit = findViewById(R.id.thursdayEdit);
        fridayEdit = findViewById(R.id.fridayEdit);
        saturdayEdit = findViewById(R.id.saturdayEdit);
        sundayEdit = findViewById(R.id.sundayEdit);
        mondayRecView = findViewById(R.id.mondayRecView);
        tuesdayRecView = findViewById(R.id.tuesdayRecView);
        wednesdayRecView = findViewById(R.id.wednesdayRecView);
        thursdayRecView = findViewById(R.id.thursdayRecView);
        fridayRecView = findViewById(R.id.fridayRecView);
        saturdayRecView = findViewById(R.id.saturdayRecView);
        sundayRecView = findViewById(R.id.sundayRecView);
        noPlanRelLayout = findViewById(R.id.noPlanRelLayout);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        btnAddPlan = findViewById(R.id.btnAddPlan);
    }

    private void initRecViews() {




    }

    private ArrayList<Plan> getPlansByDay(String day) {
        ArrayList<Plan> plans = new ArrayList<>();
        ArrayList<Plan> allPlans = getAllPlans();
        if(allPlans!=null){
            for (Plan p : allPlans) {
                if(p.getDay()!=null) {
                    if (p.getDay().equalsIgnoreCase(day)) {
                        plans.add(p);
                    }
                }else{
                    Log.d(TAG, "getPlansByDay: day is null");
                }
            }
        }else{
            Log.d(TAG, "getPlansByDay: plans are null");
        }
        return plans;
    }
    private ArrayList<Plan> getAllPlans(){
        ArrayList<Plan> plans = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(PlanActivity.this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM plans",null);
       // Cursor cursor = sqLiteDatabase.query("plans",null,null,null,null,null,null);
        if(c!=null){
            if(c.moveToFirst()){
                for(int i =0;i<c.getCount();i++){
                    Plan p = new Plan();
                    int id = c.getInt(c.getColumnIndexOrThrow("id"));
                    int trainingId = c.getInt(c.getColumnIndexOrThrow("trainingId"));
                    Training t = trainingById(trainingId);
                    Log.d(TAG, "getAllPlans: training id"+t.getID());
                    Log.d(TAG, "getAllPlans: name"+t.getName());
                    int minutes = c.getInt(c.getColumnIndexOrThrow("minutes"));
                    String day = c.getString(c.getColumnIndexOrThrow("day"));
                    int isAccomplished = c.getInt(c.getColumnIndexOrThrow("isAccomplished"));
                    p.setAccomplished(isAccomplished);
                    p.setId(id);
                    p.setDay(day);
                    p.setTraining(t);
                    p.setMinutes(minutes);
                    plans.add(p);
                    c.moveToNext();

                }
                c.close();
                sqLiteDatabase.close();
            }else{
                c.close();
                sqLiteDatabase.close();
            }
        }else{
            sqLiteDatabase.close();
        }
        return plans;
    }
    private void setEditOnClickListeners() {
        final Intent intent = new Intent(this, EditActivity.class);
        mondayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("day", "Monday");
                startActivity(intent);
            }
        });

        tuesdayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("day", "Tuesday");
                startActivity(intent);
            }
        });

        wednesdayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("day", "Wednesday");
                startActivity(intent);
            }
        });

        thursdayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("day", "Thursday");
                startActivity(intent);
            }
        });

        fridayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("day", "Friday");
                startActivity(intent);
            }
        });

        saturdayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("day", "Saturday");
                startActivity(intent);
            }
        });

        sundayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("day", "Sunday");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public  Training trainingById(int id){
        String ID= String.valueOf(id);  //sql takes commands in strings
        DataBaseHelper dataBaseHelper = new DataBaseHelper(PlanActivity.this);
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

    private class GetAllPlans extends AsyncTask<Void,Void,ArrayList<Plan>>{
        private DataBaseHelper dataBaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataBaseHelper = new DataBaseHelper(PlanActivity.this);
        }

        @Override
        protected ArrayList<Plan> doInBackground(Void... voids) {
            try{
                SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("plans",null,null,null,null,null,null);
                if(null!=cursor){
                    if(cursor.moveToFirst()){
                        ArrayList<Plan> plans = new ArrayList<>();
                        int trainingIdIndex = cursor.getColumnIndex("trainingId");
                        int minutesIndex = cursor.getColumnIndex("minutes");
                        int dayIndex = cursor.getColumnIndex("day");
                        int isAccomplishedIndex = cursor.getColumnIndex("isAccomplished");
                        for(int i=0;i<cursor.getCount();i++){
                        Plan p = new Plan();
                        Training t = trainingById(trainingIdIndex);
                        p.setTraining(t);
                        p.setAccomplished(cursor.getInt(isAccomplishedIndex));
                        p.setDay(cursor.getString(dayIndex));
                        p.setMinutes(cursor.getInt(minutesIndex));
                        plans.add(p);
                        cursor.moveToNext();
                        }
                        cursor.close();
                        db.close();
                        return plans;
                    }else{
                        cursor.close();
                        db.close();
                    }
                }else{
                    db.close();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Plan> plans) {
            super.onPostExecute(plans);
            if(null!=plans) {
                mondayAdapter = new PlanAdapter(PlanActivity.this);
                mondayAdapter.setPlans(getPlansByDay("Monday"));
                mondayRecView.setAdapter(mondayAdapter);
                mondayRecView.setLayoutManager(new LinearLayoutManager(PlanActivity.this, RecyclerView.HORIZONTAL, false));

                tuesdayAdapter = new PlanAdapter(PlanActivity.this);
                tuesdayAdapter.setPlans(getPlansByDay("Tuesday"));
                tuesdayRecView.setAdapter(tuesdayAdapter);
                tuesdayRecView.setLayoutManager(new LinearLayoutManager(PlanActivity.this, RecyclerView.HORIZONTAL, false));


                wednesdayAdapter = new PlanAdapter(PlanActivity.this);
                wednesdayAdapter.setPlans(getPlansByDay("Wednesday"));
                wednesdayRecView.setAdapter(wednesdayAdapter);
                wednesdayRecView.setLayoutManager(new LinearLayoutManager(PlanActivity.this, RecyclerView.HORIZONTAL, false));

                thursdayAdapter = new PlanAdapter(PlanActivity.this);
                thursdayAdapter.setPlans(getPlansByDay("Thursday"));
                thursdayRecView.setAdapter(thursdayAdapter);
                thursdayRecView.setLayoutManager(new LinearLayoutManager(PlanActivity.this, RecyclerView.HORIZONTAL, false));

                fridayAdapter = new PlanAdapter(PlanActivity.this);
                fridayAdapter.setPlans(getPlansByDay("Friday"));
                fridayRecView.setAdapter(fridayAdapter);
                fridayRecView.setLayoutManager(new LinearLayoutManager(PlanActivity.this, RecyclerView.HORIZONTAL, false));


                saturdayAdapter = new PlanAdapter(PlanActivity.this);
                saturdayAdapter.setPlans(getPlansByDay("Saturday"));
                saturdayRecView.setAdapter(saturdayAdapter);
                saturdayRecView.setLayoutManager(new LinearLayoutManager(PlanActivity.this, RecyclerView.HORIZONTAL, false));

                sundayAdapter = new PlanAdapter(PlanActivity.this);
                sundayAdapter.setPlans(getPlansByDay("Sunday"));
                sundayRecView.setAdapter(sundayAdapter);
                sundayRecView.setLayoutManager(new LinearLayoutManager(PlanActivity.this, RecyclerView.HORIZONTAL, false));
                if (plans.size() > 0) {
                    noPlanRelLayout.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.VISIBLE);
                    initRecViews();
                } else {
                    noPlanRelLayout.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.GONE);
                    btnAddPlan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PlanActivity.this, AllTrainingActivity.class);
                            startActivity(intent);

                        }
                    });
                }
            }
                 else {
                    noPlanRelLayout.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.GONE);
                    btnAddPlan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PlanActivity.this, AllTrainingActivity.class);
                            startActivity(intent);

                        }
                    });
                }

            }
        }
    }
