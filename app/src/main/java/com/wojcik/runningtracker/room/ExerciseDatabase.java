package com.wojcik.runningtracker.room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wojcik.runningtracker.utility.Calculate;
import com.wojcik.runningtracker.utility.TextFormatter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {ExerciseEntity.class}, version = 5, exportSchema = false)
public abstract class ExerciseDatabase extends RoomDatabase {
    public abstract ExerciseDao exerciseDao();

    private static volatile ExerciseDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ExerciseDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ExerciseDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room
                            .databaseBuilder(context.getApplicationContext(), ExerciseDatabase.class, "exercise_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(createCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback createCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                ExerciseDao exerciseDao = INSTANCE.exerciseDao();
                exerciseDao.deleteAll();

                exerciseDao.insert(new ExerciseEntity(2023,
                        1,
                        11,
                        983f,
                        450f,
                        Calculate.calculateAvgPace(983f, 450f),
                        TextFormatter.getEmojiFromName("average"),
                        TextFormatter.getEmojiFromName("cloudy"),
                        TextFormatter.getEmojiFromName("run")));

                exerciseDao.insert(new ExerciseEntity(2023,
                        1,
                        11,
                        256.4f,
                        67f,
                        Calculate.calculateAvgPace(67f, 256.4f),
                        TextFormatter.getEmojiFromName("great"),
                        TextFormatter.getEmojiFromName("sunny"),
                        TextFormatter.getEmojiFromName("walk")));


                exerciseDao.insert(new ExerciseEntity(2023,
                        1,
                        13,
                        2564.4f,
                        4034f,
                        Calculate.calculateAvgPace(4034f, 2564.4f),
                        TextFormatter.getEmojiFromName("average"),
                        TextFormatter.getEmojiFromName("snowing"),
                        TextFormatter.getEmojiFromName("bike")));

            });
        }
    };
}
