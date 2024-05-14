package org.figuramc.exampleplugin;

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
public class ExamplePermissionsPlugin implements FiguraPermissions {
    /**
     * Example for a permission, available constructors are as follows:
     *  Toggleable Permission: Permissions(name {String}, blocked {0, 1}, low {0, 1}, default {0, 1}, high {0,1 }, maximum {0,1})
     *  Slider Permission : Permissions(name {String}, sliderMin {int}, sliderMax {int}, blocked {int}, low {int}, default {int}, high {int}, maximum {int})
     */
    public static final Permissions EXAMPLE_PERMISSION = new Permissions("example_permission", 0,0,0,1,1);
    @Override
    public String getTitle() {
        return ExamplePlugin.PLUGIN_ID;
    }

    @Override
    public Collection<Permissions> getPermissions() {
        return List.of(EXAMPLE_PERMISSION);
    }
}
