package me.gameisntover.kbffa.command.subcommands.kit;

import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.command.SubCommand;
import me.gameisntover.kbffa.customconfig.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class DelKitCommand extends SubCommand {
    public DelKitCommand(String name) {
        super(name);
    }

    @Override
    public @NotNull String getSubName() {
        return "delkit";
    }

    @Override
    public @NotNull String getSubDescription() {
        return ChatColor.AQUA + "Deletes the specified kit";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/delkit <kitname>";
    }


    @Override
    public void perform(Knocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (args.length != 1){
            p.sendMessage(ChatColor.RED + "Usage: /delkit <kitname>");
        return;
        }
        else{
            Kits.getfile().delete();
            p.sendMessage(ChatColor.GREEN + "I've deleted the kit " + args[0] + "!");
        }
    }

    @Override
    public List<String> performTab(Knocker knocker, String[] args) {
        return Arrays.asList(Arrays.stream(new File(KnockbackFFA.getINSTANCE().getDataFolder(), "Kits" + File.separator).list()).map(s -> s.replace(".yml", "")).toArray(String[]::new));
    }
}