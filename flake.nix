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
          taskFor = vers: task: {
            type = "app";
            program = "${pkgs.writeScript "${name}-build-${vers}" ''
              #!${pkgs.bash}/bin/bash
              JAVA_HOME=${pkgs.jdk17} ${pkgs.gradle}/bin/gradle -Pminecraft_version=${vers} ${task} "$@"
            ''}";
          };
          build1 = taskFor "1.20.1" "build";
          build4 = taskFor "1.20.4" "build";
          run1 = taskFor "1.20.1" "runClient";
          run4 = taskFor "1.20.4" "runClient";
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
              ${formatter}/bin/${formatter.pname} --check .
            '';
        };
        formatter = pkgs.alejandra;
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [nix jdk17 gradle git nix bashInteractive];
        };
      }
    );
}
