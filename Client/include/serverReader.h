//
// Created by spl211 on 10/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_SERVERREADER_H
#define BOOST_ECHO_CLIENT_SERVERREADER_H
#include <iostream>
#include <thread>
#include "connectionHandler.h"

class serverReader {
private:
    ConnectionHandler & _ch;

public:
    serverReader(ConnectionHandler &ch);
    void run();
};


#endif //BOOST_ECHO_CLIENT_SERVERREADER_H
