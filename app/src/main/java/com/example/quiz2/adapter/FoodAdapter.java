package com.example.quiz2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quiz2.R;
import com.example.quiz2.model.Food;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class FoodAdapter extends BaseAdapter {
    private Context context;
    private List<Food> foods;
    private LayoutInflater inflater;

    public FoodAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foods.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_food, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.img_food);
            holder.nameTextView = convertView.findViewById(R.id.tv_food_name);
            holder.descriptionTextView = convertView.findViewById(R.id.tv_food_description);
            holder.priceTextView = convertView.findViewById(R.id.tv_food_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Food food = foods.get(position);
        holder.nameTextView.setText(food.getName());
        holder.descriptionTextView.setText(food.getDescription());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.priceTextView.setText(formatter.format(food.getPrice()));

        // Set image based on food name
        int imageResource = getImageResource(food.getImageName());
        holder.imageView.setImageResource(imageResource);

        return convertView;
    }

    private int getImageResource(String imageName) {
        switch (imageName) {
            case "pho": return R.drawable.pho;
            case "bun_bo_hue": return R.drawable.bun_bo_hue;
            case "mi_quang": return R.drawable.mi_quang;
            case "hu_tieu": return R.drawable.hu_tieu;
            default: return R.drawable.default_food;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
    }
}