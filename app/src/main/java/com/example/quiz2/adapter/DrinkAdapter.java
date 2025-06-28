package com.example.quiz2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quiz2.R;
import com.example.quiz2.model.Drink;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

public class DrinkAdapter extends BaseAdapter {
    private Context context;
    private List<Drink> drinks;
    private LayoutInflater inflater;

    public DrinkAdapter(Context context, List<Drink> drinks) {
        this.context = context;
        this.drinks = drinks;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Object getItem(int position) {
        return drinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return drinks.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_drink, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.img_drink);
            holder.nameTextView = convertView.findViewById(R.id.tv_drink_name);
            holder.descriptionTextView = convertView.findViewById(R.id.tv_drink_description);
            holder.priceTextView = convertView.findViewById(R.id.tv_drink_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Drink drink = drinks.get(position);
        holder.nameTextView.setText(drink.getName());
        holder.descriptionTextView.setText(drink.getDescription());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.priceTextView.setText(formatter.format(drink.getPrice()));

        // Set image based on drink name
        int imageResource = getImageResource(drink.getImageName());
        holder.imageView.setImageResource(imageResource);

        return convertView;
    }

    private int getImageResource(String imageName) {
        switch (imageName) {
            case "pepsi": return R.drawable.pepsi;
            case "heineken": return R.drawable.heineken;
            case "tiger": return R.drawable.tiger;
            case "saigon_do": return R.drawable.saigon_do;
            default: return R.drawable.default_drink;
        }
    }

    static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
    }
}