using DAL;
using DAL.Interfaces;
using DAL.Repositories;
using Infrastructure.Interfaces;
using Infrastructure.Mapper;
using Infrastructure.Services;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.FileProviders;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddDbContext<AppEFContext>(opt =>
    opt.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));
builder.Services.AddScoped<ICategoryRepository, CategoryRepository>();
builder.Services.AddScoped<ICategoryService, CategoryService>();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddAutoMapper(typeof(AppMappingProfile));
builder.Services.AddControllers();
AutoMapperConfiguration.Config(builder.Services);
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}


app.UseAuthorization();
var dir = Path.Combine(Directory.GetCurrentDirectory(), "images");
if (!Directory.Exists(dir))
    Directory.CreateDirectory(dir);

app.UseStaticFiles(new StaticFileOptions
{
    FileProvider = new PhysicalFileProvider(dir),
    RequestPath="/images"
});
app.MapControllers();
app.UseCors(options => options.AllowAnyOrigin().AllowAnyHeader().AllowAnyMethod()
    // .WithOrigins(new[] { "http://localhost:3000"})
    // .AllowAnyHeader()
    // .AllowCredentials()
);
app.SeedData();
app.Run();