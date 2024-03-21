<div align="center" style="margin-top: 40px;">
<a href="https://github.com/Quail-Language/quail">
<img src="images/quail.png" alt="Logo" width="100" height="100">
</a>

<h1 align="center">Quail</h1>

<p align="center">
An easy-to-use scripting language with simple yet powerful syntax
<br>

![GitHub License](https://img.shields.io/github/license/Quail-Language/quail)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/Quail-Language/quail/total)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/Quail-Language/quail/main.yml)
![GitHub Issues or Pull Requests](https://img.shields.io/github/issues/Quail-Language/quail)
![GitHub Release](https://img.shields.io/github/v/release/Quail-Language/quail)

<a href="https://quail-language.github.io/">Website</a>
·
<a href="https://quail-language.github.io/docs">Docs</a>
·
<a href="https://github.com/Quail-Language/quail/issues">Issues</a>
</p>
</div>

### About project
Quail is an easy-to-use scripting language with simple yet powerful syntax, embeddable in Java applications and supporting a plenty of different approaches for designing code and writing it.

Find out more on the [website](https://quail-language.github.io/)

**Quail's main purposes** are: developing utilities, using it as a 
script language for bigger project and using it for education. 

### Installation (automatic)

##### On Windows and Linux
Download and run installer from [downloads page](https://quail-language.github.io/download)

If you are on Linux, don't forget to `chmod +x quail-installer.sh`

After you've installed Quail with this installer,
you can access Quail Runtime with `quail` command and Quail Development
Kit with `quail-qdk` command. Find out more about commands in 
[Quail Specification Chapter 12](https://quail-language.github.io/docs/spec/chapter12.html)

### Manual installation

##### On Windows
1. Make sure you have at least Java 8 installed
2. Download your chosen Quail version
3. Place `qre.jar` and/or `qdk.jar` into some folder on your computer
4. Create an environment variable called `QUAIL_HOME` and set it to that folder
5. Place the following contents into file at `C:\Windows\quail.bat`
    ```
    @echo off
    if exist "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" (
    "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" -jar "%QUAIL_HOME%\qre.jar" %*
    )
    if not exist "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" (
    java -jar "%QUAIL_HOME%\qre.jar" %*
    )
    ```
6. Place the following contents into file at `C:\Windows\quail-qdk.bat`
    ```
    @echo off
    if exist "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" (
    "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" -jar "%QUAIL_HOME%\qdk.jar" %*
    )
    if not exist "%QUAIL_HOME%\jre\jdk1.8.0_402\bin\java.exe" (
    java -jar "%QUAIL_HOME%\qdk.jar" %*
    )
    ```
7. Now you can access Quail with `quail` and `quail-qdk` commands

##### On Linux
1. Make sure you have at least Java 8 installed
2. Download your chosen Quail version
3. Place `qre.jar` and/or `qdk.jar` into `/bin/quail-jars/`
4. Place the following contents into file at `/bin/quail`
    ```
    #!/bin/bash
    java -jar /bin/quail-jars/qre.jar $@
    ```
5. Place the following contents into file at `/bin/quail-qdk`
    ```
    #!/bin/bash
    java -jar /bin/quail-jars/qdk.jar $@
    ```
6. Run
    ```
    sudo chmod +x /bin/quail
    sudo chmod +x /bin/quail-qdk
    ```
7. Now you can access Quail with `quail` and `quail-qdk` commands

### First test
Write a short script:
```lua
print("Hello, World!")
```

Then execute it:
```
quail run myscript.q
```

You will see the output in your console

### Where to go next?

If you are completely new or an amateur to programming, read
[Getting Started with Quail 2.0+](https://github.com/Quail-Language/quail/wiki/Getting-Started-with-Quail-v2.0)

If you are very confident with different languages, and you are aware
of abstract syntax concepts, you can go straight to 
[Quail Specification](https://quail-language.github.io/docs/spec/),
[Quail Core Docs](https://quail-language.github.io/docs/core/) and
[Quail Library Docs](https://quail-language.github.io/docs/libs/)

Also check out [examples](examples).

### License

This work is licensed under GNU General Public License v3.0

### Contact

You can contact the developer in the 
[Issues](https://github.com/Quail-Language/quail/issues) tab or in
[Discord server](https://discord.gg/8smQAa8whM).

### Developer
Project was being built from scratch and is being developed now 
only by [@Tapeline](https://www.github.com/Tapeline)
