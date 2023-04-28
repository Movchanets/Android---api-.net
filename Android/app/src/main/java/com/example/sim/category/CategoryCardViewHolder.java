package com.example.sim.category;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sim.R;

public class CategoryCardViewHolder extends RecyclerView.ViewHolder {
    private ImageView categoryImage;
    private TextView categoryName;
    private int id;

    public CategoryCardViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryName=itemView.findViewById(R.id.categoryName);
        categoryImage=itemView.findViewById(R.id.categoryImage);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public ImageView getCategoryImage() {
        return categoryImage;
    }

    public TextView getCategoryName() {
        return categoryName;
    }
}
