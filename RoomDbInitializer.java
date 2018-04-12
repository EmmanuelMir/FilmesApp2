package com.emmanuelmir.filmesapp;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class RoomDbInitializer {

    private static final String TAG = RoomDbInitializer.class.getName();

    public static void populateAsync(@NonNull final RoomDb db, WrapperModel.FilmesModel.Result realResult) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateTestAsync(@NonNull final RoomDb db) {
        populateWithTestData(db);
    }

    private static WrapperModel.FilmesModel.Result addFilme(final RoomDb db, WrapperModel.FilmesModel.Result result) {
        db.filmeDao().insertAll(result);
        return result;
    }

    private static void populateWithTestData(RoomDb db) {
        WrapperModel.FilmesModel.Result result = new WrapperModel.FilmesModel.Result();
        result.setAdult(true);
        result.setBackdropPath("Things");
        result.setId(10);
        result.setOriginalLanguage("português");
        result.setOriginalTitle("Avatar Azul");
        result.setOverview("Wathever");
        result.setReleaseDate("uhul");
        result.setTitle("Avatar");
        result.setPopularity(6);
        result.setVoteAverage(12);
        result.setVoteCount(2000);
        result.setVideo(false);
        result.setPosterPath("alo");
        addFilme(db, result);

        List<WrapperModel.FilmesModel.Result> resultList = db.filmeDao().getAll();
        Log.d(RoomDbInitializer.TAG, "Rows Count: " + resultList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<WrapperModel.FilmesModel, Void, Void> {

        private final RoomDb mDb;

        PopulateDbAsync(RoomDb db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(WrapperModel.FilmesModel... params) {
            populateWithTestData(mDb);
            return null;
        }
    }
}
