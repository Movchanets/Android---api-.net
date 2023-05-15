using AutoMapper;
using DAL.Identity;
using Infrastructure.Interfaces;
using Infrastructure.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace RESTapi.Controllers;

[ApiController]
[Route("api/[controller]")]
[Authorize]
public class CategoriesController : ControllerBase
{
    private readonly IMapper _mapper;
    private readonly ICategoryService _categoryService;
    private readonly UserManager<UserEntity> _userManager;

    public CategoriesController(ICategoryService categoryService, IMapper mapper, UserManager<UserEntity> userManager)
    {
        _categoryService = categoryService;
        _mapper = mapper;
        _userManager = userManager;
    }

    [HttpPost("create")]
    public async Task<IActionResult> Create([FromBody] CreateCategoryVm model)
    {
        string email = User.Claims.First().Value;
        var user = await _userManager.FindByEmailAsync(email);
        
        await _categoryService.Create(model, user);
        return Ok();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(int id)
    {  
        await _categoryService.Delete(id);
        return Ok();
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(int id)
    {
       
        var category = await _categoryService.GetById(id);
        
        if (category is null)
            return NotFound();

        return Ok(_mapper.Map<CategoryItemVm>(category));
    }

    [HttpPut("update")]
    public async Task<IActionResult> Update([FromBody] UpdateCategoryVm model)
    {
        
        await _categoryService.Update(model);
        return Ok();
    }

    [HttpGet("list")]
    public async Task<IActionResult> GetAllAsync()
    {
        string email = User.Claims.First().Value;
        var user = await _userManager.FindByEmailAsync(email);
        Thread.Sleep(1000);
        return Ok(await _categoryService.GetAllAsync(user));
    }
}