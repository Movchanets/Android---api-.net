package com.example.sim.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sim.R;
import com.example.sim.application.HomeApplication;
import com.example.sim.contants.Urls;
import com.example.sim.dto.category.CategoryItemDTO;

import java.util.List;

import kotlinx.coroutines.channels.ActorKt;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoryCardViewHolder> {

    private List<CategoryItemDTO> categories;
    private OnItemClickListener deleteListener;
    private OnItemClickListener updateListener;

   public CategoriesAdapter(List<CategoryItemDTO> categories, OnItemClickListener deleteListener, OnItemClickListener updateListener) {
        this.categories = categories;

        this.deleteListener = deleteListener;
        this.updateListener = updateListener;
    }
    @NonNull
    @Override
    public CategoryCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.category_view, parent, false);
        return new CategoryCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryCardViewHolder holder, int position) {
        if (categories != null && position < categories.size()) {
            CategoryItemDTO item = categories.get(position);
            holder.getCategoryName().setText(item.getName());
            String url = Urls.BASE +"/images/"+ item.getImage();
            Glide.with(HomeApplication.getAppContext())
                    .load(url)
                    .apply(new RequestOptions().override(600))
                    .into(holder.getCategoryImage());
            System.out.println("ID: "+item.getId());
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        deleteListener.onItemClick(item);
                }
            });
            holder.updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateListener.onItemClick(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
       if (categories == null) return 0;
        return  categories.size();
    }
}
