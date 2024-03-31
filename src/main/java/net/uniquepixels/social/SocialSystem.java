package net.uniquepixels.social;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.uniquepixels.core.paper.chat.chatinput.ChatInputManager;
import net.uniquepixels.core.paper.gui.backend.UIHolder;
import net.uniquepixels.coreapi.player.PlayerManager;
import net.uniquepixels.social.friends.FriendCommand;
import net.uniquepixels.social.friends.FriendManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class SocialSystem extends JavaPlugin {

  @Override
  public void onEnable() {

    ResourceBundle bundle = ResourceBundle.getBundle("translation");
    ResourceBundle deBundle = ResourceBundle.getBundle("translation_de");

    TranslationRegistry registry = TranslationRegistry.create(Key.key("uniquepixels:core"));

    GlobalTranslator.translator().addSource(registry);

    registry.registerAll(Locale.ENGLISH, bundle, true);
    registry.registerAll(Locale.GERMAN, deBundle, true);


    RegisteredServiceProvider<UIHolder> uiProvider = Bukkit.getServicesManager().getRegistration(UIHolder.class);

    if (uiProvider == null)
      return;

    /*
     * UI workflow to open and manage current ui's (extend ChestUI for custom inventories)
     * */
    UIHolder uiHolder = uiProvider.getProvider();

    RegisteredServiceProvider<ChatInputManager> chatProvider = Bukkit.getServicesManager().getRegistration(ChatInputManager.class);

    if (chatProvider == null)
      return;

    /*
     * Use ChatInputManager to get the next chat message from player and add actions after a message has been sent
     * */

    PlayerManager playerManager = new PlayerManager();

    ChatInputManager chatInputManager = chatProvider.getProvider();

    FriendManager friendManager = new FriendManager();

    getCommand("friend").setExecutor(new FriendCommand(uiHolder, friendManager, playerManager));


  }

  @Override
  public void onDisable() {

  }
}
