package com.example.gymplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity implements PlanAdapter.RemovePlan {
    @Override
    public void onRemovePlanResult(Plan plan) {
        Utils.removePlan(plan);
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
        ArrayList<Plan> allPlans = Utils.getPlans();
        ArrayList<Plan> plans = new ArrayList<>();
        for (Plan p : allPlans) {
            if (p.getDay().equalsIgnoreCase(day)) {
                plans.add(p);
            }
        }
        return plans;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PlanActivity.class);
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

}