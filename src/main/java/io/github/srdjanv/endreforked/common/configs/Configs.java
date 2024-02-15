package io.github.srdjanv.endreforked.common.configs;

import net.minecraftforge.common.config.Config;

import io.github.srdjanv.endreforked.Tags;

@Config(modid = Tags.MODID, name = Tags.MODID + "/BasicConfigs", category = "")
public final class Configs {

    public static final ClientSideConfigs CLIENT_SIDE_CONFIGS = new ClientSideConfigs();

    public static class ClientSideConfigs {

        @Config.Name("Show broken textures in jei for the EntropyWand")
        public boolean entropyWandRenderBrokenTextures = false;

        @Config.Name("Panorama")
        @Config.RequiresMcRestart
        @Config.Comment({ "Main menu panorama" })
        public boolean panorama = true;
    }

    public static final ServerSideConfigs SERVER_SIDE_CONFIGS = new ServerSideConfigs();

    public static class ServerSideConfigs {

        @Config.Name("ender")
        @Config.RequiresMcRestart
        @Config.Comment({ "EnchantBoost" })
        public int[] enchantBoost = { 1 };

        @Config.Name("EntropyWand tool damage per conversion")
        @Config.RequiresMcRestart
        public int entropyWandUseDamage = 2;

        @Config.Name("New Villagers")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to spawn" })
        public boolean spawnNewVillagers = true;

        @Config.Name("Chest Loot")
        @Config.RequiresMcRestart
        @Config.Comment({ "Allows to fill." })
        public boolean chestLoot = true;

        @Config.Name("Entity Loot")
        @Config.RequiresMcRestart
        public boolean entityLoot = true;

        @Config.Name("End Void Teleporter")
        @Config.RequiresMcRestart
        @Config.Comment({ "When player falls to void in The End, he teleports to the Overworld" })
        public boolean teleporterEnd = true;

        private ServerSideConfigs() {}
    }
}
