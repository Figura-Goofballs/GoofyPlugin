#vim:ft=nix:ts=2:sts=2:sw=2:et:
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

        packages.code = pkgs.vscode-with-extensions.override {
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
          buildFor = vers: {
            type = "app";
            program = "${pkgs.writeScript "${name}-build-${vers}" ''
#!${pkgs.bash}/bin/bash
              JAVA_HOME=${pkgs.jdk17} ${pkgs.gradle}/bin/gradle build -Dminecraft_version=${vers} -Dfabric_api_version=0.85.0+${vers} -Dfigura_version=0.1.4+${vers}
            ''}";
          };
          build = buildFor "1.20.1";
          build4 = buildFor "1.20.4";

          code.type = "app";
          code.program = with pkgs; "${writeScript "${name}-code" ''
#!${bash}/bin/bash
            export PATH=${bashInteractive}/bin:${git}/bin:${gradle}/bin:${jdk17}/bin:$PATH
            exec ${vscodium}/bin/codium --verbose -w .
          ''}";
        };
        formatter = pkgs.alejandra;
      }
    );
}
