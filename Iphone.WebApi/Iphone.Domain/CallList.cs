using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Iphone.Domain
{
    public class CallList : IdentityUser<long>
    {
        public string UserID { get; set; }
        public string UserName { get; set; }
        public string Date { get; set; }
        public string UrlProfile { get; set; }
        public string CallType { get; set; }
    }
}
