//
// Created by spl211 on 06/01/2021.
//

#include "clientReader.h"

clientReader::clientReader(ConnectionHandler &ch): _ch(ch){ }

void clientReader::run() {
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
		std::string line(buf);
        if (!_ch.sendLine(line)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        if (line == "LOGOUT" && _ch.checkTerminate()) {
            break;
        }
    }
}


