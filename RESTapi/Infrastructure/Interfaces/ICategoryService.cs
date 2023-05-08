using Infrastructure.Models;

namespace Infrastructure.Interfaces;

public interface ICategoryService 
{
    Task Create(CreateCategoryVm model);
    Task Delete(int id);
    Task<CategoryItemVm?> GetById(int id);
    Task<List<CategoryItemVm>> GetAllAsync();
    Task Update(UpdateCategoryVm model);
}