package net.uniquepixels.social.friends.ui;

import dev.s7a.base64.Base64ItemStack;
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
import net.uniquepixels.coreapi.ListPaginator;
import net.uniquepixels.social.friends.FriendDto;
import net.uniquepixels.social.friends.FriendManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class FriendUI extends ChestUI {
  private final UIHolder uiHolder;
  private final FriendManager friendManager;
  int page = 0;

  ListPaginator<UUID> paginator;

  public FriendUI(UIHolder uiHolder, FriendManager friendManager, Player player) {
    super(Component.translatable("friend.friends").color(NamedTextColor.DARK_GRAY), UIRow.CHEST_ROW_6);
    this.uiHolder = uiHolder;
    this.friendManager = friendManager;

    Optional<FriendDto> optional = this.friendManager.getFriendDto(player.getUniqueId());

    if (optional.isEmpty()) {
      player.closeInventory();
      player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 30f, 1f);
      player.sendMessage(UIStyle.PREFIX.append(Component.translatable("friends.error").color(NamedTextColor.RED)));
      return;
    }

    this.paginator = new ListPaginator<>(optional.get().friends());
  }

  @Override
  public boolean allowItemMovementInOtherInventories() {
    return false;
  }

  @Override
  protected void initItems(Player opener) throws OutOfInventoryException {

    Locale locale = opener.locale();

    if (this.paginator == null) {
      return;
    }

    HashMap<Integer, ArrayList<UUID>> maxSizePerPage = this.paginator.maxSizePerPage(36);

    if (!maxSizePerPage.containsKey(page)) {
      opener.playSound(opener.getLocation(), Sound.ENTITY_VILLAGER_NO, 30f, 1f);
      return;
    }

    ArrayList<UUID> page = maxSizePerPage.get(this.page);
    for (int i = 0; i < page.size(); i++) {

      UUID uuid = page.get(i);
      OfflinePlayer friend = Bukkit.getOfflinePlayer(uuid);

      item(new UIItem(new SkullItemStackBuilder(Material.PLAYER_HEAD)
        .setSkullOwner(friend)
        .displayName(UIStyle.UI_ARROW.append(this.generateItemName(friend)))
        .addLoreLine(Component.empty())
        .addLoreLine(this.generateOnlineOn(locale, friend))
        .buildItem(), UISlot.fromSlotId(i).orElse(UISlot.SLOT_0)), (player, uiItem, clickType, inventoryClickEvent) -> {

        return true;
      });

    }

    item(new UIItem(
      new DefaultItemStackBuilder<>(Base64ItemStack.decode("rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABHQAAj09dAABdnQABHR5cGV0AARtZXRhdXEAfgAGAAAABHQAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA50dAALUExBWUVSX0hFQURzcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAh0AAltZXRhLXR5cGV0AAxkaXNwbGF5LW5hbWV0AAtza3VsbC1vd25lcnVxAH4ABgAAAAR0AAhJdGVtTWV0YXQABVNLVUxMdACVeyJ0ZXh0IjoiIiwiZXh0cmEiOlt7InRleHQiOiJDb21tYW5kIEJsb2NrIiwib2JmdXNjYXRlZCI6ZmFsc2UsIml0YWxpYyI6ZmFsc2UsInVuZGVybGluZWQiOmZhbHNlLCJzdHJpa2V0aHJvdWdoIjpmYWxzZSwiY29sb3IiOiJibHVlIiwiYm9sZCI6ZmFsc2V9XX1zcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAh0AAh1bmlxdWVJZHQABG5hbWV0AApwcm9wZXJ0aWVzdXEAfgAGAAAABHQADVBsYXllclByb2ZpbGV0ACQwNDA0OWM5MC1kM2U5LTQ2MjEtOWNhZi0wMDAwYWFhNDIyNjF0AA1IZWFkIERhdGFiYXNlc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAAFzcgAXamF2YS51dGlsLkxpbmtlZEhhc2hNYXA0wE5cEGzA+wIAAVoAC2FjY2Vzc09yZGVyeHIAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAJxAH4AIHQACHRleHR1cmVzdAAFdmFsdWV0ALRleUowWlhoMGRYSmxjeUk2ZXlKVFMwbE9JanA3SW5WeWJDSTZJbWgwZEhBNkx5OTBaWGgwZFhKbGN5NXRhVzVsWTNKaFpuUXVibVYwTDNSbGVIUjFjbVV2T1RNd01tWmtNakl5TW1NMll6RXhNbVE0TlRrNU1tVXdPVFEzTnpjNU1EWmhNMk5tWkdNNE5EZzRPR0l6T0dObU9UWXlOamc1WWpFeFpURTFOMll4TnlKOWZYMD14AHg="))
        .displayName(UIStyle.UI_ARROW.append(UIStyle.translate(locale, "friend.settings").color(TextStyle.WHITE_COLOR).decorate(TextDecoration.BOLD)))
        .buildItem(), UISlot.SLOT_45
    ), (player, uiItem, clickType, inventoryClickEvent) -> {

      this.uiHolder.open(new FriendSettingsUI(this.uiHolder), player);

      return true;
    });

    item(new UIItem(
      new DefaultItemStackBuilder<>(Base64ItemStack.decode("rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABXQAAj09dAABdnQABHR5cGV0AAZhbW91bnR0AARtZXRhdXEAfgAGAAAABXQAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA50dAALUExBWUVSX0hFQURzcQB+AA8AAABAc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIdAAJbWV0YS10eXBldAAMZGlzcGxheS1uYW1ldAALc2t1bGwtb3duZXJ1cQB+AAYAAAAEdAAISXRlbU1ldGF0AAVTS1VMTHQAVnsiaXRhbGljIjpmYWxzZSwiZXh0cmEiOlt7ImJvbGQiOnRydWUsImNvbG9yIjoiIzk5OTk5OSIsInRleHQiOiJQcmV2aW91cyJ9XSwidGV4dCI6IiJ9c3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIdAAIdW5pcXVlSWR0AARuYW1ldAAKcHJvcGVydGllc3VxAH4ABgAAAAR0AA1QbGF5ZXJQcm9maWxldAAkMDQwNDljOTAtZDNlOS00NjIxLTljYWYtMDAwMDBhYWE5OTgydAANSGVhZCBEYXRhYmFzZXNyABNqYXZhLnV0aWwuQXJyYXlMaXN0eIHSHZnHYZ0DAAFJAARzaXpleHAAAAABdwQAAAABc3IAF2phdmEudXRpbC5MaW5rZWRIYXNoTWFwNMBOXBBswPsCAAFaAAthY2Nlc3NPcmRlcnhyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAACcQB+ACJ0AAh0ZXh0dXJlc3QABXZhbHVldACwZXlKMFpYaDBkWEpsY3lJNmV5SlRTMGxPSWpwN0luVnliQ0k2SW1oMGRIQTZMeTkwWlhoMGRYSmxjeTV0YVc1bFkzSmhablF1Ym1WMEwzUmxlSFIxY21Vdk5UUXlabVJsT0dJNE1tVTRZekZpT0dNeU1tSXlNalkzT1RrNE0yWmxNelZqWWpjMllUYzVOemM0TkRJNVltUmhaR0ZpWXpNNU4yWmtNVFV3TmpFaWZYMTl4AHg="))
        .setAmount(1)
        .displayName(UIStyle.UI_ARROW.append(UIStyle.translate(locale, "friend.previous").color(TextStyle.WHITE_COLOR).decorate(TextDecoration.BOLD)))
        .buildItem(), UISlot.SLOT_52
    ), (player, uiItem, clickType, inventoryClickEvent) -> {


      return true;
    });

    item(new UIItem(
      new DefaultItemStackBuilder<>(Base64ItemStack.decode("rO0ABXNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAkwABGtleXN0ABJMamF2YS9sYW5nL09iamVjdDtMAAZ2YWx1ZXNxAH4ABHhwdXIAE1tMamF2YS5sYW5nLk9iamVjdDuQzlifEHMpbAIAAHhwAAAABXQAAj09dAABdnQABHR5cGV0AAZhbW91bnR0AARtZXRhdXEAfgAGAAAABXQAHm9yZy5idWtraXQuaW52ZW50b3J5Lkl0ZW1TdGFja3NyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAA50dAALUExBWUVSX0hFQURzcQB+AA8AAABAc3EAfgAAc3EAfgADdXEAfgAGAAAABHEAfgAIdAAJbWV0YS10eXBldAAMZGlzcGxheS1uYW1ldAALc2t1bGwtb3duZXJ1cQB+AAYAAAAEdAAISXRlbU1ldGF0AAVTS1VMTHQAUnsiaXRhbGljIjpmYWxzZSwiZXh0cmEiOlt7ImJvbGQiOnRydWUsImNvbG9yIjoiIzJGQ0Q0MCIsInRleHQiOiJOZXh0In1dLCJ0ZXh0IjoiIn1zcQB+AABzcQB+AAN1cQB+AAYAAAAEcQB+AAh0AAh1bmlxdWVJZHQABG5hbWV0AApwcm9wZXJ0aWVzdXEAfgAGAAAABHQADVBsYXllclByb2ZpbGV0ACQwNDA0OWM5MC1kM2U5LTQ2MjEtOWNhZi0wMDAwMGFhYTk4NzF0AA1IZWFkIERhdGFiYXNlc3IAE2phdmEudXRpbC5BcnJheUxpc3R4gdIdmcdhnQMAAUkABHNpemV4cAAAAAF3BAAAAAFzcgAXamF2YS51dGlsLkxpbmtlZEhhc2hNYXA0wE5cEGzA+wIAAVoAC2FjY2Vzc09yZGVyeHIAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA/QAAAAAAADHcIAAAAEAAAAAJxAH4AInQACHRleHR1cmVzdAAFdmFsdWV0ALBleUowWlhoMGRYSmxjeUk2ZXlKVFMwbE9JanA3SW5WeWJDSTZJbWgwZEhBNkx5OTBaWGgwZFhKbGN5NXRhVzVsWTNKaFpuUXVibVYwTDNSbGVIUjFjbVV2TkdWbU16VTJZV1F5WVdFM1lqRTJOemhoWldOaU9EZ3lPVEJsTldaaE5XRXpOREkzWlRWbE5EVTJabVkwTW1aaU5URTFOamt3WXpZM05URTNZamdpZlgxOXgAeA=="))
        .setAmount(1)
        .displayName(UIStyle.UI_ARROW.append(UIStyle.translate(locale, "friend.next").color(TextStyle.WHITE_COLOR).decorate(TextDecoration.BOLD)))
        .buildItem(), UISlot.SLOT_53
    ), (player, uiItem, clickType, inventoryClickEvent) -> {


      return true;
    });


    this.setBackground(UIBackground.drawLine(UIRow.CHEST_ROW_5));
  }

  private Component generateOnlineOn(Locale locale, OfflinePlayer player) {
    return UIStyle.translate(locale, "online")
      .color(NamedTextColor.GREEN)
      .style(builder -> builder.decoration(TextDecoration.ITALIC, false))
      .appendSpace()
      .append(UIStyle.translate(locale, "on").color(TextStyle.PRIMARY_COLOR))
      .appendSpace()
      .append(Component.text("Lobby-1").color(TextStyle.WHITE_COLOR));
  }

  private Component generateItemName(OfflinePlayer player) {
    return Component.text(player.getName());
  }

  @Override
  public void onClose(Player player) {

  }
}
