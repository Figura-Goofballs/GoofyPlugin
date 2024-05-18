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

          code.type = "app";
          code.program = with pkgs; "${writeScript "${name}-code" ''
            #!${bash}/bin/bash
            exec nix develop -c ${codium}/bin/codium --verbose -w . "$@"
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
          buildInputs = with pkgs; [nix jdk17 gradle git gh nix bashInteractive];
        };
      }
    );
}
