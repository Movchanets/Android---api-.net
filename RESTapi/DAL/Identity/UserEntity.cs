using System.ComponentModel.DataAnnotations;
using DAL.Entities;
using Microsoft.AspNetCore.Identity;

namespace DAL.Identity;

public class UserEntity : IdentityUser<int>
{
    [StringLength(100)]
    public string FirstName { get; set; }
    [StringLength(100)]
    public string LastName { get; set; }
    [StringLength(255)]
    public string? Image { get; set; }
    public virtual ICollection<UserRoleEntity> UserRoles { get; set; }
    public virtual ICollection<CategoryEntity> Categories { get; set; }
   }
