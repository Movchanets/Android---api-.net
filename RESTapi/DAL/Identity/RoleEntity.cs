using Microsoft.AspNetCore.Identity;

namespace DAL.Identity;

public class RoleEntity : IdentityRole<int>
{
    public virtual ICollection<UserRoleEntity> UserRoles { get; set; }
}