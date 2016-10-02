JAVA=/usr/lib/jvm/java-1.7.0-openjdk-1.7.0.65.x86_64
HERE= $(shell pwd)

libinsertionsort.so: src/insertionsort.c src/insertionsort.h
	gcc -fPIC -I$(JAVA)/include -I$(JAVA)/include/linux -o lib/libinsertionsort.so -shared src/insertionsort.c

insertionsort.h:
	$(JAVA)/bin/javah -o src/insertionsort.h -classpath build/classes sorter.InsertionSorter

run:
	$(JAVA)/bin/java -Djava.library.path=$(HERE)/lib -classpath build/classes sorter.Sorter $(ARGS)
