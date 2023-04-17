using AutoMapper;
using DAL.Entities;
using DAL.Interfaces;
using Infrastructure.Helper;
using Infrastructure.Interfaces;
using Infrastructure.Models;

namespace Infrastructure.Services;


public class CategoryService : ICategoryService
{
    private readonly ICategoryRepository _categoryRepository;
    private readonly IMapper _mapper;

    public CategoryService(ICategoryRepository categoryRepository, IMapper mapper)
    {
        _categoryRepository = categoryRepository;
        _mapper = mapper;
    }

    public async Task Create(CreateCategoryVm model)
    {
        var category = _mapper.Map<CreateCategoryVm, CategoryEntity>(model);
        category.Image = ImageWorker.SaveImage(model.ImageBase64);
        await _categoryRepository.Create(category);

    }

    public async Task<List<CategoryItemVm>> GetAllAsync()
    {

        return _mapper.Map<List<CategoryEntity>, List<CategoryItemVm>>(_categoryRepository.Categories.ToList());

    }
}
