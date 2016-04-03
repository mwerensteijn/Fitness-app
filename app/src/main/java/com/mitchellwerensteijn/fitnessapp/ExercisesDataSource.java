package com.mitchellwerensteijn.fitnessapp;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ExercisesDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_EXERCISE };

    public ExercisesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Exercise createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_EXERCISE, comment);
        long insertId = database.insert(MySQLiteHelper.TABLE_EXERCISES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Exercise newExercise = cursorToExercise(cursor);
        cursor.close();
        return newExercise;
    }

    public void deleteExercise(Exercise exercise) {
        long id = exercise.getId();
        System.out.println("Exercise deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_EXERCISES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Exercise> getAllComments() {
        List<Exercise> comments = new ArrayList<Exercise>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EXERCISES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Exercise exercise = cursorToExercise(cursor);
            comments.add(exercise);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Exercise cursorToExercise(Cursor cursor) {
        Exercise exercise = new Exercise();
        exercise.setId(cursor.getLong(0));
        exercise.setExercise(cursor.getString(1));
        return exercise;
    }
}
