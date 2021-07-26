with import <nixpkgs> { };
mkShell {
  nativeBuildInputs = [
    bashInteractive
    maven
  ];
  buildInputs = [
    openjdk11
    nodejs-14_x
  ];
}
