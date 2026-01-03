package com.thekillerbunny.goofyplugin.lua;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.nio.file.Path;

import com.thekillerbunny.goofyplugin.GoofyPlugin;
import com.thekillerbunny.goofyplugin.Feature;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.Minecraft;

import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.Badges;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.avatar.local.LocalAvatarFetcher;
import org.figuramc.figura.avatar.local.LocalAvatarLoader;
import org.figuramc.figura.avatar.UserData;
import org.figuramc.figura.backend2.NetworkStuff;
import org.figuramc.figura.config.Configs;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.LuaNotNil;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.event.LuaEvent;
import org.figuramc.figura.lua.api.nameplate.NameplateAPI;
import org.figuramc.figura.lua.api.world.ItemStackAPI;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.docs.LuaMethodOverload;
import org.figuramc.figura.lua.docs.LuaTypeDoc;
import org.figuramc.figura.math.vector.FiguraVec2;
import org.figuramc.figura.math.vector.FiguraVec3;
import org.figuramc.figura.gui.widgets.lists.AvatarList;
import org.figuramc.figura.utils.ColorUtils;
import org.figuramc.figura.utils.LuaUtils;
import org.figuramc.figura.utils.TextUtils;
import org.figuramc.figura.font.Emojis;

import com.google.gson.Gson;

import org.apache.commons.lang3.ObjectUtils;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ZeroArgFunction;

import com.thekillerbunny.goofyplugin.Enums;
import com.thekillerbunny.goofyplugin.GoofyPermissionsPlugin;
import com.thekillerbunny.goofyplugin.GoofyPlugin;
import com.thekillerbunny.goofyplugin.ducks.FiguraLuaRuntimeAccess;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Abilities;

@LuaWhitelist
@LuaTypeDoc(name = "GoofyAPI", value = "goofy")
public class GoofyAPI {
    private final Gson gson;
    private final FiguraLuaRuntime runtime;
    private final Avatar owner;
    private final Minecraft mc;
    
    public GoofyAPI(FiguraLuaRuntime runtime) {
        this.runtime = runtime;
        this.owner = runtime.owner;
        this.mc = Minecraft.getInstance();
        this.gson = new Gson();
    }

