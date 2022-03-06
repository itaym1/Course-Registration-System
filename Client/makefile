CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:= -lboost_system -lboost_thread -pthread

all: Client
	g++ -Wall -Weffc++ -o bin/BGRSclient bin/connectionHandler.o bin/bgrsClient.o bin/clientReader.o bin/serverReader.o $(LDFLAGS)

Client: bin/connectionHandler.o bin/bgrsClient.o bin bin/clientReader.o bin/serverReader.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/bgrsClient.o: src/bgrsClient.cpp
	g++ $(CFLAGS) -o bin/bgrsClient.o src/bgrsClient.cpp

bin/clientReader.o: src/clientReader.cpp
	g++ $(CFLAGS) -o bin/clientReader.o src/clientReader.cpp

bin/serverReader.o: src/serverReader.cpp
	g++ $(CFLAGS) -o bin/serverReader.o src/serverReader.cpp

.PHONY: clean
clean:
	rm -f bin/*
