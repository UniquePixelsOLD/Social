package net.uniquepixels.social.friends;

import net.uniquepixels.core.paper.gui.backend.UIHolder;
import net.uniquepixels.social.friends.ui.FriendUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FriendCommand implements CommandExecutor {

    private final UIHolder uiHolder;

    public FriendCommand(UIHolder uiHolder) {
        this.uiHolder = uiHolder;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("not supported");
            return true;
        }

        this.uiHolder.open(new FriendUI(this.uiHolder), player);

        return true;
    }
}
