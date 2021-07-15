# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2021-07-12
### Added
- Java 8 compatibility (goodbye `.isBlank()` hello `.chars().allMatch(Character::isWhitespace)`).
- Auto-selection of the `Injector##.exe` based on the selected game.
- Auto-creation of `ayria_appid.txt` with App ID based on the selected game (and button pressed).
- JTextArea to show some information to the user.
- Symbolic links to be able to use the Launcher in one place and run the 5 possible games.

### Changed
- The way to move files (`moveFiles()`) is now similar to the way `H1PrtlUpd()` does it.

### Fixed
- The "PLAY XXX" buttons will not attempt to repeat their action multiple times.
