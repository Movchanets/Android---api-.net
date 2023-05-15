using System.Drawing;
using AutoMapper;
using DAL.Entities;
using DAL.Identity;
using DAL.Interfaces;
using Infrastructure.Helper;
using Infrastructure.Interfaces;
using Infrastructure.Models;
using Microsoft.AspNetCore.Identity;

namespace Infrastructure.Services;

public class CategoryService : ICategoryService
{
    private readonly ICategoryRepository _categoryRepository;
    private readonly IMapper _mapper;
    private readonly UserManager<UserEntity> _userManager;

    public CategoryService(ICategoryRepository categoryRepository, IMapper mapper, UserManager<UserEntity> userManager)
    {
        _categoryRepository = categoryRepository;
        _mapper = mapper;
        _userManager = userManager;
    }

    public async Task Create(CreateCategoryVm model, UserEntity user)
    {
        var category = _mapper.Map<CreateCategoryVm, CategoryEntity>(model);
        category.UserId = user.Id;
        category.Image = ImageWorker.SaveImage(model.ImageBase64);
        await _categoryRepository.Create(category);
    }

    public async Task Delete(int id)
    {
        await _categoryRepository.Delete(id);
    }

    public async Task<CategoryItemVm?> GetById(int id)
    {
        var category = await _categoryRepository.GetById(id);

        return _mapper.Map<CategoryEntity, CategoryItemVm>(category);
    }

    public async Task<List<CategoryItemVm>> GetAllAsync(UserEntity user)
    {
        List<CategoryEntity> categories;
        var roles = await _userManager.GetRolesAsync(user);
        if (roles.Contains("admin")){
            categories = _categoryRepository.Categories.ToList();
        }
        else{
            categories = _categoryRepository.Categories.Where(x => x.UserId == user.Id).ToList();
        }
        return _mapper.Map<List<CategoryEntity>, List<CategoryItemVm>>(categories);
    }

    public async Task Update(UpdateCategoryVm model)
    {
        var category = await _categoryRepository.GetById(model.Id);

        if (category != null)
        {
            if (model.ImageBase64 != null)
            {
                ImageWorker.RemoveImage(category.Image);
                category.Image = ImageWorker.SaveImage(model.ImageBase64);
            }

            category.Name = model.Name;
            category.Description = model.Description;
            category.Priority = model.Priority;
            await _categoryRepository.Update(category);
        }
    }
}