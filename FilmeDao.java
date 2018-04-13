package com.emmanuelmir.filmesapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FilmeDao {
    @Query("SELECT * FROM result")
    List<WrapperModel.FilmesModel.Result> getAll();

    @Query("SELECT * FROM result WHERE title IN (:title)")
    List<WrapperModel.FilmesModel.Result> findByTitle(String title);

    @Query("SELECT * FROM result WHERE title LIKE :title AND "
            + "voteAverage LIKE :voteAverage ")
    WrapperModel.FilmesModel.Result findByName(String title, float voteAverage );

    @Insert
    void insertAll(List<WrapperModel.FilmesModel.Result> results);

    @Insert
    void insertResult(WrapperModel.FilmesModel.Result result);


    @Query("SELECT * FROM filmesmodel")
    WrapperModel.FilmesModel getFilmesModel();

    @Insert
    void insertFilmeModel(WrapperModel.FilmesModel filmesModel);

    @Query("UPDATE FilmesModel SET page = :page WHERE totalPages = :totalPages")
            void updateFilmeModelPages(int page,int totalPages);

    @Delete
    void delete(WrapperModel.FilmesModel.Result result);

    /*@Query("SELECT * FROM result")
    List<WrapperModel.FilmesModel> getAll();*/

}