    public boolean canLog() {
        return ((Configs.LOG_OTHERS.value || FiguraMod.isLocal(owner.owner)) && owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1);
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.stop_avatar"
    )
    public void stopAvatar(String message) {
        if (message != null) {
          Component userComponent = TextUtils.tryParseJson(message);
          MutableComponent errorComponent = Component.empty().withStyle(ColorUtils.Colors.LUA_ERROR.style);
          errorComponent.append(userComponent);

          owner.errorText = errorComponent;
        }else {
          owner.errorText = Component.literal("Execution aborted by script").withStyle(ColorUtils.Colors.LUA_ERROR.style);
        }

        owner.scriptError = true;

        ((FiguraLuaRuntimeAccess) runtime).getSetHookFunction().invoke(LuaValue.varargsOf(new ZeroArgFunction() {
          @Override
          public LuaValue call() {
            return LuaValue.valueOf(0);
          }
        }, LuaValue.EMPTYSTRING, LuaValue.valueOf(1)));

        owner.clearParticles();
        owner.clearSounds();
        owner.closeBuffers();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = { Boolean.class },
                argumentNames = {"canFly"}
            )
        },
        value = "goofy.set_can_fly"
    )
    public void setCanFly(@LuaNotNil Boolean canFly) {
        runIfMovementEnabled(player -> {
            Abilities abilities = player.getAbilities();
            abilities.mayfly = canFly;
        });
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class, String.class},
                argumentNames = {"str", "pattern"}
            )
        },
        value = "goofy.regex_match"
    )
    public LuaTable regexMatch(@LuaNotNil String str, @LuaNotNil String pattern) {
        Pattern goofyPattern = Pattern.compile(pattern);
        Matcher matcher = goofyPattern.matcher(str);
        
        ArrayList<String> matches = new ArrayList<String>();

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        LuaTable tbl = new LuaTable();
        
        for (int i = 0; i < matches.size(); i++) {
            tbl.set(i + 1, LuaValue.valueOf(matches.get(i)));
        }

        return tbl;
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class, String.class, String.class},
                argumentNames = {"str", "pattern", "replacement"}
            )
        },
        value = "goofy.regex_sub"
    )
    public String regexSub(@LuaNotNil String str, @LuaNotNil String pattern, @LuaNotNil String replacement) {
        return str.replaceAll(pattern, replacement);
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.debug_to_log"
    )
    public void debugToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.debug("[" + owner.entityName + "] - " + data);
        }
    }

	/**
	 * @see https://github.com/FiguraMC/Figura/pull/309
	*/
	@LuaMethodDoc("goofy.get_cursor_item")
	@LuaWhitelist
	public ItemStackAPI getCursorItem() {
		return ItemStackAPI.verify(mc.player.containerMenu.getCarried());
	}

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.info_to_log"
    )
    public void infoToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.info("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.warn_to_log"
    )
    public void warnToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.warn("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.error_to_log"
    )
    public void errorToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.error("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.trace_to_log"
    )
    public void traceToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.trace("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_debug_enabled"
    )
    public boolean isDebugEnabled() {
        return canLog() && GoofyPlugin.LOGGER.isDebugEnabled();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_info_enabled"
    )
    public boolean isInfoEnabled() {
        return !(!canLog() || GoofyPlugin.LOGGER.isInfoEnabled());
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_warn_enabled"
    )
    public boolean isWarnEnabled() {
        return canLog() && GoofyPlugin.LOGGER.isWarnEnabled();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_error_enabled"
    )
    public boolean isErrorEnabled() {
        return canLog() && GoofyPlugin.LOGGER.isErrorEnabled();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_trace_enabled"
    )
    public boolean isTraceEnabled() {
        return canLog() && GoofyPlugin.LOGGER.isTraceEnabled();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.what_does_bumpscocity_do"
    )
    public String whatDoesBumpscocityDo() {
        throw new LuaError("");
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.get_bumpscocity"
    )
    public int getBumpscocity() {
        if (owner.permissions.get(GoofyPermissionsPlugin.BUMPSCOCITY) > 1000) {
            throw new LuaError("Dear god, this is way too much bumpscocity! (max 1000)");
        }
        return owner.permissions.get(GoofyPermissionsPlugin.BUMPSCOCITY);
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"playerUUID"}
            )
        },
        value = "goofy.load_avatar"
    )
    public void loadAvatar(@LuaNotNil String playerUUID) {
        UUID uuid = UUID.fromString(playerUUID);
        if (AvatarManager.getAvatarForPlayer(uuid) == null) {
            return;
        }
        NetworkStuff.getUser(new UserData(uuid));
    }

    @LuaWhitelist
    @LuaMethodDoc(
      overloads = {
        @LuaMethodOverload(
          argumentTypes = { String.class },
          argumentNames = { "avatarUUID" }
        )
      },
      value = "goofy.get_avatar_nameplate"
    )
    public Object[] getAvatarNameplate(@LuaNotNil String avatarUUID) {
      UUID uuid = UUID.fromString(avatarUUID);
      Avatar avatar = AvatarManager.getLoadedAvatar(uuid);

      if (avatar == null) {
        return new Object[]{avatarUUID, avatarUUID, avatarUUID};
      }

      NameplateAPI plate = avatar.luaRuntime.nameplate;

      String name = avatar.entityName;
      String chat = ObjectUtils.firstNonNull(plate.CHAT.getText(), name, avatarUUID);
      String entity = ObjectUtils.firstNonNull(plate.ENTITY.getText(), name, avatarUUID);
      String list = ObjectUtils.firstNonNull(plate.LIST.getText(), name, avatarUUID);

      Component cChat = Emojis.removeBlacklistedEmojis(Emojis.applyEmojis(Badges.noBadges4U(Badges.appendBadges(TextUtils.tryParseJson(chat), uuid, true))));
      Component cEntity = Emojis.removeBlacklistedEmojis(Emojis.applyEmojis(Badges.noBadges4U(Badges.appendBadges(TextUtils.tryParseJson(entity), uuid, true))));
      Component cList = Emojis.removeBlacklistedEmojis(Emojis.applyEmojis(Badges.noBadges4U(Badges.appendBadges(TextUtils.tryParseJson(list), uuid, true))));

      return new Object[] { cChat, cEntity, cList };
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = { String.class },
                argumentNames = { "playerUUID" }
            )
        },
        value = "goofy.get_avatar_color"
    )
    public String getAvatarColor(String playerUUID) {
        UUID uuid = UUID.fromString(playerUUID);
        if (AvatarManager.getAvatarForPlayer(uuid) == null) {
            return null;
        }
        Avatar avatar = AvatarManager.getLoadedAvatar(uuid);
        if (avatar == null) {
            return null;
        }
        return avatar.color;
    }

    @LuaWhitelist
    @LuaMethodDoc("goofy.check_features")
    public void checkFeatures(LuaTable features) {
        Varargs v = LuaValue.NIL;
        while (!(v = features.next(v.arg1())).isnil(1)) {
            String n = v.checkjstring(1);
            int l = v.checkint(2);
            Feature f;
            try {
                f = Feature.valueOf(n.toUpperCase());
            } catch (Throwable t) {
                throw new LuaError("GoofyPlugin feature '%s' not supported".formatted(n));
            }
            if (f.current() < l) {
                throw new LuaError("This avatar uses version %d of GoofyPlugin feature %s, which is more recent than your currently-installed version %d".formatted(l, n, f.current()));
            }
            if (f.compatible() > l) {
                throw new LuaError("This avatar uses version %d of GoofyPlugin feature %s, which is incompatible with your currently-installed version %d (must be at most %d)".formatted(l, n, f.current(), f.compatible()));
            }
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"playerUUID"}
            )
        },
        value = "goofy.reload_avatar"
    )
    public void reloadAvatar(@LuaNotNil String playerUUID) {
        UUID uuid = UUID.fromString(playerUUID);
        if (AvatarManager.getAvatarForPlayer(uuid) == null) {
            return;
        }
        AvatarManager.reloadAvatar(uuid);
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {Enums.GuiElement.class, Boolean.class},
                argumentNames = {"element", "disableRender"}
            )
        },
        value = "goofy.set_disable_gui_element"
    )
    public void setDisableGUIElement(@LuaNotNil String guiElement, @LuaNotNil Boolean disableRender) {
        if (!FiguraMod.isLocal(owner.owner)) {
            return;
        }

        try {
            Enums.GuiElement element = Enums.GuiElement.valueOf(guiElement);
            GoofyPlugin.disabledElements.put(element, disableRender);
        }catch (IllegalArgumentException e) {
            throw new LuaError("Could not find element with name " + guiElement);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc("goofy.upload_avatar")
    public void uploadAvatar() {
        if (!FiguraMod.isLocal(owner.owner)) {
            return;
        }

        try {
            LocalAvatarLoader.loadAvatar(null, null);
        }catch (Exception ignored) {}
        NetworkStuff.uploadAvatar(owner);
        AvatarList.selectedEntry = null;
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"path"}
            )
        },
        value = "goofy.load_local_avatar"
    )
    public void loadLocalAvatar(@LuaNotNil String avatarPath) {
        if (!FiguraMod.isLocal(owner.owner)) {
            return;
        }
        if (avatarPath.isEmpty()) {
            throw new LuaError("Path cannot be empty");
        }
        
        Path path = LocalAvatarFetcher.getLocalAvatarDirectory().resolve(avatarPath);
        AvatarManager.loadLocalAvatar(path);
        AvatarList.selectedEntry = path;
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = {FiguraVec3.class},
                            argumentNames = {"velocity"}
                    ),
                    @LuaMethodOverload(
                            argumentTypes = {Double.class, Double.class, Double.class},
                            argumentNames = {"x", "y", "z"}
                    )
            },
            value = "goofy.movement_enabled"
    )
    public boolean movementEnabled() {
        AtomicBoolean enabled = new AtomicBoolean(false);
        runIfMovementEnabled(player -> enabled.set(true));
        return enabled.get();
    }

    public void runIfMovementEnabled(Consumer<LocalPlayer> callback) {
        if (FiguraMod.isLocal(owner.owner) && ((Object) mc.player) instanceof LocalPlayer player) callback.accept(player);
    }

    @LuaWhitelist
    @LuaMethodDoc(
      overloads = {
        @LuaMethodOverload(
          argumentTypes = {FiguraVec3.class},
          argumentNames = {"velocity"}
        ),
        @LuaMethodOverload(
          argumentTypes = {Double.class, Double.class, Double.class},
          argumentNames = {"x", "y", "z"}
        )
      },
      value = "goofy.set_velocity"
    )
    public void setVelocity(@LuaNotNil Object x, Double y, Double z) {
      runIfMovementEnabled(player -> {
        FiguraVec3 velocity = LuaUtils.parseVec3("setVelocity", x, y, z);
        player.setDeltaMovement(velocity.asVec3());
      });
    }

    @LuaWhitelist
    @LuaMethodDoc(
      overloads = {
        @LuaMethodOverload(
          argumentTypes = {FiguraVec3.class},
          argumentNames = {"position"}
        ),
        @LuaMethodOverload(
          argumentTypes = {Double.class, Double.class, Double.class},
          argumentNames = {"x", "y", "z"}
        )
      },
      value = "goofy.set_pos"
    )
    public void setPos(@LuaNotNil Object x, Double y, Double z) {
        runIfMovementEnabled(player -> {
        FiguraVec3 pos = LuaUtils.parseVec3("setPos", x, y, z);
        player.setPos(pos.asVec3());
      });
    }
    
    @LuaWhitelist
    @LuaMethodDoc(
      overloads = {
        @LuaMethodOverload(
          argumentTypes = {FiguraVec2.class},
          argumentNames = {"rotation"}
        ),
        @LuaMethodOverload(
          argumentTypes = {Double.class, Double.class},
          argumentNames = {"x", "y"}
        )
      },
      value = "goofy.set_rot"
    )

    public void setRot(@LuaNotNil Object x, Double y) {
        runIfMovementEnabled(player -> {
        FiguraVec2 rot = LuaUtils.parseVec2("setRot", x, y);
        player.setXRot((float) rot.x);
        player.setYRot((float) rot.y);
      });
    }

    @LuaWhitelist
    @LuaMethodDoc(
      overloads = {
        @LuaMethodOverload(
          argumentTypes = {Double.class},
          argumentNames = {"angle"}
        )
      },
      value = "goofy.set_body_rot"
    )

    public void setBodyRot(@LuaNotNil double rot) {
        runIfMovementEnabled(player -> {
        player.setYBodyRot((float) rot);
      });
    }

    @Override
    public String toString() {
        return "GoofyAPI";
    }
}
