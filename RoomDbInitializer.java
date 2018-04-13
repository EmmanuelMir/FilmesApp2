package com.emmanuelmir.filmesapp;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RoomDbInitializer {

    private static final String TAG = RoomDbInitializer.class.getName();

    public static void populateAsync(@NonNull final RoomDb db, WrapperModel.FilmesModel rFilmesModels) {
        PopulateDbAsync task = new PopulateDbAsync(db,rFilmesModels);
        task.execute();
    }

    public static WrapperModel.FilmesModel getFilmesModelBanco(@NonNull final RoomDb db){
        WrapperModel.FilmesModel mFilmesModel = db.filmeDao().getFilmesModel();
        if(mFilmesModel!=null)
            mFilmesModel.setResults(db.filmeDao().getAll());
        return mFilmesModel;
    }

    public static void populateTestAsync(@NonNull final RoomDb db) {
        populateWithTestData(db);
    }

    private static void addFilme(final RoomDb db, WrapperModel.FilmesModel.Result result) {
        try {
            db.filmeDao().insertResult(result);
        }catch (SQLiteConstraintException e){
            e.printStackTrace();
        }

    }

    private static WrapperModel.FilmesModel addFilmeModel(final RoomDb db, WrapperModel.FilmesModel mfilmesModel){
        db.filmeDao().insertFilmeModel(mfilmesModel);
        return mfilmesModel;
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

    private static void populateWithRealData(RoomDb db,WrapperModel.FilmesModel filmesModel) {
        List<WrapperModel.FilmesModel.Result> results = new ArrayList(filmesModel.getResults());
        if(db.filmeDao().getFilmesModel()==null) {
            addFilmeModel(db, filmesModel);
        }else{
            if(filmesModel.getPage()>db.filmeDao().getFilmesModel().getPage()){
                db.filmeDao().updateFilmeModelPages(filmesModel.getPage(), filmesModel.getTotalPages());
            }
        }
        for(int i=0; i<results.size(); i++) {
            addFilme(db, results.get(i));
        }

        WrapperModel.FilmesModel mFilmesModel = db.filmeDao().getFilmesModel();
        List<WrapperModel.FilmesModel.Result> resultList = db.filmeDao().getAll();
        Log.d(RoomDbInitializer.TAG, "Rows Count: " + resultList.size()+"" +
                "Rows FilmesModel: "+ mFilmesModel.getPage());
    }

    private static WrapperModel.FilmesModel selectBanco(RoomDb db){
        WrapperModel.FilmesModel mFilmesModel = db.filmeDao().getFilmesModel();
        return mFilmesModel;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RoomDb mDb;
        private final WrapperModel.FilmesModel afilmesModel;

        PopulateDbAsync(RoomDb db, WrapperModel.FilmesModel rFilmesModel) {
            mDb = db;
            afilmesModel = new WrapperModel.FilmesModel(rFilmesModel.getPage(), rFilmesModel.getResults(),rFilmesModel.getTotalResults(),rFilmesModel.getTotalPages());
        }

        @Override
        protected Void doInBackground(Void... params) {
            //populateWithTestData(mDb);
            populateWithRealData(mDb,afilmesModel);
            return null;
        }
    }
}
