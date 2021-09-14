using Iphone.Domain;
using IPhone.Application.Exceptions;
using IPhone.Application.Interfaces;
using MediatR;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace IPhone.Application.Account.Login
{
    public class LoginHandler : IRequestHandler<LoginCommand, UserViewModel>
    {
        private readonly UserManager<AppUser> _userManager;
        private readonly SignInManager<AppUser> _signInManager;
        private readonly IJwtGenerator _jwtGenerator;

        public LoginHandler(UserManager<AppUser> userManager,
            SignInManager<AppUser> signInManager,
            IJwtGenerator jwtGenerator)
        {
            _userManager = userManager;
            _signInManager = signInManager;
            _jwtGenerator = jwtGenerator;
        }
        public async Task<UserViewModel> Handle(LoginCommand request, CancellationToken cancellationToken)
        {
            var user = _userManager.Users.FirstOrDefault(x => x.PhoneNo == request.PhoneNo);//.FindByEmailAsync(request.PhoneNo);
            if(user == null)
            {
                throw new RestException(HttpStatusCode.Unauthorized, new { Invalid = new[] { "The data is incorrect !" } });
            }
            var result = await _signInManager
                .CheckPasswordSignInAsync(user, request.Password, false);

            if(result.Succeeded)
            {
                return new UserViewModel
                {
                    //DisplayName = user.DisplayName,
                    Token = _jwtGenerator.CreateToken(user),
                    Username = user.UserName,
                    ProfileImage = null
                };
            }

            throw new RestException(HttpStatusCode.Unauthorized, new { Invalid = new[] { "The data is incorrect !" } });
        }
    }
}
