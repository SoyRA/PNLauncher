## PNLauncher
[![GitHub License](https://img.shields.io/github/license/SoyRA/PNLauncher?label=License)](https://github.com/SoyRA/PNLauncher)
[![GitHub Issues](https://img.shields.io/github/issues/SoyRA/PNLauncher?label=Issues)](https://github.com/SoyRA/PNLauncher/issues)
[![GitHub Stars](https://img.shields.io/github/stars/SoyRA/PNLauncher?label=Stars)](https://github.com/SoyRA/PNLauncher/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/SoyRA/PNLauncher?label=Forks)](https://github.com/SoyRA/PNLauncher/network)
![GitHub Release](https://img.shields.io/github/v/release/SoyRA/PNLauncher?label=Release)
![GitHub Downloads](https://img.shields.io/github/downloads/SoyRA/PNLauncher/total?label=Downloads)
![Platforms](https://img.shields.io/badge/Platforms-Windows-969696?style=flat&labelColor=585858)
- A Launcher for [ProjectNova](https://projectnova.us/) written in Java, with the (experimental) feature to be used to launch IW5, T6, etc. without having to install it in the game folder.
  - I recommend you to read the [Wiki](../../wiki) before doing anything.

## Compiling from source
1. If you have [Git](https://git-scm.com/), use `git clone --depth=1 https://github.com/SoyRA/PNLauncher.git`. Otherwise, click on the [green button](../../archive/refs/heads/main.zip) to download the code.
2. Mmm...I only know that I used [Eclipse](https://www.eclipse.org/) and made a project with [WindowBuilder](https://www.eclipse.org/windowbuilder/). :P 
    - So you are supposed to Import it as a project (then see how to make an Runnable JAR File or how to make an Ant Build with `Build.xml`).
3. If you plan to make your own version, convert it to another language (like C#) and stuff: don't forget to read the [Licenses](#licenses).

## Notice
1. Since I was waiting to go to University, I decided to study Java while I was waiting. So you may find that some things are missing (like more checks and better handling of `try catch`) or that there are other things that could be simplified even more.
2. Everything has worked as I want and I managed to do the Symlinks part, that's why I posted the code. Hopefully you won't encounter any fatal errors. 😅
3. I hope to learn a lot to improve the code and make it pure Java (and to make it work on Linux...well, if ProjectNova really works on Linux).

## Credits
- Pinecone - For replying to [my questions about ProjectNova](https://projectnova.us/community/threads/68).
- Homura - To solve my existential doubt if there is any standard to be followed when programming.
- Dasfonia - To solve my doubt that my symlinks idea can be achieved without symlinks by modifying the game (something I didn't want to do nor know how to do).
  - I'm sure you have to modify the `fs_homepath` of the game and/or other things.
- Vivi - Help me understand why one part of my code was not working the way I wanted it to.
- Internet - I learned about Java and other programming stuff thanks to the Internet people, all for free!

## Licenses
- [PNLauncher](LICENSE)
- [ini4j](PNLauncher/src/libs/ini4j-0.5.4/LICENSE.txt)
- [Ln](PNLauncher/src/libs/lnstatic/license.txt) (also uses [TRE](PNLauncher/src/libs/lnstatic/license_tre.txt), [UltraGetopt](PNLauncher/src/libs/lnstatic/license_ultragetop.txt) and [uint128](PNLauncher/src/libs/lnstatic/license_uint128.txt))
