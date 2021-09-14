using Iphone.Domain;
using Iphone.EFData;
using Iphone.Infrastructure.Helpers;
using IPhone.Application.Account;
using IPhone.Application.Exceptions;
using IPhone.Application.Interfaces;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace Iphone.Infrastructure.Services
{
    public class UserService : IUserService
    {
        private readonly EFDataContext _context;
        public UserService(EFDataContext context)
        {
            _context = context;
        }
        public UserViewModel ChageUserImage(string userName, string image)
        {
            var user = _context.Users.SingleOrDefault(x=>x.UserName==userName);
            if (user == null)
            {
                throw new RestException(HttpStatusCode.NotFound);
            }

            string filename = Guid.NewGuid().ToString() + ".jpg";
            string path = Path.Combine(Directory.GetCurrentDirectory(), "ProfileImages");
            if (!Directory.Exists(path))
            {
                Directory.CreateDirectory(path);
            }
            path = Path.Combine(path, filename);
            if (string.IsNullOrEmpty(image))
            {
                return null;
            }

            using (Bitmap b = ImageWorker.Base64StringToBitmap(image))
            {
                Bitmap savedImage = ImageWorker.CreateImage(b, 400, 360);
                if (savedImage != null)
                {
                    savedImage.Save(path, ImageFormat.Jpeg);
                    user.ProfileImage = filename;
                    _context.SaveChanges();
                    return null;
                }
                else
                {
                    return null;
                }
            };
        }
    }
}
