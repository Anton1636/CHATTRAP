using Microsoft.AspNetCore.Identity;
using System.Collections.Generic;

namespace Iphone.Domain
{
    public class AppUser : IdentityUser<long>
    {
        public string FullName { get; set; }
        public string Date { get; set; }
        public string Gender { get; set; }
        public string PhoneNo { get; set; }
        public string ProfileImage { get; set; }
        public virtual ICollection<AppUserRole> UserRoles { get; set; }
    }
}
