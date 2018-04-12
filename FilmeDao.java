package com.emmanuelmir.filmesapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FilmeDao {
    @Query("SELECT * FROM result")
    List<WrapperModel.FilmesModel.Result> getAll();

    @Query("SELECT * FROM result WHERE title IN (:title)")
    List<WrapperModel.FilmesModel.Result> findByTitle(String title);

    @Query("SELECT * FROM result WHERE title LIKE :title AND "
            + "vote_average LIKE :voteAverage LIMIT 1")
    WrapperModel.FilmesModel.Result findByName(String title, float voteAverage );

    @Insert
    void insertAll(WrapperModel.FilmesModel.Result... results);

    @Delete
    void delete(WrapperModel.FilmesModel.Result result);
}