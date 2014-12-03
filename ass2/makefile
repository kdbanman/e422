ANT= ant -Dplatforms.JDK_1.7.home=$(JAVA_HOME) -f build-lab.xml

sockio:
	cd SockIO && $(ANT) clean jar && cd ..

server: sockio
	cd EncServer && $(ANT) clean jar && cd ..

client: sockio
	cd EncClient && $(ANT) clean jar && cd ..

run_c: client
	cd EncClient && $(ANT) run && cd ..

run_s: server
	cd EncServer && $(ANT) run && cd ..
