package com.thekillerbunny.goofyplugin;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class GoofyExpectPlatform {
    /**
     * This is an example of {@link ExpectPlatform}.
     * <p>
     * This must be a <b>public static</b> method. The platform-implemented solution must be placed under a
     * platform sub-package, with its class suffixed with {@code Impl}.
     * <p>
     * Example:
     * Expect: com.thekillerbunny.goofyplugin.ExampleExpectPlatform#getConfigDirectory()
     * Actual Fabric: com.thekillerbunny.goofyplugin.fabric.ExampleExpectPlatformImpl#getConfigDirectory()
     * Actual Forge: com.thekillerbunny.goofyplugin.forge.ExampleExpectPlatformImpl#getConfigDirectory()
     * <p>
     * <a href="https://plugins.jetbrains.com/plugin/16210-architectury">You should also get the IntelliJ plugin to help with @ExpectPlatform.</a>
     */
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
