# BPMS-Leshan-with-RPi
Blood Pressure Monitoring System Simulator using the IoT Protocol Lightweight M2M  and Leshan server and client implementation in Java.

## Getting Started
#### Equipment Used
```
Laptop
Raspberry Pi  
PCB with sensors and ADC
```
#### Libraries-Tools Used 
```
apache-maven-3.3.9-bin.tar.gz
pi4j-core.jar
tsensor.jar
```

## Info
#### General
The objects used are IPSO compliant, based on the object model specified in OMA LightWeight M2M [1] Chapter 6, Identifiers and Resources.

#### Pi4j Library
a friendly object-oriented I/O API and implementation libraries for Java Programmers to access the full I/O capabilities of the Raspberry Pi platform
#### PCB
The PCB used has a Temperature Sensor LM335Z,an Analog to Digital Converter MCP3002 - 2.7V Dual Channel 0-Bit A/D Converter with SPI Serial Interface and some LEDs.

#### tsensor.jar
The tsensor.jar implements methods for easy interraction with the temperature sensor.

## Characteristics of the System
```
•   The Raspberry Pi is the Lightweight M2M client that sends to the server the temperature value every 5 seconds.
•   The Laptop application is the Lightweight M2M server that can read and observe the sensor value.
•   If the value exceeds a specific limit, the server then sends a message to the client to turn on a LED,the status of which the server can also read and observe. 
•   LwM2M operations used:Write,Read,Observe
```

## Configuring the Laptop
•  To  get the folder:
```
git clone https://github.com/nikoshet/BPMS-Leshan-with-RPi.git
```
Open the project BPMS_leshan_server,compile and run(the main is on BPMS_leshan_server/leshan-server-demo folder).

## Configuring the Raspberry Pi
•  First do:
```
sudo apt-get update && sudo apt-get upgrade -y
```
•  To  get the folder:
```
git clone https://github.com/nikoshet/BPMS-Leshan-with-RPi.git
```
•  To install Maven:
```
wget http://www.mirrorservice.org/sites/ftp.apache.org/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
sudo tar -xzvf apache-maven-3.3.9-bin.tar.gz -C /opt
sudo edit /etc/profile.d/maven.sh
export M2_HOME=/opt/apache-maven-3.3.9
export PATH=$PATH:$M2_HOME/bin
mvn -version
```
•  For the tsensor.jar library:
```
mvn install:install-file -Dfile=/home/pi/BPMS-Leshan-with-RPi/BPMS_RPi_leshan_client/tsensor.jar -DgroupId=apt.lab.rpi -DartifactId=tempsensor -Dversion=1.0.0 -Dpackaging=jar -DgeneratePom=true
```
•  For the Pi4j library:
Download the Pi4J Debian/Raspian installer package (.deb) using your web browser at the following URL:
http://get.pi4j.com/download/pi4j-1.2-SNAPSHOT.deb
```
cd BPMS-Leshan-with-RPi
sudo dpkg -i pi4j-1.2-SNAPSHOT.deb
dpkg-deb -R pi4j-1.2-SNAPSHOT.deb tmp
mvn install:install-file -Dfile=/home/pi/BPMS-Leshan-with-RPi/BPMS_RPi_leshan_client/tmp/opt/pi4j/lib/pi4j-core.jar -DgroupId=com.pi4j -DartifactId=pi4j-core -Dversion=1.2-SNAPSHOT -Dpackaging=jar -DgeneratePom=true
```
Change the IP of your LwM2M Server

•  Get the maven dependencies needed for the project
```
cd BPMS-Leshan-with-RPi
mvn clean install
cd BPMS_RPi_leshan_client/leshan-client-demo
mvn clean install
```
•  To finally start the client
```
sudo java -jar target/leshan-client-demo-1.0.0-SNAPSHOT-jar-with-dependencies.jar
```

## FYI

#### LwM2M Protocol 
[here](http://www.openmobilealliance.org/release/LightweightM2M/V1_0-20160407-C/OMA-TS-LightweightM2M-V1_0-20160407-C.pdf) and [here](https://github.com/eclipse/leshan) 

#### Pi4j Library
[here](http://pi4j.com/index.html)



For any feedback or questions do not hesitate to contact me.

