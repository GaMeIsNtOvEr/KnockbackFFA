package me.gameisntover.kbffa.command.subcommands.game;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.arena.ArenaManager;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.util.CommonUtils;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JoinCommand extends KnockCommand {
    public JoinCommand() {
        super("join");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "Players can use this command to join the game";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }

    @Override
    public String getSyntax() {
        return "/join";
    }

    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        Player p = knocker.getPlayer();
        if (ArenaManager.isInGame(p.getUniqueId()))
            p.sendMessage(Message.ALREADY_INGAME.toString());
        else {
            if (KnockbackFFA.getInstance().getArenaManager().getEnabledArena() == null)
                knocker.getPlayer().sendMessage(Message.NO_READY_ARENA.toString());
            else {
                String joinText = Message.ARENA_JOIN.toString();
                joinText = PlaceholderAPI.setPlaceholders(p, joinText);
                p.sendMessage(joinText);
                //TODO re-cache inventories: knocker.setInventory(p.getInventory());
                p.getInventory().clear();
                p.setFoodLevel(20);
                CommonUtils.giveLobbyItems(p);
                knocker.setScoreboardEnabled(true);
                ArenaManager.setInGame(p.getUniqueId(), true);
            }
            KnockbackFFA.getInstance().getArenaManager().teleportPlayerToArena(p);
        }
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }
}
