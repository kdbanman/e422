JAVA_HOME=/usr/lib/jvm/java-1.7.0
JCC=$(JAVA_HOME)/bin/javac -d build -cp lib src/*/*.java
JAVA=$(JAVA_HOME)/bin/java -cp lib:build

sockio:
	-mkdir SockIO/build
	-mkdir SockIO/lib
	cd SockIO && $(JCC)
	-mkdir EncServer/lib
	-mkdir EncClient/lib
	rsync -a SockIO/build/* EncServer/lib/
	rsync -a SockIO/build/* EncClient/lib/

server: sockio
	-mkdir EncServer/build
	cd EncServer && $(JCC)

client: sockio
	-mkdir EncClient/build
	cd EncClient && $(JCC)

run_c: client
	cd EncClient && $(JAVA) encclient.EncClient

run_s: server
	cd EncServer && $(JAVA) encserver.EncServer
