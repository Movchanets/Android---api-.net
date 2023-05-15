namespace Infrastructure.Models;

public class EditAccountVM
{
    public string Email { get; set; }
    public string FirstName { get; set; }
    public string LastName { get; set; }
    public string ImageBase64 { get; set; }
    public string password { get; set; }
    public string? newPassword { get; set; }
}