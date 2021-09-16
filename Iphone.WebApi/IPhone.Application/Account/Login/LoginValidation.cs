using FluentValidation;

namespace IPhone.Application.Account.Login
{
    public class LoginValidation : AbstractValidator<LoginCommand>
    {
        public LoginValidation()
        {
            RuleFor(x => x.PhoneNo).NotEmpty().WithMessage("The field can’t be empty ");
            RuleFor(x => x.Password).NotEmpty().WithMessage("The field can’t be empty ");
        }
    }
}
