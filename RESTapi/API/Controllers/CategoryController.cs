using AutoMapper;
using Infrastructure.Interfaces;
using Infrastructure.Models;
using Microsoft.AspNetCore.Mvc;

namespace RESTapi.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CategoriesController : ControllerBase
{
    private readonly IMapper _mapper;
    private readonly ICategoryService _categoryService;

    public CategoriesController(ICategoryService categoryService, IMapper mapper)
    {
        _categoryService = categoryService;
        _mapper = mapper;
    }

    [HttpPost("create")]
    public async Task<IActionResult> Create([FromBody] CreateCategoryVm model)
    {
        await _categoryService.Create(model);
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
        Thread.Sleep(1000);
        return Ok(await _categoryService.GetAllAsync());
    }
}