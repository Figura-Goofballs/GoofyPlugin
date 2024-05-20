package com.thekillerbunny.goofyplugin;

import org.figuramc.figura.entries.FiguraPermissions;
import org.figuramc.figura.entries.annotations.FiguraPermissionsPlugin;
import org.figuramc.figura.permissions.Permissions;

import java.util.Collection;
import java.util.List;

/**
 * Example Permission/Trust Plugin
 *  Annotation required for Forge to Locate and Load the Plugin
 *  Entrypoint in fabric.mod.json: figura_permissions
 */
@FiguraPermissionsPlugin
public class GoofyPermissionsPlugin implements FiguraPermissions {
    public static final Permissions CAN_LOG = new Permissions("can_log", 0,0,0,1,1);
    public static final Permissions BUMPSCOCITY = new Permissions("bumpscocity", 0, 1500, 0, 250, 500, 750, 1000);

    @Override
    public String getTitle() {
        return GoofyPlugin.PLUGIN_ID;
    }

    @Override
    public Collection<Permissions> getPermissions() {
        return List.of(CAN_LOG, BUMPSCOCITY);
    }
}
