using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Iphone.Domain
{
    public partial class Status : IdentityUser<long>
    {
        public string ImageUrl { get; set; }
        public long TimeStamp { get; set; }

        public virtual ICollection<AppUserRole> UserRoles { get; set; }
    }
}
