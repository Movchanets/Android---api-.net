using DAL.Entities;

using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DAL.Interfaces;

namespace DAL.Repositories
{

    public class GenericRepository<TEntity> : IGenericRepository<TEntity, int>
        where TEntity : class, IEntity<int>
    {
        private protected readonly AppEFContext _dbContext;

        public GenericRepository(AppEFContext context)
        {
            _dbContext = context;
        }

        
        public async Task<TEntity?> GetByName(string name)
        {
            return await _dbContext.Set<TEntity>()
                .AsNoTracking().Where(e => e.IsDelete == false)
                .FirstOrDefaultAsync(e => e.Name == name);
        }

        

        public async Task Create(TEntity entity)
        {
            await _dbContext.Set<TEntity>().AddAsync(entity);
            await _dbContext.SaveChangesAsync();
        }

        public async Task Delete(int id)
        {
            var entity = await GetById(id);
            entity.IsDelete = true;
            await _dbContext.SaveChangesAsync();
        }

        public IQueryable<TEntity> GetAll()
        {
            return _dbContext.Set<TEntity>().AsNoTracking().Where(i=>i.IsDelete==false);
        }

        public async Task<TEntity?> GetById(int id)
        {
            return await _dbContext.Set<TEntity>()
                .FirstOrDefaultAsync(e => e.Id == id);
        }

        public async Task Update(TEntity entity)
        {
            _dbContext.Set<TEntity>().Update(entity);
            await _dbContext.SaveChangesAsync();
        }
    }
}