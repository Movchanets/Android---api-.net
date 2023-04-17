namespace DAL;
using Microsoft.EntityFrameworkCore;
public static class SeederDB
{
    public static void SeedData(this IApplicationBuilder app)
    {
        using var scope = app.ApplicationServices
            .GetRequiredService<IServiceScopeFactory>().CreateScope();
        var context = scope.ServiceProvider.GetRequiredService<AppEFContext>();
        context.Database.Migrate();
    }
}