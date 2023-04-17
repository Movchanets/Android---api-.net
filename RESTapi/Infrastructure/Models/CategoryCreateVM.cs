namespace Infrastructure.Models;

public class CategoryItemVm
{
    public string Name { get; set; }
    public string Image { get; set; }
    public string Description { get; set; }
   
}
public class CreateCategoryVm
{
    public string Name { get; set; }
    public string ImageBase64 { get; set; }   
    public int Priority { get; set; }
    public string Description { get; set; }
}