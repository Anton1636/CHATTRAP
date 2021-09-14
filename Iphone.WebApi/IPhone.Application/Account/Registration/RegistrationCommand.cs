using MediatR;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IPhone.Application.Account.Registration
{
	public class RegistrationCommand : IRequest<UserViewModel>
	{
        public string FullName { get; set; }
        public string Username { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
        public string Date { get; set; }
        public string Gender { get; set; }
        public string PhoneNo { get; set; }
        public string ProfileImage { get; set; }
    }
}
