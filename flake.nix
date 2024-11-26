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
            url = "https://downloads.marketplace.jetbrains.com/files/8327/527990/Minecraft_Development-2024.1-1.7.5.zip";
            hash = "sha256:9C34GX+bj5v2j7svF47boz7/uVIkWJuLyuXlAhV4TZI=";
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
          writeApp = runtimeInputs: text: with pkgs; {
            type = "app";
            program = "${writeShellApplication {
              name = "script.sh";
              inherit runtimeInputs text;
            }}/bin/script.sh";
          };
          build1 = taskFor {
            task = "build";
            minecraft = "1.20.1";
            fabric-api = "0.83.0";
            loom = "1.2-SNAPSHOT";
          };
          build4 = taskFor {
            task = "build";
            minecraft = "1.20.1";
            minecraft-out = "1.20.4";
            fabric-api = "0.83.0";
            loom = "1.2-SNAPSHOT";
          };
          run1 = taskFor {
            task = "runClient";
            minecraft = "1.20.1";
            fabric-api = "0.83.0";
            loom = "1.2-SNAPSHOT";
          };
          run4 = taskFor {
            task = "runClient";
            minecraft = "1.20.1";
            minecraft-out = "1.20.4";
            fabric-api = "0.83.0";
            loom = "1.2-SNAPSHOT";
          };
          default = run1;

          headlessInit = writeApp [pkgs.jdk21 pkgs.coreutils pkgs.unzip] /*bash*/ ''
            set -ex
            rm -rf HeadlessMC

            mkdir -p HeadlessMC/run/saves/testWorld
            unzip ${./test/testWorld.zip} -d HeadlessMC/run/saves/testWorld

            mkdir -p HeadlessMC/run/mods
            ln -vfs "$(realpath fabric/build/libs/goofyfiguraplugin-*+1.20.1.jar)" "$_/"
            ln -vfs ${pkgs.fetchurl {
              url = "https://github.com/3arthqu4ke/hmc-specifics/releases/download/v1.20.1-1.2.2/hmc-specifics-fabric-1.20.1-1.2.2.jar";
              hash = "sha256:Q43VsYkZWu8F0f+B28RIQRjabKP8RQpJgBlxXSM9Dl0=";
            }} ${pkgs.fetchurl {
              url = "https://cdn.modrinth.com/data/P7dR8mSH/versions/P7uGFii0/fabric-api-0.92.2%2B1.20.1.jar";
              hash = "sha256:RQD4RMRVc9A51o05Y8mIWqnedxJnAhbgrT5d8WxncPw=";
            }} ${pkgs.fetchurl {
              url = "https://cdn.modrinth.com/data/s9gIPDom/versions/DhHrk371/figura-0.1.4%2B1.20.1-fabric-mc.jar";
              hash = "sha256:7YvlqzciJXeBx919VHY/Cq04TSHoGwMoHpCgQ7BfAaI=";
            }} -t "$_"
            rm -vf HeadlessMC/config.properties
            ln -vfs ${pkgs.writeText "config.properties" /*conf*/''
              hmc.assets.dummy=true
              hmc.exit.on.failed.command=true
              hmc.gamedir=HeadlessMC/run
              hmc.java.versions=${pkgs.jdk21}/bin/java
              hmc.jline.enabled=false
              hmc.mcdir=HeadlessMC
              hmc.offline.username=GithubActions
              hmc.offline=true
            ''} "$_"
            java -jar ${pkgs.fetchurl {
              url = "https://github.com/3arthqu4ke/headlessmc/releases/download/2.4.1/headlessmc-launcher-wrapper-2.4.1.jar";
              hash = "sha256:mWJd/ygFzSsTkVekm/bT0u21d6tDIXHlEoYa2eMfmOw=";
            }} <<EOF
              download 1.20.1
              fabric 1.20.1
            EOF
          '';
          headless = writeApp [pkgs.jdk21] /*bash*/''
            java -jar ${pkgs.fetchurl {
              url = "https://github.com/3arthqu4ke/headlessmc/releases/download/2.4.1/headlessmc-launcher-wrapper-2.4.1.jar";
              hash = "sha256:mWJd/ygFzSsTkVekm/bT0u21d6tDIXHlEoYa2eMfmOw=";
            }} <<EOF
              help
              launch 1 -id --quickPlaySingleplayer testWorld
              help
            EOF
          '';

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
