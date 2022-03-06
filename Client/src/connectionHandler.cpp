#include <connectionHandler.h>
#include <expat.h>

using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;

ConnectionHandler::ConnectionHandler(string host, short port): host_(host), port_(port), io_service_(), socket_(io_service_), cv(), shouldTerminate(false), mtx() {}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}
 
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(std::string& line) {
    return decode(line);
}

bool ConnectionHandler::decode(std::string& line) {
    char ch;
    short opCode = 0;
    short msgOpCode = 0;
    string optMsg = "";
    // Stop when we encounter the null character.
    // Notice that the null character is not appended to the frame string.
    try {
        do{
            if(!getBytes(&ch, 1)) return false;
            if(opCode == 0) {
                opCode = ch;
            }
            else if(msgOpCode == 0) {
                msgOpCode = ch;
                if(msgOpCode != 0 && opCode == 13) {
                    line = "ERROR "+ std::to_string(msgOpCode);
                    return true;
                }
            }
            else if(ch != '\0'){
                optMsg += ch;
            }
            else {
                line = "ACK "+ std::to_string(msgOpCode);
                if(!optMsg.empty())
                    line += "\n"+ optMsg;
                return true; // finish read optional string (ACK)
            }
        }while (1);
    } catch (std::exception& e) {
        std::cerr << "recv failed2 (Error: " << e.what() << ')' << std::endl;
        return false;
    }
}

bool ConnectionHandler::sendLine(std::string& line) {
    std::vector<char> bytes = std::vector<char>();
    encode(bytes, line);
    return sendBytes(&bytes[0] ,bytes.size());
}

void ConnectionHandler::encode(std::vector<char>& bytes, string& line) {
    std::vector<string> tokens;
    boost::split(tokens, line, boost::is_any_of(" "));
    short opCode;
    if(tokens[0] == "ADMINREG") {
        opCode = 1;
    } else if(tokens[0] == "STUDENTREG") {
        opCode = 2;
    } else if(tokens[0] == "LOGIN") {
        opCode = 3;
    }else if(tokens[0] == "LOGOUT") {
        opCode = 4;
    }else if(tokens[0] == "COURSEREG") {
        opCode = 5;
    }else if(tokens[0] == "KDAMCHECK") {
        opCode = 6;
    }else if(tokens[0] == "COURSESTAT") {
        opCode = 7;
    }else if(tokens[0] == "STUDENTSTAT") {
        opCode = 8;
    }else if(tokens[0] == "ISREGISTERED") {
        opCode = 9;
    }else if(tokens[0] == "UNREGISTER") {
        opCode = 10;
    }else if(tokens[0] == "MYCOURSES") {
        opCode = 11;
    } else { throw std::invalid_argument("No such command: "+ tokens[0]);}

    shortToBytes(opCode, bytes);
    if(opCode == 5 || opCode == 6 || opCode == 7 || opCode == 9 || opCode == 10) {
        short courseNum = (short)std::stoi(tokens[1]);
        shortToBytes(courseNum, bytes);
    }
    else if(opCode == 1 || opCode == 2 || opCode == 3 || opCode == 8) {
        const char* str = tokens[1].c_str();
        for(int i=0; str[i] != '\0'; i++) {
            bytes.push_back(str[i]);
        }
        bytes.push_back('\0');
        if(opCode == 8) { return ; }
        str = tokens[2].c_str();
        for(int i=0; str[i] != '\0'; i++) {
            bytes.push_back(str[i]);
        }
        bytes.push_back('\0');
    }
}
void ConnectionHandler::shortToBytes(short num, std::vector<char> &bytesArr) {
    bytesArr.push_back((num >> 8) & 0xFF);
    bytesArr.push_back(num & 0xFF);
}

bool ConnectionHandler::checkTerminate() {
    std::unique_lock<std::mutex> ul(mtx);
    cv.wait(ul);
    return shouldTerminate;
}

void ConnectionHandler::continueRun() {
    cv.notify_one();
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
        shouldTerminate = true;
        cv.notify_one();
    } catch (...) {
        std::cout << "closing failed: connection already closed" << std::endl;
    }
}

