

using DAL.Entities;
using Microsoft.EntityFrameworkCore;


namespace DAL
{
    public class AppEFContext : DbContext
    {
        public AppEFContext(DbContextOptions<AppEFContext> options) : base(options)
        {
            
        }
    public DbSet<CategoryEntity> Categories { get; set; }
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);
        }
    }
}