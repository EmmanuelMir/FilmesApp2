package com.emmanuelmir.filmesapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Database instanciada em singleton, com o fim de evitar múltiplas instâncias do Banco.
 */

@Database(entities = {WrapperModel.FilmesModel.class, WrapperModel.FilmesModel.Result.class}, version = 1)
public abstract class RoomDb extends RoomDatabase {

    private static RoomDb INSTANCE;

    public abstract FilmeDao filmeDao();

    public static RoomDb getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), RoomDb.class, "filmes-database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries().fallbackToDestructiveMigration()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
