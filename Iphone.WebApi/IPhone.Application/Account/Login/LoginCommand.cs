using MediatR;

namespace IPhone.Application.Account.Login
{
    public class LoginCommand : IRequest<UserViewModel>
    {
        public string Password { get; set; }
        public string PhoneNo { get; set; }
    }
}
