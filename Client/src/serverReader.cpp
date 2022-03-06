//
// Created by spl211 on 10/01/2021.
//

#include "serverReader.h"

serverReader::serverReader(ConnectionHandler &ch): _ch(ch) {

}

void serverReader::run() {
    while (1) {
        std::string answer;
        // Get back an answer: by using the expected number of bytes (len bytes + newline delimiter)
        // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
        if (!_ch.getLine(answer)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        std::cout << answer << std::endl;
        if (answer == "ACK 4") {
            _ch.close();
            break;
        }
        else if(answer == "ERROR 4") {
            _ch.continueRun();
        }
    }
}
