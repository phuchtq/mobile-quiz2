package com.example.quiz2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;
import com.example.quiz2.adapter.DrinkAdapter;
import com.example.quiz2.database.AppDatabase;
import com.example.quiz2.model.Drink;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class DrinkActivity extends AppCompatActivity {
    private ListView listView;
    private Button btnAddDrink, btnDeleteDrink, btnUpdateDrink;
    private DrinkAdapter adapter;
    private List<Drink> drinkList;
    private AppDatabase database;
    private ExecutorService executor;
    private Handler mainHandler;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        initViews();
        initDatabase();
        setupListeners();
        loadDrinks();
    }

    private void initViews() {
        listView = findViewById(R.id.lv_drinks);
        btnAddDrink = findViewById(R.id.btn_add_drink);
        btnDeleteDrink = findViewById(R.id.btn_delete_drink);
        btnUpdateDrink = findViewById(R.id.btn_update_drink);
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
            List<Drink> existingDrinks = database.drinkDao().getAllDrinks();
            if (existingDrinks.isEmpty()) {
                database.drinkDao().insertDrink(new Drink("Pepsi", "Nước ngọt Pepsi mát lạnh", 15000, "pepsi"));
                database.drinkDao().insertDrink(new Drink("Heineken", "Bia Heineken nhập khẩu", 25000, "heineken"));
                database.drinkDao().insertDrink(new Drink("Tiger", "Bia Tiger thơm ngon", 20000, "tiger"));
                database.drinkDao().insertDrink(new Drink("Sài Gòn Đỏ", "Bia Sài Gòn Đỏ truyền thống", 18000, "saigon_do"));
            }
        });
    }

    private void setupListeners() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            Drink selectedDrink = drinkList.get(position);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("drink_name", selectedDrink.getName());
            resultIntent.putExtra("drink_price", selectedDrink.getPrice());
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnAddDrink.setOnClickListener(v -> addNewDrink());
        btnDeleteDrink.setOnClickListener(v -> deleteSelectedDrink());
        btnUpdateDrink.setOnClickListener(v -> updateSelectedDrink());
    }

    private void loadDrinks() {
        executor.execute(() -> {
            drinkList = database.drinkDao().getAllDrinks();
            mainHandler.post(() -> {
                adapter = new DrinkAdapter(this, drinkList);
                listView.setAdapter(adapter);
            });
        });
    }

    private void addNewDrink() {
        // Simulate adding a new drink item
        executor.execute(() -> {
            Drink newDrink = new Drink("Đồ uống mới", "Mô tả đồ uống mới", 22000, "default");
            database.drinkDao().insertDrink(newDrink);

            mainHandler.post(() -> {
                loadDrinks();
                Toast.makeText(this, "Đã thêm đồ uống mới", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void deleteSelectedDrink() {
        if (selectedPosition >= 0 && selectedPosition < drinkList.size()) {
            Drink drinkToDelete = drinkList.get(selectedPosition);
            executor.execute(() -> {
                database.drinkDao().deleteDrink(drinkToDelete);

                mainHandler.post(() -> {
                    loadDrinks();
                    selectedPosition = -1;
                    Toast.makeText(this, "Đã xóa đồ uống", Toast.LENGTH_SHORT).show();
                });
            });
        } else {
            Toast.makeText(this, "Vui lòng chọn một đồ uống để xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSelectedDrink() {
        if (selectedPosition >= 0 && selectedPosition < drinkList.size()) {
            Drink drinkToUpdate = drinkList.get(selectedPosition);
            drinkToUpdate.setPrice(drinkToUpdate.getPrice() + 2000); // Increase price by 2000

            executor.execute(() -> {
                database.drinkDao().updateDrink(drinkToUpdate);

                mainHandler.post(() -> {
                    loadDrinks();
                    Toast.makeText(this, "Đã cập nhật giá đồ uống", Toast.LENGTH_SHORT).show();
                });
            });
        } else {
            Toast.makeText(this, "Vui lòng chọn một đồ uống để cập nhật", Toast.LENGTH_SHORT).show();
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