using FluentValidation;
using IPhone.Application.Validators;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace IPhone.Application.Account.Registration
{
	public class RegistrationValidation : AbstractValidator<RegistrationCommand>
	{
		public RegistrationValidation()
		{
			//RuleFor(x => x.FullName).NotEmpty();
			//RuleFor(x => x.Username).NotEmpty();
			//RuleFor(x => x.Email).NotEmpty().EmailAddress();
			//RuleFor(x => x.Password).NotEmpty().Password();
			//RuleFor(x => x.Date).NotEmpty();
			//RuleFor(x => x.Gender).NotEmpty();
			//RuleFor(x => x.PhoneNo).NotEmpty();
			//RuleFor(x => x.ProfileImage);
		}
	}
}
