# vim:ft=nix:ts=2:sts=2:sw=2:et:
{
  description = "A dev environment for java";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
    nix-vscode-extensions.url = "github:nix-community/nix-vscode-extensions";
  };

  outputs = {
    self,
    nixpkgs,
    flake-utils,
    nix-vscode-extensions,
  }:
    flake-utils.lib.eachDefaultSystem (
      system: let
        pkgs = import nixpkgs {
          system = "${system}";
        };

        codium = pkgs.vscode-with-extensions.override {
          vscode = pkgs.vscodium;
          vscodeExtensions = with nix-vscode-extensions.extensions.${system}.vscode-marketplace; [
            mhutchie.git-graph
            aaron-bond.better-comments
            pkgs.vscode-extensions.redhat.java
            pkgs.vscode-extensions.vscjava.vscode-java-test
            pkgs.vscode-extensions.vscjava.vscode-java-dependency
            pkgs.vscode-extensions.vscjava.vscode-java-debug
            pkgs.vscode-extensions.vscjava.vscode-gradle
          ];
        };
        idea-community = let
          fetch-plugin = { name, version, url, hash ? "sha256:" + pkgs.lib.fakeSha256 }: pkgs.stdenvNoCC.mkDerivation {
            inherit name version;
            src = pkgs.fetchurl {
              inherit url hash;
            };
            dontUnpack = true;
            installPhase = ''
              mkdir -p $out
              cp $src $out
            '';
          };
        in pkgs.jetbrains.plugins.addPlugins pkgs.jetbrains.idea-community [
          "nixidea"
          (fetch-plugin {
            name = "minecraft-development";
            version = "1.7.5";
            url = https://downloads.marketplace.jetbrains.com/files/8327/527990/Minecraft_Development-2024.1-1.7.5.zip;
            hash = sha256:9C34GX+bj5v2j7svF47boz7/uVIkWJuLyuXlAhV4TZI=;
          })
        ];
      in rec {
        name = "goofy-plugin";
        apps = rec {
          taskFor = { task, minecraft, minecraft-out ? minecraft, fabric-api, loom }: {
            type = "app";
            program = "${pkgs.writeScript "${name}-build-${minecraft}-${fabric-api}" ''
              #!${pkgs.bash}/bin/bash
              JAVA_HOME=${pkgs.jdk17} ${pkgs.gradle}/bin/gradle -Pminecraft_version=${minecraft} -Pminecraft_version_out=${minecraft-out} -P fabric_api_version=${fabric-api} -P loom_version=${loom} ${task} "$@"
            ''}";
          };
          build21 = taskFor {
            task = "build";
            minecraft = "1.21";
            minecraft-out = "1.21";
            fabric-api = "0.100.0";
            loom = "1.6-SNAPSHOT";
          };
          build21-1 = taskFor {
            task = "build";
            minecraft = "1.21";
            minecraft-out = "1.21.1";
            fabric-api = "0.100.0";
            loom = "1.6-SNAPSHOT";
          };
          build21-4 = taskFor {
            task = "build";
            minecraft = "1.21";
            minecraft-out = "1.21.4";
            fabric-api = "0.100.0";
            loom = "1.6-SNAPSHOT";
          };
          run21 = taskFor {
            task = "runClient";
            minecraft = "1.21";
            minecraft-out = "1.21";
            fabric-api = "0.100.0";
            loom = "1.6-SNAPSHOT";
          };
          run21-1 = taskFor {
            task = "runClient";
            minecraft = "1.21";
            minecraft-out = "1.21.1";
            fabric-api = "0.100.0";
            loom = "1.6-SNAPSHOT";
          };
          run21-4 = taskFor {
            task = "runClient";
            minecraft = "1.21";
            minecraft-out = "1.21.4";
            fabric-api = "0.100.0";
            loom = "1.6-SNAPSHOT";
          };
          default = run21-1;

          code.type = "app";
          code.program = with pkgs; "${writeScript "${name}-code" ''
            #!${bash}/bin/bash
            exec nix develop -c ${codium}/bin/codium --verbose -w . "$@"
          ''}";
          idea.type = "app";
          idea.program = with pkgs; "${writeScript "${name}-code" ''
            #!${bash}/bin/bash
            exec nix develop -c ${idea-community}/bin/idea-community build.gradle
          ''}";
          install.type = "app";
          install.program = with pkgs; "${writeScript "${name}-install" ''
            #!${bash}/bin/bash
            set -e
            export PATH=${with pkgs; lib.makeSearchPath "bin" [bash jq gawk perl findutils coreutils]}
            cd ~/.local/share/PrismLauncher/instances/"''${1?}"/ >/dev/null
            if ! `jq -r .components\|any\(.cachedName=='"Fabric Loader"'\) mmc-pack.json`; then
              echo "no Fabric :(" >&2
              exit 1
            fi
            jq -r .components[]\|select\(.cachedName==\"Minecraft\"\).cachedVersion mmc-pack.json | xargs bash ${builtins.toFile "install-2-electric-boogaloo" ''
              set -e
              cd .minecraft/mods/ >/dev/null
              test -f fabric-api-*.jar || echo "Remember to install Fabric API — proceeding anyway…"
              rm -f goofyfiguraplugin-*.jar
              pushd "$1"/fabric/build/libs/ >/dev/null
              ls goofyfiguraplugin-*+$2.jar | sort -rh | head -1 | xargs -i cp -v {} ~1
            ''} ~-
          ''}";
          build-install.type = "app";
          build-install.program = with pkgs; "${writeScript "${name}-build-install" ''
            #!${bash}/bin/bash
            set -e
            cd $(mktemp -d)
            trap "rm -rf $PWD" EXIT
            cp -rT --no-preserve=all ${./.} .
            nix run .#build1
            nix run .#build4
            nix run .#install "$@"
          ''}";
        };
        checks = {
          dev = pkgs.runCommand "check-dev" {inherit (devShells.default) buildInputs;} ''
            exec > $out
            command -v java
            command -v gradle
            command -v git
            command -v bash
          '';
          fmt =
            pkgs.runCommand "check-fmt" {
              buildInputs = [pkgs.nix];
            } ''
              ${formatter}/bin/${formatter.pname} --check . | tee $out
            '';
        };
        formatter = pkgs.alejandra;
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            bashInteractive
            gh
            git
            gradle
            jdk17
            nix
            stgit
            ./dev
          ];
          shellHook = ''
            alias pr="gh co"
            alias mkpr="gh pr create"
            br() {
              git fetch origin main
              git switch -c "''${1?}" --no-track origin/main
            }
            rv() {
              if test -z "$1"; then
                gh pr review -a
              else
                gh pr review -rb "$1"
              fi
            }
          '';
        };
      }
    );
}
