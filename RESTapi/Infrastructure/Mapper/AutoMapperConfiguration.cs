using Microsoft.Extensions.DependencyInjection;

namespace Infrastructure.Mapper;

public class AutoMapperConfiguration
{
    public static void Config(IServiceCollection services)
    {
        services.AddAutoMapper(typeof(AppMappingProfile));
    }
}