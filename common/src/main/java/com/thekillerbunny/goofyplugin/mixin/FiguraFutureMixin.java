package com.thekillerbunny.goofyplugin.mixin;

import com.mojang.datafixers.util.Either;
import com.thekillerbunny.goofyplugin.nomixinducks.FiguraFutureMixinAccess;
import org.figuramc.figura.lua.api.data.FiguraFuture;
import org.luaj.vm2.LuaError;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Mixin(value = FiguraFuture.class, remap = false)
public class FiguraFutureMixin<T> implements FiguraFutureMixinAccess<T> {
    @Shadow private LuaError errorObject;
    @Unique Set<Consumer<Either<Throwable, T>>> goofyplugin$handlers;

    @Inject(at = @At("TAIL"), method = "<init>")
    void init(CallbackInfo ci) {
        goofyplugin$handlers = new HashSet<>();
    }

    @Inject(at = @At("TAIL"), method = "complete")
    void onComplete(T value, CallbackInfo ci) {
        for (var handler: goofyplugin$handlers) {
            handler.accept(Either.right(value));
        }
        goofyplugin$handlers = null;
    }

    @Inject(at = @At("TAIL"), method = "error")
    void onError(Throwable t, CallbackInfo ci) {
        for (var handler: goofyplugin$handlers) {
            handler.accept(Either.left(t));
        }
        goofyplugin$handlers = null;
    }

    @Override
    public Set<Consumer<Either<Throwable, T>>> goofyplugin$handlers() {
        return goofyplugin$handlers;
    }

    @Override
    public Throwable goofyplugin$errorObject() {
        return errorObject;
    }
}
