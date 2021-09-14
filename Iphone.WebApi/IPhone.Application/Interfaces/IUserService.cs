using IPhone.Application.Account;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IPhone.Application.Interfaces
{
    public interface IUserService
    {
        UserViewModel ChageUserImage(string userName, string image);
    }
}
