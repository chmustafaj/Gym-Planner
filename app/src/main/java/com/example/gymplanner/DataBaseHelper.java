package com.example.gymplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "gym";
    private static final int DB_VERSION = 1;
    private static final String TAG = "";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlStatement = "create table trainings(id integer primary key autoincrement,name varchar,shortDesc text" +
                ",longDesc text, imageUrl text)";
        sqLiteDatabase.execSQL(sqlStatement);
        String sqlStatement2 = "create table plans(id integer primary key autoincrement, trainingId integer,minutes integer,day text,isAccomplished integer)";
        sqLiteDatabase.execSQL(sqlStatement2);
        initDatabase(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private void initDatabase(SQLiteDatabase sqLiteDatabase) {
        ContentValues pushUp = new ContentValues();
        pushUp.put("name", "Push up");
        pushUp.put("shortDesc", "An exercise in which a person lies facing the floor and, keeping their back straight, raises their body by pressing down on their hands.");
        pushUp.put("longDesc", "The definition of a push-up is an exercise done laying with face, palms and toes facing down, keeping legs and back straight, extending arms straight to push body up and back down again. An example of a push-up is a great exercise that works the chest, shoulder and arm muscles.");
        pushUp.put("imageUrl", "https://www.istreetwatch.co.uk/wp-content/uploads/2019/01/push-ups.jpg");
        sqLiteDatabase.insert("trainings", null, pushUp);

        ContentValues squat = new ContentValues();
        squat.put("name", "Squat");
        squat.put("shortDesc", "If you crouch down very low and sit on your heels, you squat");
        squat.put("longDesc", "A squat is a strength exercise in which the trainee lowers their hips from a standing position and then stands back up. During the descent of a squat, the hip and knee joints flex while the ankle joint dorsiflexes; conversely the hip and knee joints extend and the ankle joint plantarflexes when standing up.");
        squat.put("imageUrl", "https://lmimirror3pvr.azureedge.net/static/media/16949/921e38e6-9020-4dd9-a619-7726cadc7284/fit-planet-60-hero-image-960x540.jpg");
        sqLiteDatabase.insert("trainings", null, squat);

        ContentValues legPress = new ContentValues();
        legPress.put("name", "Leg Press");
        legPress.put("shortDesc", "The leg press is a weight training exercise in which the individual pushes a weight or resistance away from them using their legs.");
        legPress.put("longDesc", "The leg press is a weight training exercise in which the individual pushes a weight or resistance away from them using their legs. The term leg press also refers to the apparatus used to perform this exercise. The leg press can be used to evaluate an athlete's overall lower body strength (from knee joint to hip).");
        legPress.put("imageUrl", "https://www.fitnessfactoryoutlet.com/images/products/14392.png");
        sqLiteDatabase.insert("trainings", null, legPress);

        ContentValues pectorals = new ContentValues();
        pectorals.put("name", "Pectorals");
        pectorals.put("shortDesc", "Amazing for building chest muscles");
        pectorals.put("longDesc", "Your pectoral muscles are one of the largest muscle groups in your upper body. They pull on the humerus (upper arm bone), allowing you to make vertical, horizontal, and rotational movements with your arms. You use these muscles all day, every day, so it's important to keep them strong.");
        pectorals.put("imageUrl", "https://www.korrukmag.com/wp-content/uploads/2019/11/2756-serious-man-training-upper-body-using-fly-machine-1060869384-0effce7eff3044289055fcd16a9c6788.jpg");
        sqLiteDatabase.insert("trainings", null, pectorals);

        ContentValues pullUps = new ContentValues();
        pullUps.put("name", "Pull-ups");
        pullUps.put("shortDesc", "An exercise involving raising oneself with one's arms by pulling up against a horizontal bar fixed above one's head.");
        pullUps.put("longDesc", "A pull-up is an upper-body strength exercise. The pull-up is a closed-chain movement where the body is suspended by the hands and pulls up. As this happens, the elbows flex and the shoulders adduct and extend to bring the elbows to the torso.");
        pullUps.put("imageUrl", "https://www.fititnow.com/wp-content/uploads/2020/02/3-Simple-Tips-To-Improve-Your-Pull-Ups.jpg");
        sqLiteDatabase.insert("trainings", null, pullUps);
    }

    public void insert(SQLiteDatabase db, String tableName, int trainingId, int minutes, String day, int isAccomplished) {
        ContentValues values = new ContentValues();
        values.put("trainingId", trainingId);
        values.put("minutes", minutes);
        values.put("day", day);
        values.put("isAccomplished", isAccomplished);
        db.insert(tableName, null, values);
    }

    public Cursor read(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        return cursor;
    }


    public void delete(SQLiteDatabase db, String tableName, String id) {
        Log.d(TAG, "delete: " + id);
        db.delete(tableName, "id=?", new String[]{id});
    }

    public void setIsAccomplished(SQLiteDatabase db, String tableName, String id) {
        ContentValues values = new ContentValues();
        values.put("isAccomplished", 1);
        int affectedRow = db.update(tableName, values, "id=?", new String[]{id});
        ;


        db.close();
    }

    public boolean checkAccomplished(SQLiteDatabase db, String tableName, int id) {
        Cursor cursor = db.query("plans", new String[]{"isAccomplished"}, "id=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int isAccomplished = cursor.getInt(cursor.getColumnIndex("isAccomplished"));
                cursor.close();
                db.close();
                if (isAccomplished == 1) {
                    return true;
                }
            }
        }
        return false;

    }

}
