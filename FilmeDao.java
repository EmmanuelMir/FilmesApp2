package com.emmanuelmir.filmesapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FilmeDao {
    @Query("SELECT * FROM filme")
    List<WrapperModel.FilmesModel.Result> getAll();

    @Query("SELECT * FROM filme WHERE title IN (:title)")
    List<WrapperModel.FilmesModel.Result> findByTitle(String title);

    @Query("SELECT * FROM filme WHERE first_name LIKE :first AND "
            + "last_name LIKE :last LIMIT 1")
    WrapperModel.FilmesModel.Result findByName(String first, String last);

    @Insert
    void insertAll(WrapperModel.FilmesModel.Result... filme);

    @Delete
    void delete(WrapperModel.FilmesModel.Result filme);
}