package org.figuramc.exampleplugin;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class ExampleExpectPlatform {
    /**
     * This is an example of {@link ExpectPlatform}.
     * <p>
     * This must be a <b>public static</b> method. The platform-implemented solution must be placed under a
     * platform sub-package, with its class suffixed with {@code Impl}.
     * <p>
     * Example:
     * Expect: org.figuramc.exampleplugin.ExampleExpectPlatform#getConfigDirectory()
     * Actual Fabric: org.figuramc.exampleplugin.fabric.ExampleExpectPlatformImpl#getConfigDirectory()
     * Actual Forge: org.figuramc.exampleplugin.forge.ExampleExpectPlatformImpl#getConfigDirectory()
     * <p>
     * <a href="https://plugins.jetbrains.com/plugin/16210-architectury">You should also get the IntelliJ plugin to help with @ExpectPlatform.</a>
     */
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
