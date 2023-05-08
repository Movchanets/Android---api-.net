package com.example.sim.network;

import com.example.sim.dto.category.CategoryCreateDTO;
import com.example.sim.dto.category.CategoryItemDTO;
import com.example.sim.dto.category.CategoryDTO;
import com.example.sim.dto.category.CategoryUpdateDTO;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CategoriesApi {
    @GET("/api/categories/list")
    public Call<List<CategoryItemDTO>> list();
    @GET("/api/categories/{id}")
    public Call<CategoryDTO> get(@Path("id") int id);
    @PUT("/api/categories/update")
    public Call<Void> update(@Body CategoryUpdateDTO model);
    @POST("/api/categories/create")
    public Call<Void> create(@Body CategoryCreateDTO model);
    @DELETE("/api/categories/{id}")
    public Call<Void> delete(@Path("id") int id);

}