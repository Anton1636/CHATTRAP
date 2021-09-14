using Iphone.Domain;
using IPhone.Application.Account;
using IPhone.Application.Exceptions;
using MediatR;
using Microsoft.AspNetCore.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace IPhone.Application.User.Profile
{
    public class UserListHandler : IRequestHandler<UserListCommand, List<UserViewModel>>
    {
        private readonly UserManager<AppUser> _userManager;
        public UserListHandler(UserManager<AppUser> userManager)
        {
            _userManager = userManager;
        }
        public async Task<List<UserViewModel>> Handle(UserListCommand request, CancellationToken cancellationToken)
        {
            var users = _userManager.Users.Select(x => 
            new UserViewModel
            { 
            //DisplayName = x.DisplayName,
            Email = x.Email,
            PhoneNo = x.PhoneNo,
            Username = x.UserName,
            ProfileImage = "profile.jpg"}).ToList();
            if (users == null)
            {
                throw new RestException(HttpStatusCode.NotFound);
            }

            return users;
            //throw new RestException(HttpStatusCode.NotFound);
        }
    }
}
