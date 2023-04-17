using Infrastructure.Models;

namespace Infrastructure.Interfaces;

public interface ICategoryService 
{
    Task Create(CreateCategoryVm model);
    Task<List<CategoryItemVm>> GetAllAsync();
}