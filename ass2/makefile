JAVA_HOME=/usr/lib/jvm/java-1.7.0
JCC=$(JAVA_HOME)/bin/javac -d build -cp lib src/*/*.java
JAVA=$(JAVA_HOME)/bin/java -Djava.library.path=$(shell pwd) -cp lib:build
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
	cp libencrypt.so EncServer/lib/
	cp libencrypt.so EncClient/lib/

libdecrypt: decrypt
	$(JNI) -o libdecrypt.so -shared decrypt.c
	cp libdecrypt.so EncServer/lib/
	cp libdecrypt.so EncClient/lib/

server: sockio libencrypt libdecrypt
	-mkdir EncServer/build
	cd EncServer && $(JCC)

client: sockio libencrypt libdecrypt
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
