using Iphone.Domain;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Iphone.EFData
{
    public class EFDataContext : IdentityDbContext<AppUser, AppRole, long, IdentityUserClaim<long>,
                    AppUserRole, IdentityUserLogin<long>,
                    IdentityRoleClaim<long>, IdentityUserToken<long>>
    {
        public EFDataContext(DbContextOptions<EFDataContext> options)
            : base(options)
        {

        }
        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            builder.Entity<AppUserRole>(userRole =>
            {
                userRole.HasKey(ur => new { ur.UserId, ur.RoleId });

                userRole.HasOne(ur => ur.Role)
                    .WithMany(r => r.UserRoles)
                    .HasForeignKey(ur => ur.RoleId)
                    .IsRequired();

                userRole.HasOne(ur => ur.User)
                    .WithMany(r => r.UserRoles)
                    .HasForeignKey(ur => ur.UserId)
                    .IsRequired();
            });
        }

        //public DbSet<CallList> CallLists { get; set; }
        //public DbSet<Message> Messages { get; set; }
        //public DbSet<MessagesList> MessagesLists { get; set; }
        //public DbSet<Status> Status { get; set; }
        //public DbSet<UserStatus> UserStatus { get; set; }
    }
}
