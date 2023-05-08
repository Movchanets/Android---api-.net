using DAL.Constants;
using DAL.Identity;
using Infrastructure.Interfaces;
using Infrastructure.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace RESTapi.Controllers;
[Route("api/[controller]")]
[ApiController]
public class AccountController : ControllerBase
{
    private readonly IJwtTokenService _jwtTokenService;
    private readonly UserManager<UserEntity> _userManager;
    
    public AccountController(IJwtTokenService jwtTokenService, UserManager<UserEntity> userManager)
    {
        _jwtTokenService = jwtTokenService;
        _userManager = userManager;
    }
    [HttpPost("login")]
    public async Task<IActionResult> Login([FromBody] LoginViewModel model)
    {
        var user = await _userManager.FindByEmailAsync(model.Email);
        if (user == null)
        {
            return BadRequest(new { invalid = "Email or password is incorrect." });
        }
        if (!await _userManager.CheckPasswordAsync(user, model.Password))
        {
            return BadRequest(new { invalid = "Email or password is incorrect." });
        }
        var token = await _jwtTokenService.CreateToken(user);
        return Ok(new { token });
    }
    [HttpPost("register")]
    public async Task<IActionResult> Register([FromBody] RegisterUserViewModel model)
    {
        var user = new UserEntity
        {
            FirstName = model.FirstName,
            LastName = model.LastName,
            Email = model.Email,
            UserName = model.Email,
            Image = model.ImageBase64
        };
        var result = await _userManager.CreateAsync(user, model.Password);
        if (!result.Succeeded)
        {
            return BadRequest(result.Errors);
        }
        await _userManager.AddToRoleAsync(user, Roles.User);
        var token = await _jwtTokenService.CreateToken(user);
        return Ok(new { token });
    }
}