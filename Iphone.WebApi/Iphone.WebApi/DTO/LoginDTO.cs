using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Iphone.WebApi.DTO
{
    public record LoginDTO
    {
        
        [Required(ErrorMessage = "Обовязкове поле"), EmailAddress(ErrorMessage = "Не валідна пошта")]
        public string Email { get; set; }
        [Required(ErrorMessage = "Обовязкове поле")]
        public string Password { get; set; }
    }
}
