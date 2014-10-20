JAVA=/usr/java/jdk1.7.0_25
HERE= $(shell pwd)

libinsertionsort.so: src/insertionsort.c src/insertionsort.h
	gcc -fPIC -I$(JAVA)/include -I$(JAVA)/include/linux -o lib/libinsertionsort.so -shared src/insertionsort.c

insertionsort.h:
	$(JAVA)/bin/javah -o src/insertionsort.h -classpath build/classes sorter.InsertionSorter

run:
	$(JAVA)/bin/java -Djava.library.path=$(HERE)/lib -classpath build/classes sorter.Sorter $(ARGS)
