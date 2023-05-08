using System.ComponentModel.DataAnnotations.Schema;
using Microsoft.AspNetCore.Identity;

namespace DAL.Identity;

public class UserRoleEntity : IdentityUserRole<int>
{
   
    public virtual UserEntity User { get; set; }
    
    public virtual RoleEntity Role { get; set; }
}