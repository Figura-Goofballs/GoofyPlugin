package com.thekillerbunny.goofyplugin;

@SuppressWarnings("unused") // only used by `valueOf`
public enum Feature {
    BASE {
        @Override
        public int current() {
            return 0;
        }

        @Override
        public int compatible() {
            return 0;
        }
    };
    public abstract int current();
    public abstract int compatible();
    {
        assert current() >= compatible();
    }
}
