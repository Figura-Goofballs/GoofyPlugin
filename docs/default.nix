# NATHAN I SWEAR TO GOD IF YOU FLAKE THIS
# DO NOT FLAKE THIS
# DO NOT FLAKE THIS
# DO NOT FLAKE THIS
# https://matthewrhone.dev/jekyll-in-nixos
with (import <nixpkgs> {});
let env = bundlerEnv {
    name = "jekyll-gh-pages-test";
    inherit ruby;
    gemfile = ./Gemfile;
    lockfile = ./Gemfile.lock;
    gemset = ./gemset.nix;
  };
in stdenv.mkDerivation {
  name = "jekyll-gh-pages-test";
  buildInputs = [env bundler ruby];
}
