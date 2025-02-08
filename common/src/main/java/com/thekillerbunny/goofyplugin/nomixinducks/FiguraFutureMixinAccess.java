package com.thekillerbunny.goofyplugin.nomixinducks;

import com.mojang.datafixers.util.Either;
import org.luaj.vm2.LuaFunction;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Set;
import java.util.function.Consumer;

public interface FiguraFutureMixinAccess<T> {
    Set<Consumer<Either<Throwable, T>>> goofyplugin$handlers();
    Throwable goofyplugin$errorObject();
}
