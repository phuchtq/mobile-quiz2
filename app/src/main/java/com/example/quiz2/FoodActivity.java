package com.example.quiz2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;
import com.example.quiz2.adapter.FoodAdapter;
import com.example.quiz2.database.AppDatabase;
import com.example.quiz2.model.Food;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class FoodActivity extends AppCompatActivity {
    private ListView listView;
    private Button btnAddFood, btnDeleteFood, btnUpdateFood;
    private FoodAdapter adapter;
    private List<Food> foodList;
    private AppDatabase database;
    private ExecutorService executor;
    private Handler mainHandler;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        initViews();
        initDatabase();
        setupListeners();
        loadFoods();
    }

    private void initViews() {
        listView = findViewById(R.id.lv_foods);
        btnAddFood = findViewById(R.id.btn_add_food);
        btnDeleteFood = findViewById(R.id.btn_delete_food);
        btnUpdateFood = findViewById(R.id.btn_update_food);
    }

    private void initDatabase() {
        database = AppDatabase.getInstance(this);
        executor = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());

        // Initialize default data if database is empty
        initializeDefaultData();
    }

    private void initializeDefaultData() {
        executor.execute(() -> {
            List<Food> existingFoods = database.foodDao().getAllFoods();
            if (existingFoods.isEmpty()) {
                database.foodDao().insertFood(new Food("Phở Hà Nội", "Món phở truyền thống Hà Nội với nước dùng đậm đà", 45000, "pho"));
                database.foodDao().insertFood(new Food("Bún Bò Huế", "Bún bò Huế cay nồng đặc trưng miền Trung", 40000, "bun_bo_hue"));
                database.foodDao().insertFood(new Food("Mì Quảng", "Mì Quảng đậm đà hương vị Quảng Nam", 38000, "mi_quang"));
                database.foodDao().insertFood(new Food("Hủ Tíu Sài Gòn", "Hủ tíu Nam Vang thanh mát", 35000, "hu_tieu"));
            }
        });
    }

    private void setupListeners() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            Food selectedFood = foodList.get(position);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("food_name", selectedFood.getName());
            resultIntent.putExtra("food_price", selectedFood.getPrice());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnAddFood.setOnClickListener(v -> addNewFood());
        btnDeleteFood.setOnClickListener(v -> deleteSelectedFood());
        btnUpdateFood.setOnClickListener(v -> updateSelectedFood());
    }

    private void loadFoods() {
        executor.execute(() -> {
            foodList = database.foodDao().getAllFoods();
            mainHandler.post(() -> {
                adapter = new FoodAdapter(this, foodList);
                listView.setAdapter(adapter);
            });
        });
    }

    private void addNewFood() {
        // Simulate adding a new food item
        executor.execute(() -> {
            Food newFood = new Food("Món mới", "Mô tả món mới", 50000, "default");
            database.foodDao().insertFood(newFood);

            mainHandler.post(() -> {
                loadFoods();
                Toast.makeText(this, "Đã thêm món mới", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void deleteSelectedFood() {
        if (selectedPosition >= 0 && selectedPosition < foodList.size()) {
            Food foodToDelete = foodList.get(selectedPosition);
            executor.execute(() -> {
                database.foodDao().deleteFood(foodToDelete);

                mainHandler.post(() -> {
                    loadFoods();
                    selectedPosition = -1;
                    Toast.makeText(this, "Đã xóa món ăn", Toast.LENGTH_SHORT).show();
                });
            });
        } else {
            Toast.makeText(this, "Vui lòng chọn một món để xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSelectedFood() {
        if (selectedPosition >= 0 && selectedPosition < foodList.size()) {
            Food foodToUpdate = foodList.get(selectedPosition);
            foodToUpdate.setPrice(foodToUpdate.getPrice() + 5000); // Increase price by 5000

            executor.execute(() -> {
                database.foodDao().updateFood(foodToUpdate);

                mainHandler.post(() -> {
                    loadFoods();
                    Toast.makeText(this, "Đã cập nhật giá món ăn", Toast.LENGTH_SHORT).show();
                });
            });
        } else {
            Toast.makeText(this, "Vui lòng chọn một món để cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}
