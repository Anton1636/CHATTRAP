using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IPhone.Application.Account.Login
{
    public class LoginCommand : IRequest<UserViewModel>
    {
        public string Password { get; set; }
        public string PhoneNo { get; set; }
    }
}
