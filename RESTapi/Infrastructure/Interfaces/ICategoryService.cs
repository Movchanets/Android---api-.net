using DAL.Identity;
using Infrastructure.Models;

namespace Infrastructure.Interfaces;

public interface ICategoryService 
{
    Task Create(CreateCategoryVm model, UserEntity user);
    Task Delete(int id);
    Task<CategoryItemVm?> GetById(int id);
    Task<List<CategoryItemVm>> GetAllAsync(UserEntity user);
    Task Update(UpdateCategoryVm model);
}