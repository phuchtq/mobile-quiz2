package com.example.quiz2.dao;

import androidx.room.*;
import com.example.quiz2.model.Food;
import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM foods")
    List<Food> getAllFoods();

    @Insert
    void insertFood(Food food);

    @Update
    void updateFood(Food food);

    @Delete
    void deleteFood(Food food);

    @Query("DELETE FROM foods WHERE id = :id")
    void deleteFoodById(int id);
}