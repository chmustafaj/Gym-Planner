package com.example.gymplanner;

import static com.example.gymplanner.TrainingActivity.TRAINING_KEY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
    private static final String TAG = "";

    public interface RemovePlan {
        void onRemovePlanResult(Plan plan);
    }
    public interface Accomplished{
        void setAccomplished(Plan plan);
    }
    private Accomplished accomplished;
    private RemovePlan removePlan;
    private ArrayList<Plan> plans = new ArrayList<>();
    private Context context;
    private String type = "";

    public PlanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtName.setText(plans.get(position).getTraining().getName());
        holder.txtDescription.setText(plans.get(position).getTraining().getShortDesc());
        holder.txtTime.setText(String.valueOf(plans.get(position).getMinutes()));
        Glide.with(context).asBitmap().load(plans.get(position).getTraining().getImageURL()).into(holder.image);
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        if (dataBaseHelper.checkAccomplished(db,"plans",plans.get(position).getId())) {
            holder.emptyCircle.setVisibility(View.GONE);
            holder.checkedCircle.setVisibility(View.VISIBLE);
        } else {
            holder.emptyCircle.setVisibility(View.VISIBLE);
            holder.checkedCircle.setVisibility(View.GONE);
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TrainingActivity.class);
                intent.putExtra(TRAINING_KEY, plans.get(position).getTraining());
                context.startActivity(intent);
            }
        });
        if (type.equals("edit")) {
            holder.emptyCircle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context)
                            .setTitle("Completed")
                            .setMessage("Have you finished " + plans.get(position).getTraining().getName() + "?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ArrayList<Plan> allPlans =getAllPlans();
                                    for (Plan p : allPlans) {
                                        if (p.getId()==(plans.get(position).getId())) {
                                            try{
                                                accomplished = (Accomplished) context;
                                                accomplished.setAccomplished(p);
                                            }catch (ClassCastException c){
                                                c.printStackTrace();
                                            }
                                        }
                                    }
                                    notifyDataSetChanged();
                                }
                            });
                    builder.create().show();
                }
            });
        }
        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Remove")
                        .setMessage("Are you sure you want to remove " + plans.get(position).getTraining().getName() + "?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    removePlan = (RemovePlan) context;
                                    removePlan.onRemovePlanResult(plans.get(position));
                                } catch (ClassCastException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                builder.create().show();
                return true;
            }

        });

    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    public void setPlans(ArrayList<Plan> plans) {
        this.plans = plans;
        notifyDataSetChanged();
    }

    public void setType(String type) {
        this.type = type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTime, txtName, txtDescription;
        private MaterialCardView parent;
        private ImageView image, emptyCircle, checkedCircle;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            parent = itemView.findViewById(R.id.parent);
            image = itemView.findViewById(R.id.trainingImage);
            emptyCircle = itemView.findViewById(R.id.emptyCircle);
            checkedCircle = itemView.findViewById(R.id.checkedCircle);
        }
    }
    private ArrayList<Plan> getAllPlans(){
        ArrayList<Plan> plans = new ArrayList<>();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getReadableDatabase();
        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM plans",null);
        if(c!=null){
            if(c.moveToFirst()){
                for(int i =0;i<c.getCount();i++){
                    Plan p = new Plan();
                    int id =c.getInt(c.getColumnIndexOrThrow("id"));
                    int trainingId = c.getInt(c.getColumnIndexOrThrow("trainingId"));
                    Training t = trainingById(trainingId);
                    int minutes = c.getInt(c.getColumnIndexOrThrow("minutes"));
                    String day = c.getString(c.getColumnIndexOrThrow("day"));
                    int isAccomplished = c.getInt(c.getColumnIndexOrThrow("isAccomplished"));
                    p.setAccomplished(isAccomplished);
                    p.setDay(day);
                    p.setId(id);
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
    public  Training trainingById(int id){
        String ID= String.valueOf(id);  //sql takes commands in strings
        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
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
