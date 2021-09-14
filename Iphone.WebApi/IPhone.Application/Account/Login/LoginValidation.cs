using FluentValidation;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

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
