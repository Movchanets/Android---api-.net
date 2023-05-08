using DAL.Identity;

namespace Infrastructure.Interfaces;

public interface IJwtTokenService
{
    Task<string> CreateToken(UserEntity user);
}