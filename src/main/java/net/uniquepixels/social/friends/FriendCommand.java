package net.uniquepixels.social.friends;

import net.uniquepixels.core.paper.gui.backend.UIHolder;
import net.uniquepixels.coreapi.player.PlayerManager;
import net.uniquepixels.social.friends.ui.FriendUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendCommand implements CommandExecutor {

  private final UIHolder uiHolder;
  private final FriendManager friendManager;
  private final PlayerManager playerManager;

  public FriendCommand(UIHolder uiHolder, FriendManager friendManager, PlayerManager playerManager) {
    this.uiHolder = uiHolder;
    this.friendManager = friendManager;
    this.playerManager = playerManager;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

    if (!(sender instanceof Player player)) {
      sender.sendMessage("not supported");
      return true;
    }

    if (this.friendManager.createFriendDto(new FriendDto(player.getUniqueId(), List.of()))) {
      player.sendMessage("Success!");
    }

    this.uiHolder.open(new FriendUI(this.uiHolder, this.friendManager, player, this.playerManager), player);

    return true;
  }
}
