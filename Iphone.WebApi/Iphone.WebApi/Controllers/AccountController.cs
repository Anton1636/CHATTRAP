using Iphone.WebApi.DTO;
using Iphone.WebApi.Helpers;
using IPhone.Application.Account;
using IPhone.Application.Account.Login;
using IPhone.Application.Account.Registration;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;

namespace Iphone.WebApi.Controllers
{
    //[Route("api/[controller]")]
    //[Produces("application/json")]
    //public class AccountController : ControllerBase
    //{
    //    private UserDBContext _context;

    //    public AccountController(UserDBContext context)
    //    {
    //        _context = context;
    //    }

    //    [HttpPost]
    //    [Route("login")]
    //    public async Task<IActionResult> Login([FromBody]LoginDTO model)
    //    {
    //        Thread.Sleep(2000);
    //        if(!ModelState.IsValid)
    //        {
    //            var errors = CustomValidator.GetErrorsByModel(ModelState);
    //            return BadRequest(errors);
    //        }

    //        if(!_context.Logins.Any(x=> x.Email == model.Email))
    //        {
    //            //var errors = CustomValidator.GetErrorsByModel(ModelState);
    //            return BadRequest(new
    //            {
    //                invalid="Bad request"
    //            });
    //        }
    //        return Ok(new
    //        {
    //            text="Ковбаса"
    //        });
    //    }
    //}

    [AllowAnonymous]
    public class AccountController : BaseController
    {
        [HttpPost("login")]
        public async Task<ActionResult<UserViewModel>> LoginAsync(LoginCommand query)
        {
            return await Mediator.Send(query);
        }

        [HttpPost("registration")]
        public async Task<ActionResult<UserViewModel>> RegistrationAsync(RegistrationCommand command)
        {
            return await Mediator.Send(command);
        }
    }

}
