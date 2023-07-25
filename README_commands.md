# Count all lines of code
```shell
for /D %I in (.\*) do cd %I && cloc --vcs git && cd ..
```