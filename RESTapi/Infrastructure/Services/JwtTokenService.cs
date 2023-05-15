using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using DAL.Identity;
using Infrastructure.Interfaces;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;

namespace Infrastructure.Services;

public class JwtTokenService : IJwtTokenService
{
    private readonly IConfiguration _configuration;
    private readonly UserManager<UserEntity> _userManager;
    public JwtTokenService(IConfiguration configuration, UserManager<UserEntity> userManager)
    {
        _configuration = configuration;
        _userManager = userManager;
    }

    public async Task<string> CreateToken(UserEntity user)
    {
        var roles = await _userManager.GetRolesAsync(user);
        var claims = new List<Claim>()
        {
            new Claim("email", user.Email),
            new Claim("firstName",user.FirstName),
            new Claim("lastName",user.LastName),
            new Claim("image", user.Image??"user.jpg")
        };
        foreach(var role in roles)
        {
            claims.Add(new Claim("roles", role));
        }
        var signinKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration.GetValue<String>("JWTSecretKey")));
        var signinCredentials = new SigningCredentials(signinKey, SecurityAlgorithms.HmacSha256);

        var jwt = new JwtSecurityToken(
            signingCredentials: signinCredentials,
            expires: DateTime.Now.AddDays(10),
            claims: claims
        );
        return new JwtSecurityTokenHandler().WriteToken(jwt);
    }
}
