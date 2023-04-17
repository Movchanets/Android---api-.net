using AutoMapper;
using DAL.Entities;
using Infrastructure.Models;

namespace Infrastructure.Mapper;

public class AppMappingProfile : Profile
{
  

    public AppMappingProfile()
    {   
        CreateMap<CategoryEntity, CategoryItemVm>();
        CreateMap<CategoryEntity, CreateCategoryVm>()
            .ForMember(x => x.ImageBase64, opt => opt.MapFrom(x => $"/images/{x.Image}"));

        CreateMap<CreateCategoryVm, CategoryEntity>()
            .ForMember(x => x.Image, opt => opt.Ignore())
            .ForMember(x => x.DateCreated,
                opt => opt.MapFrom(x => DateTime.SpecifyKind(DateTime.Now, DateTimeKind.Utc)));
  
    }

    
}