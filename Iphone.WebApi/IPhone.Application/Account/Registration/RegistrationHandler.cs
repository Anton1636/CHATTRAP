using Iphone.Domain;
using Iphone.EFData;
using IPhone.Application.Exceptions;
using IPhone.Application.Interfaces;
using MediatR;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;
using System.Net;
using System.Threading;
using System.Threading.Tasks;

namespace IPhone.Application.Account.Registration
{
    public class RegistrationHandler : IRequestHandler<RegistrationCommand, UserViewModel>
	{
		private readonly UserManager<AppUser> _userManager;
		private readonly IJwtGenerator _jwtGenerator;
		private readonly EFDataContext _context;

		public RegistrationHandler(EFDataContext context, UserManager<AppUser> userManager, IJwtGenerator jwtGenerator)
		{
			_context = context;
			_userManager = userManager;
			_jwtGenerator = jwtGenerator;
		}

		public async Task<UserViewModel> Handle(RegistrationCommand request, CancellationToken cancellationToken)
		{
			if (await _context.Users.Where(x => x.Email == request.Email).AnyAsync())
			{
				throw new RestException(HttpStatusCode.BadRequest, new { Email = new[] { "Email already exist" } });
			}

			//if (await _context.Users.Where(x => x.UserName == request.UserName).AnyAsync())
			//{
			//	throw new RestException(HttpStatusCode.BadRequest, new { UserName = "UserName already exist" });
			//}

			var user = new AppUser
			{
				FullName = request.FullName,
				UserName = request.Username,
				Email = request.Email,
				//Password = request.Password,
				Date = request.Date,
				Gender = request.Gender,
				PhoneNo = request.PhoneNo,
				ProfileImage = request.ProfileImage
			};

			var result = await _userManager.CreateAsync(user, request.Password);

			if (result.Succeeded)
			{
				return new UserViewModel
				{
					Token = _jwtGenerator.CreateToken(user),
					FullName = user.UserName,
					ProfileImage = null,
					PhoneNo = user.PhoneNo
				};
			}

			throw new Exception("Client creation failed");
		}
	}
}
