//
// Created by spl211 on 06/01/2021.
//

#ifndef BOOST_ECHO_CLIENT_CLIENTREADER_H
#define BOOST_ECHO_CLIENT_CLIENTREADER_H
#include <iostream>
#include <thread>
#include "connectionHandler.h"

class clientReader {
private:
    ConnectionHandler & _ch;

public:
    clientReader(ConnectionHandler &ch);
    void run();
};


#endif //BOOST_ECHO_CLIENT_CLIENTREADER_H
