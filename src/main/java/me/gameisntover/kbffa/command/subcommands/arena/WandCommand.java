package me.gameisntover.kbffa.command.subcommands.arena;

import me.gameisntover.kbffa.api.ReworkedKnocker;
import me.gameisntover.kbffa.command.KnockCommand;
import me.gameisntover.kbffa.util.Items;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WandCommand extends KnockCommand {
    public WandCommand() {
        super("wand");
    }

    @Override
    public @NotNull String getKnockDescription() {
        return "gives player position selector wand";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.OP;
    }

    @Override
    public String getSyntax() {
        return "/wand";
    }

    @Override
    public void perform(ReworkedKnocker knocker, String[] args) {
        ItemStack wand = Items.POSITION_SELECTOR_WAND.getItem();
        knocker.getPlayer().getInventory().addItem(wand);
    }

    @Override
    public List<String> performTab(ReworkedKnocker knocker, String[] args) {
        return null;
    }
}
