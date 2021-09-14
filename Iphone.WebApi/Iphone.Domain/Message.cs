using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Iphone.Domain
{
    public class Message : IdentityUser<long>
    {
        public string Messages { get; set; }
        public string SenderId { get; set; }
        public string ImageUrl { get; set; }
        public string Receiver { get; set; }
        public string Url { get; set; }
        public string Type { get; set; }
        public long timestamp { get; set; }
        public int feeling { get; set; }
    }
}
