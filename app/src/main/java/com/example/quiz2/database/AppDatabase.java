package com.example.quiz2.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.example.quiz2.model.Food;
import com.example.quiz2.model.Drink;
import com.example.quiz2.dao.FoodDao;
import com.example.quiz2.dao.DrinkDao;

@Database(entities = {Food.class, Drink.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract FoodDao foodDao();
    public abstract DrinkDao drinkDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "food_ordering_database"
            ).build();
        }
        return instance;
    }
}