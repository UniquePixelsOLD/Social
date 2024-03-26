package net.uniquepixels.social.friends.ui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.uniquepixels.core.paper.TextStyle;
import net.uniquepixels.core.paper.gui.UIRow;
import net.uniquepixels.core.paper.gui.UISlot;
import net.uniquepixels.core.paper.gui.UIStyle;
import net.uniquepixels.core.paper.gui.backend.UIHolder;
import net.uniquepixels.core.paper.gui.background.UIBackground;
import net.uniquepixels.core.paper.gui.exception.OutOfInventoryException;
import net.uniquepixels.core.paper.gui.item.UIItem;
import net.uniquepixels.core.paper.gui.types.chest.ChestUI;
import net.uniquepixels.core.paper.item.DefaultItemStackBuilder;
import net.uniquepixels.core.paper.item.skull.SkullItemStackBuilder;
import net.uniquepixels.social.friends.FriendManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FriendUI extends ChestUI {
  private final UIHolder uiHolder;
  private final FriendManager friendManager;

  public FriendUI(UIHolder uiHolder, FriendManager friendManager) {
    super(Component.translatable("friend.title").color(NamedTextColor.BLACK), UIRow.CHEST_ROW_3);
    this.uiHolder = uiHolder;
    this.friendManager = friendManager;
  }

  @Override
  public boolean allowItemMovementInOtherInventories() {
    return false;
  }

  @Override
  protected void initItems(Player opener) throws OutOfInventoryException {

    Locale locale = opener.locale();

    item(new UIItem(new DefaultItemStackBuilder<>(Material.LIME_SHULKER_BOX)
      .displayName(UIStyle.UI_ARROW.append(UIStyle.translate(locale, "friend.friends").color(TextStyle.WHITE_COLOR)))
      .addLoreLine(UIStyle.leftClick(locale).append(UIStyle.MINUS)
        .append(UIStyle.translate(locale, "friend.friends.lore").color(NamedTextColor.GRAY)))
      .buildItem(), UISlot.SLOT_11), (clicker, clickedItem, action, event) -> {

      this.friendManager.addFriend(clicker.getUniqueId(), UUID.fromString("07de5039-b85f-4593-b720-dd56cb5dade8"));

      return true;
    });

    item(new UIItem(new SkullItemStackBuilder(Material.PLAYER_HEAD)
      .setSkullOwner(opener)
      .displayName(UIStyle.UI_ARROW.append(Component.text(opener.getName()).color(TextStyle.WHITE_COLOR)))
      .addLoreLine(UIStyle.translate(locale, "friend.info.amount")
        .style(builder -> builder.decoration(TextDecoration.ITALIC, false))
        .append(UIStyle.MINUS)
        .append(Component.text(0).color(NamedTextColor.GRAY)))
      .buildItem(), UISlot.SLOT_13), (clicker, clickedItem, action, event) -> {


      return true;
    });

    item(new UIItem(new DefaultItemStackBuilder<>(Material.ANVIL)
      .displayName(UIStyle.UI_ARROW.append(UIStyle.translate(locale, "friend.settings").color(TextStyle.WHITE_COLOR)))
      .addLoreLine(UIStyle.leftClick(locale).append(UIStyle.MINUS)
        .append(UIStyle.translate(locale, "friend.settings.lore").color(NamedTextColor.GRAY)))
      .buildItem(), UISlot.SLOT_15), (clicker, clickedItem, action, event) -> {


      return true;
    });


    setBackground(new UIBackground(UIBackground.BackgroundType.FULL,
      List.of(new UIItem(new DefaultItemStackBuilder<>(Material.GRAY_STAINED_GLASS_PANE)
        .displayName(Component.empty())
        .addFlags(ItemFlag.values())
        .buildItem(), UISlot.SLOT_4))));

  }

  @Override
  public void onClose(Player player) {

  }
}
