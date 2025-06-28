package com.example.quiz2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int FOOD_REQUEST_CODE = 1;
    private static final int DRINK_REQUEST_CODE = 2;

    private TextView tvSelectedFood, tvSelectedDrink, tvTotalPrice;
    private Button btnChooseFood, btnChooseDrink, btnOrder;

    private String selectedFoodName = "";
    private double selectedFoodPrice = 0;
    private String selectedDrinkName = "";
    private double selectedDrinkPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
        updateDisplay();
    }

    private void initViews() {
        tvSelectedFood = findViewById(R.id.tv_selected_food);
        tvSelectedDrink = findViewById(R.id.tv_selected_drink);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnChooseFood = findViewById(R.id.btn_choose_food);
        btnChooseDrink = findViewById(R.id.btn_choose_drink);
        btnOrder = findViewById(R.id.btn_order);
    }

    private void setupListeners() {
        btnChooseFood.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FoodActivity.class);
            startActivityForResult(intent, FOOD_REQUEST_CODE);
        });

        btnChooseDrink.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DrinkActivity.class);
            startActivityForResult(intent, DRINK_REQUEST_CODE);
        });

        btnOrder.setOnClickListener(v -> {
            if (selectedFoodName.isEmpty() && selectedDrinkName.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một món!", Toast.LENGTH_SHORT).show();
            } else {
                String orderDetails = "Đơn hàng của bạn:\n";
                if (!selectedFoodName.isEmpty()) {
                    orderDetails += "Món ăn: " + selectedFoodName + "\n";
                }
                if (!selectedDrinkName.isEmpty()) {
                    orderDetails += "Đồ uống: " + selectedDrinkName + "\n";
                }
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                orderDetails += "Tổng tiền: " + formatter.format(selectedFoodPrice + selectedDrinkPrice);

                Toast.makeText(this, orderDetails, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == FOOD_REQUEST_CODE) {
                selectedFoodName = data.getStringExtra("food_name");
                selectedFoodPrice = data.getDoubleExtra("food_price", 0);
            } else if (requestCode == DRINK_REQUEST_CODE) {
                selectedDrinkName = data.getStringExtra("drink_name");
                selectedDrinkPrice = data.getDoubleExtra("drink_price", 0);
            }
            updateDisplay();
        }
    }

    private void updateDisplay() {
        if (selectedFoodName.isEmpty()) {
            tvSelectedFood.setText("Chưa chọn món ăn");
        } else {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvSelectedFood.setText(selectedFoodName + " - " + formatter.format(selectedFoodPrice));
        }

        if (selectedDrinkName.isEmpty()) {
            tvSelectedDrink.setText("Chưa chọn đồ uống");
        } else {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvSelectedDrink.setText(selectedDrinkName + " - " + formatter.format(selectedDrinkPrice));
        }

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvTotalPrice.setText("Tổng tiền: " + formatter.format(selectedFoodPrice + selectedDrinkPrice));
    }
}