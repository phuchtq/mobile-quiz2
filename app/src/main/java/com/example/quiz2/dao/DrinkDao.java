package com.example.quiz2.dao;

import androidx.room.*;
import com.example.quiz2.model.Drink;
import java.util.List;

@Dao
public interface DrinkDao {
    @Query("SELECT * FROM drinks")
    List<Drink> getAllDrinks();

    @Insert
    void insertDrink(Drink drink);

    @Update
    void updateDrink(Drink drink);

    @Delete
    void deleteDrink(Drink drink);

    @Query("DELETE FROM drinks WHERE id = :id")
    void deleteDrinkById(int id);
}
