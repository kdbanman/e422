JAVA_HOME=/usr/lib/jvm/java-1.7.0
JCC=$(JAVA_HOME)/bin/javac -d build -cp lib src/*/*.java
JAVA=$(JAVA_HOME)/bin/java -cp lib:build
JAVAH=$(JAVA_HOME)/bin/javah
JNI=gcc -fPIC -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

all: sockio encrypt decrypt libencrypt libdecrypt server client

sockio:
	-mkdir SockIO/build
	-mkdir SockIO/lib
	cd SockIO && $(JCC)
	-mkdir EncServer/lib
	-mkdir EncClient/lib
	rsync -a SockIO/build/* EncServer/lib/
	rsync -a SockIO/build/* EncClient/lib/

encrypt: sockio
	$(JAVAH) -o encrypt.h -cp SockIO/build sockio.Encryptor

decrypt: sockio
	$(JAVAH) -o decrypt.h -cp SockIO/build sockio.Decryptor

libencrypt: encrypt
	$(JNI) -o libencrypt.so -shared encrypt.c

libdecrypt: decrypt
	$(JNI) -o libdecrypt.so -shared decrypt.c

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

clean:
	rm -rf */build
	rm -rf */dist
	rm -rf */lib
	rm *.so *.h
