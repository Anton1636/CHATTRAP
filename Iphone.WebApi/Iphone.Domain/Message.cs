using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Iphone.Domain
{
    public class Message 
    {
        [Key]
        public int Id { get; set; }

        [Required, StringLength(1000)]
        public string Messages { get; set; }

        [ForeignKey("Sender")]
        public long SenderId { get; set; }
        public string ImageUrl { get; set; }
        public string Receiver { get; set; }
        public string Url { get; set; }
        public string Type { get; set; }
        public long timestamp { get; set; }
        public int feeling { get; set; }

        public virtual AppUser Sender { get; set; }
    }
}