using IPhone.Application.Account;
using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IPhone.Application.User.Profile
{
    public class UserProfileCommand : IRequest<UserViewModel>
    {
        public string UserName { get; set; }
    }
}
