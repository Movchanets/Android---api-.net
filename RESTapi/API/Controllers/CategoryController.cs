using AutoMapper;
using Infrastructure.Interfaces;
using Infrastructure.Models;
using Microsoft.AspNetCore.Mvc;

namespace RESTapi.Controllers;
[ApiController]
[Route("[controller]")]

public class CategoryController :ControllerBase
{
    private readonly IMapper _mapper;
    private readonly ICategoryService _categoryService;
    public CategoryController(ICategoryService categoryService, IMapper mapper)
    {
        _categoryService = categoryService;
        _mapper = mapper;
    }
    [HttpPost]
    [Route("Create")]
    public async Task<IActionResult> Create(CreateCategoryVm model)
    {
        await _categoryService.Create(model);
        return Ok();
    }
    [HttpGet]
    [Route("GetAll")]
    public async Task<IActionResult> GetAllAsync()
    {
        return Ok(await _categoryService.GetAllAsync());
    }
}