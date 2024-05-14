package org.figuramc.goofyplugin.forge;

import net.minecraftforge.fml.common.Mod;
import org.figuramc.goofyplugin.GoofyPlugin;

/**
 * A mod class is needed for Forge to load the Plugin
 */
@Mod(GoofyPlugin.PLUGIN_ID)
public class GoofyModForge {
    public GoofyModForge() {
        GoofyPlugin.init();
    }
}
