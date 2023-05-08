using System.Text;
using DAL;
using DAL.Identity;
using DAL.Interfaces;
using DAL.Repositories;
using FluentValidation.AspNetCore;
using Infrastructure.Interfaces;
using Infrastructure.Mapper;
using Infrastructure.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.FileProviders;
using Microsoft.IdentityModel.Tokens;

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
var signinKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(builder.Configuration.GetValue<String>("JWTSecretKey")));

builder.Services.AddAuthentication(options =>
{
    options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
    options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
}).AddJwtBearer(cfg =>
{
    cfg.RequireHttpsMetadata = false;
    cfg.SaveToken = true;
    cfg.TokenValidationParameters = new TokenValidationParameters()
    {
        IssuerSigningKey = signinKey,
        ValidateAudience = false,
        ValidateIssuer = false,
        ValidateLifetime = true,
        ValidateIssuerSigningKey = true,
        ClockSkew = TimeSpan.Zero
    };
});

builder.Services.AddIdentity<UserEntity, RoleEntity>(options =>
{
    options.Stores.MaxLengthForKeys = 128;
    options.Password.RequireDigit = false;
    options.Password.RequiredLength = 5;
    options.Password.RequireNonAlphanumeric = false;
    options.Password.RequireUppercase = false;
    options.Password.RequireLowercase = false;
}).AddEntityFrameworkStores<AppEFContext>().AddDefaultTokenProviders();
AutoMapperConfiguration.Config(builder.Services);
builder.Services.AddScoped<IJwtTokenService, JwtTokenService>();
builder.Services.AddFluentValidation(x => x.RegisterValidatorsFromAssemblyContaining<Program>());
var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseAuthentication();
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