package gc.arcaniax.gobrush.command;

import com.sk89q.worldedit.IncompleteRegionException;
import gc.arcaniax.gobrush.Main;
import gc.arcaniax.gobrush.Session;
import gc.arcaniax.gobrush.object.Brush;
import gc.arcaniax.gobrush.object.BrushPlayer;
import gc.arcaniax.gobrush.object.Config;
import gc.arcaniax.gobrush.object.HeightMapExporter;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Cmd
  implements CommandExecutor
{
  private static final String prefix = "§bgoBrush> ";
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ((cmd.getName().equalsIgnoreCase("gobrush")) || (cmd.getName().equalsIgnoreCase("gb")))
    {
      if (!(sender instanceof Player)) {
        return false;
      }
      final Player p = (Player)sender;
      BrushPlayer bp = Session.getBrushPlayer(p.getUniqueId());
      if (!p.hasPermission("gobrush.use"))
      {
        p.sendMessage("§bgoBrush> §cyou are not creative enough, sorry.");
        return true;
      }
      if (args.length == 0)
      {
        if (p.hasPermission("gobrush.admin"))
        {
          p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§creload§7|§cexport§7|§cinfo ");
          return true;
        }
        if (p.hasPermission("gobrush.export"))
        {
          p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§cexport§7|§cinfo ");
          return true;
        }
        p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§cinfo ");
        return true;
      }
      if (args.length == 1)
      {
        if ((args[0].equalsIgnoreCase("size")) || (args[0].equalsIgnoreCase("s")))
        {
          p.sendMessage("§bgoBrush> §c/gb size [number]");
          return true;
        }
        if ((args[0].equalsIgnoreCase("intensity")) || (args[0].equalsIgnoreCase("i")))
        {
          p.sendMessage("§bgoBrush> §c/gb intensity [number]");
          return true;
        }
        if ((args[0].equalsIgnoreCase("brush")) || (args[0].equalsIgnoreCase("b")))
        {
          p.sendMessage("§bgoBrush> §c/gb brush [fileName]");
          return true;
        }
        if (((args[0].equalsIgnoreCase("export")) || (args[0].equalsIgnoreCase("e"))) && (p.hasPermission("gobrush.export")))
        {
          p.sendMessage("§bgoBrush> §c/gb export [fileName]");
          return true;
        }
        if ((args[0].equalsIgnoreCase("toggle")) || (args[0].equalsIgnoreCase("t")))
        {
          if (bp.isBrushEnabled())
          {
            bp.toggleBrushEnabled();
            p.sendMessage("§bgoBrush> §cdisabled brush");
          }
          else
          {
            bp.toggleBrushEnabled();
            p.sendMessage("§bgoBrush> §aenabled brush");
          }
          return true;
        }
        if (((args[0].equalsIgnoreCase("reload")) || (args[0].equalsIgnoreCase("r"))) && (p.hasPermission("gobrush.admin")))
        {
          Main.getPlugin().reloadConfig();
          Session.getConfig().reload(Main.getPlugin().getConfig());
          int amountOfValidBrushes = Session.initializeValidBrushes();
          Main.getPlugin().getLogger().log(Level.INFO, "Registered {0} brushes.", Integer.valueOf(amountOfValidBrushes));
          Session.initializeBrushMenu();
          Session.initializeBrushPlayers();
          p.sendMessage("§bgoBrush> §areloaded");
          return true;
        }
        if ((args[0].equalsIgnoreCase("info")) || (args[0].equalsIgnoreCase("i")))
        {
          p.spigot().sendMessage(new ComponentBuilder("goBrush> ").color(ChatColor.AQUA)
            .append("Created by: ").color(ChatColor.GOLD)
            .append("Arcaniax").color(ChatColor.YELLOW)
            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/Arcaniax")).create());
          
          p.spigot().sendMessage(new ComponentBuilder("goBrush> ").color(ChatColor.AQUA)
            .append("Sponsored by: ").color(ChatColor.GOLD)
            .append("@goCreativeMC").color(ChatColor.YELLOW)
            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/goCreativeMC")).create());
          
          p.sendMessage("§bgoBrush> §6Plugin download: §ehttp://pmc.la/Ta5aG");
          
          p.spigot().sendMessage(new ComponentBuilder("goBrush> ").color(ChatColor.AQUA)
            .append("More brushes: ").color(ChatColor.GOLD)
            .append("Click here").color(ChatColor.YELLOW)
            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://sellfy.com/Aerios")).create());
          
          return true;
        }
        if (p.hasPermission("gobrush.admin"))
        {
          p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§creload§7|§cexport§7|§cinfo ");
          return true;
        }
        if (p.hasPermission("gobrush.export"))
        {
          p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§cexport§7|§cinfo ");
          return true;
        }
        p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§cinfo ");
        return true;
      }
      if (args.length == 2)
      {
        if ((args[0].equalsIgnoreCase("size")) || (args[0].equalsIgnoreCase("s"))) {
          try
          {
            Integer sizeAmount = Integer.valueOf(Integer.parseInt(args[1]));
            Integer localInteger1;
            if (sizeAmount.intValue() > bp.getMaxBrushSize())
            {
              sizeAmount = Integer.valueOf(bp.getMaxBrushSize());
            }
            else if (sizeAmount.intValue() < 5)
            {
              sizeAmount = Integer.valueOf(5);
            }
            else if (sizeAmount.intValue() % 2 == 0)
            {
              localInteger1 = sizeAmount;Integer localInteger2 = sizeAmount = Integer.valueOf(sizeAmount.intValue() + 1);
            }
            bp.setBrushSize(sizeAmount.intValue());
            p.sendMessage("§bgoBrush> §6size set to: §e" + sizeAmount);
            bp.getBrush().resize(sizeAmount.intValue());
            
            return true;
          }
          catch (Exception e)
          {
            p.sendMessage("§bgoBrush> §c/gb size [number]");
            return true;
          }
        }
        if ((args[0].equalsIgnoreCase("intensity")) || (args[0].equalsIgnoreCase("i"))) {
          try
          {
            Integer intensityAmount = Integer.valueOf(Integer.parseInt(args[1]));
            if (intensityAmount.intValue() > bp.getMaxBrushIntensity()) {
              intensityAmount = Integer.valueOf(bp.getMaxBrushIntensity());
            } else if (intensityAmount.intValue() < 1) {
              intensityAmount = Integer.valueOf(1);
            }
            bp.setBrushIntensity(intensityAmount.intValue());
            p.sendMessage("§bgoBrush> §6intensity set to: §e" + intensityAmount);
            return true;
          }
          catch (Exception e)
          {
            p.sendMessage("§bgoBrush> §c/gb intensity [number]");
            return true;
          }
        }
        if ((args[0].equalsIgnoreCase("brush")) || (args[0].equalsIgnoreCase("b")))
        {
          String name = args[1].replace("_", " ");
          if (Session.containsBrush(name))
          {
            int size = bp.getBrushSize();
            Brush brush = Session.getBrush(name);
            bp.setBrush(brush);
            bp.getBrush().resize(size);
            p.sendMessage("§bgoBrush> §6brush set to: §e" + name);
            return true;
          }
          p.sendMessage("§bgoBrush> §ccould not load brush \"" + name + "\"");
          return true;
        }
        if (((args[0].equalsIgnoreCase("export")) || (args[0].equalsIgnoreCase("e"))) && (p.hasPermission("gobrush.export")))
        {
          final String name = args[1];
          Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable()
          {
            public void run()
            {
              HeightMapExporter hm = null;
              try
              {
                hm = new HeightMapExporter(p);
              }
              catch (IncompleteRegionException e)
              {
                p.sendMessage("§bgoBrush> §cPlease make a worledit selection");
                return;
              }
              if (!hm.hasWorldEditSelection())
              {
                p.sendMessage("§bgoBrush> §cPlease make a worledit selection");
                return;
              }
              hm.exportImage(500, name);
              p.sendMessage("§bgoBrush> §6Exported §e" + name + ".png");
              Session.initializeValidBrushes();
              Session.initializeBrushMenu();
            }
          });
          return true;
        }
        if (p.hasPermission("gobrush.admin"))
        {
          p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§creload§7|§cexport§7|§cinfo ");
          return true;
        }
        if (p.hasPermission("gobrush.export"))
        {
          p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§cexport§7|§cinfo ");
          return true;
        }
        p.sendMessage("§bgoBrush> §c/gb size§7|§cintensity§7|§cbrush§7|§ctoggle§7|§cinfo ");
        return true;
      }
    }
    return false;
  }
}
