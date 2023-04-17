

using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DAL.Entities;

[Table("tblCategories")]
public class CategoryEntity : BaseEntity<int>
{
     
    [StringLength(255)]
    public string Image { get; set; }
    public int Priority { get; set; }
    [StringLength(4000)]
    public string Description { get; set; }
}