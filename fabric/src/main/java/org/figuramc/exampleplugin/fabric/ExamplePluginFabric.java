package org.figuramc.goofyplugin.fabric;

import net.fabricmc.api.ModInitializer;
import org.figuramc.goofyplugin.GoofyPlugin;

/**
 * A mod class is not technically needed for Fabric to load the Plugin, but it's still nice to have.
 */
public class GoofyPluginFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        GoofyPlugin.init();
    }
}
