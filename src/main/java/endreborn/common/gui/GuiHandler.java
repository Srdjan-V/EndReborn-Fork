package endreborn.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import endreborn.Reference;
import endreborn.client.gui.MaterializerContainer;
import endreborn.client.gui.MaterializerGui;
import endreborn.common.tiles.MaterializerTile;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Reference.GUI_E_USER)
            return new MaterializerContainer(player.inventory,
                    (MaterializerTile) world.getTileEntity(new BlockPos(x, y, z)));

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Reference.GUI_E_USER)
            return new MaterializerGui(player.inventory, (MaterializerTile) world.getTileEntity(new BlockPos(x, y, z)));

        return null;
    }
}
