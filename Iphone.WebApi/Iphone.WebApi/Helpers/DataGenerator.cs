using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Iphone.WebApi.Helpers
{
    public class DataGenerator
    {
        public static void Initialize(IServiceProvider serviceProvider)
        {
            using (var context = new UserDBContext(
                serviceProvider.GetRequiredService<DbContextOptions<UserDBContext>>()))
            {
                // Look for any board games.
                if (context.Logins.Any())
                {
                    return;   // Data was already seeded
                }

                context.Logins.AddRange(
                    new Models.Login
                    {
                        Email = "1@gmail.com",
                        Password = "1"
                    },
                    new Models.Login
                    {
                        Email = "2@gmail.com",
                        Password = "2"
                    },
                    new Models.Login
                    {
                        Email = "3@gmail.com",
                        Password = "3"
                    }); ;

                context.SaveChanges();
            }
        }
    }
}
