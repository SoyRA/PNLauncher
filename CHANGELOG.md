# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.7] - 2021-07-24
### Changed
- `Process proc` now use relative paths instead of absolute paths [#3](../../issues/3)
- Auto-select 64-bit ID if the current ID is detected as such (duh).
- If it's the first time a Symlink is made and there is Backup, it will be moved [f8b91dd](../../commit/f8b91dd966336d6c4ea945c91418d2e3c2e41b43)

### Fixed
- 64-bit ID now works [#1](../../issues/1)
- `Excl*List.txt` are now replaced as long as they are different [#2](../../issues/2)
- Sometimes backups are not moved [#4](../../issues/4)
- Exclude PrtlUpd and some fixes [fb65373](../../commit/fb653730360418111e00fe425bfbcf8322dc87da)
- Relative path for H1Prtl and fixed DirectoryNotEmptyException [4404227](../../commit/440422751526f5797b30c26c217431177af771fd)

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
