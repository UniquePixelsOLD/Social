package net.uniquepixels.social.friends.ui;

import net.kyori.adventure.text.Component;
import net.uniquepixels.core.paper.gui.UIRow;
import net.uniquepixels.core.paper.gui.backend.UIHolder;
import net.uniquepixels.core.paper.gui.exception.OutOfInventoryException;
import net.uniquepixels.core.paper.gui.types.chest.ChestUI;
import org.bukkit.entity.Player;

public class FriendSettingsUI extends ChestUI {
    public FriendSettingsUI(UIHolder uiHolder) {
        super(Component.translatable("friend.settings"), UIRow.CHEST_ROW_3);
    }

    @Override
    public boolean allowItemMovementInOtherInventories() {
        return false;
    }

    @Override
    protected void initItems(Player opener) throws OutOfInventoryException {


    }

    @Override
    public void onClose(Player player) {

    }
}
