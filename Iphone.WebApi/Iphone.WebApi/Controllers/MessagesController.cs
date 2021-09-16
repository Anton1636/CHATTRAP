using System.Linq;
using System.Threading.Tasks;
using Iphone.Domain;
using Iphone.EFData;
using Microsoft.AspNetCore.Mvc;

namespace Iphone.WebApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class MessagesController : BaseController
    {
        private readonly EFDataContext _context;

        public MessagesController(EFDataContext context)
        {
            _context = context;
        }

        [HttpGet("getMessage")]
        public IActionResult GetData()
        {
            var message = _context.Messages.Select(x => new
            {
                Messages = x.Messages,
                SenderId = x.SenderId,
                ImageUrl = x.ImageUrl,
                Receiver = x.Receiver,
                Url = x.Url,
                Type = x.Type,
                timestamp = x.timestamp,
                feeling = x.feeling
            });
            return Ok(message);
        }

        [HttpPost("setMessage")]
        public IActionResult SetDataAsync(MessageFragment message)
        {

            return Ok();
        }
    }
}