using Iphone.EFData;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Iphone.WebApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TestController : ControllerBase
    {
        private readonly EFDataContext _context;
        public TestController(EFDataContext context)
        {
            _context = context;
        }

        [HttpGet]
        public IActionResult GetData()
        {
            var list = _context.Users.Select(x=>new
            {
                Name=x.FullName,
                Phone = x.PhoneNo,
                Image=x.ProfileImage
            }).ToList();
            return Ok(list);
        }
    }
}
