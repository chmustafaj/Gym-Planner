package com.example.gymplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class AllTrainingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TrainingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_training);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#00aaff"));
        actionBar.setBackgroundDrawable(colorDrawable);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TrainingAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        ArrayList<Training> allTrainings = Utils.getTrainings();
//        if (null != allTrainings) {
//            adapter.setTrainings(allTrainings);
//        }
        new GetAllTrainings().execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AllTrainingActivity.this, MainActivity.class);
        startActivity(intent);
    }
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
    private class GetAllTrainings extends AsyncTask<Void,Void,ArrayList<Training>>{
        private DataBaseHelper dataBaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataBaseHelper = new DataBaseHelper(AllTrainingActivity.this);
        }

        @Override
        protected ArrayList<Training> doInBackground(Void... voids) {
            try {
                SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("trainings",null,null,null,null,null,null);
                if(null!=cursor){
                    if(cursor.moveToFirst()){
                        ArrayList<Training> trainings = new ArrayList<>();
                        int idIndex = cursor.getColumnIndex("id");
                        int nameIndex = cursor.getColumnIndex("name");
                        int shortDescIndex = cursor.getColumnIndex("shortDesc");
                        int longDescIndex = cursor.getColumnIndex("longDesc");
                        int imageUrlIndex = cursor.getColumnIndex("imageUrl");
                        for(int i = 0;i<cursor.getCount();i++){
                            Training t = new Training();
                            t.setID(cursor.getInt(idIndex));
                            t.setName(cursor.getString(nameIndex));
                            t.setShortDesc(cursor.getString(shortDescIndex));
                            t.setLongDesc(cursor.getString(longDescIndex));
                            t.setImageURL(cursor.getString(imageUrlIndex));
                            trainings.add(t);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        db.close();
                        return trainings;
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
        protected void onPostExecute(ArrayList<Training> trainings) {
            super.onPostExecute(trainings);
            if(null!=trainings){
                adapter.setTrainings(trainings);
            }else{
                adapter.setTrainings(new ArrayList<Training>());
            }
        }
    }

}