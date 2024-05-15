package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.lua.GoofyAPI;
import org.figuramc.figura.lua.FiguraAPIManager;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.api.AnimationAPI;
import org.figuramc.figura.lua.api.AvatarAPI;
import org.figuramc.figura.lua.api.ClientAPI;
import org.figuramc.figura.lua.api.ConfigAPI;
import org.figuramc.figura.lua.api.FileAPI;
import org.figuramc.figura.lua.api.HostAPI;
import org.figuramc.figura.lua.api.RaycastAPI;
import org.figuramc.figura.lua.api.RendererAPI;
import org.figuramc.figura.lua.api.TextureAPI;
import org.figuramc.figura.lua.api.action_wheel.ActionWheelAPI;
import org.figuramc.figura.lua.api.data.DataAPI;
import org.figuramc.figura.lua.api.data.ResourcesAPI;
import org.figuramc.figura.lua.api.event.EventsAPI;
import org.figuramc.figura.lua.api.json.JsonAPI;
import org.figuramc.figura.lua.api.keybind.KeybindAPI;
import org.figuramc.figura.lua.api.math.MatricesAPI;
import org.figuramc.figura.lua.api.math.VectorsAPI;
import org.figuramc.figura.lua.api.nameplate.NameplateAPI;
import org.figuramc.figura.lua.api.net.NetworkingAPI;
import org.figuramc.figura.lua.api.particle.ParticleAPI;
import org.figuramc.figura.lua.api.ping.PingAPI;
import org.figuramc.figura.lua.api.sound.SoundAPI;
import org.figuramc.figura.lua.api.vanilla_model.VanillaModelAPI;
import org.figuramc.figura.lua.api.world.WorldAPI;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Mixin(value = FiguraAPIManager.class, remap = false)
public class FiguraAPIManagerMixin {
    @Shadow
    @Final
    public static Set<Class<?>> WHITELISTED_CLASSES;

    @Shadow
    @Final
    public static Map<String, Function<FiguraLuaRuntime, Object>> API_GETTERS;

    static {
        WHITELISTED_CLASSES.add(GoofyAPI.class);

        API_GETTERS.put("goofy", r -> new GoofyAPI(r));
    }
}
