#!/bin/bash

rm -rf /bin/quail-jars/
rm -f /bin/quail

mkdir /bin/quail-jars/
sudo apt-get install openjdk-8-jre-headless

wget -P /bin/quail-jars/ https://github.com/Quail-Language/quail/releases/latest/download/qdk.jar
wget -P /bin/quail-jars/  https://github.com/Quail-Language/quail/releases/latest/download/qre.jar

printf "#!/bin/bash\njava -jar /bin/quail-jars/qre.jar $@" > /bin/quail
printf "#!/bin/bash\njava -jar /bin/quail-jars/qdk.jar $@" > /bin/quail-qdk

chmod +x /bin/quail
chmod +x /bin/quail-qdk