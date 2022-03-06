package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.commands.BGUEncoderDecoder;
import bgu.spl.net.impl.commands.BGUProtocol;
import bgu.spl.net.impl.commands.Message;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Supplier;

public class ReactorMain {
    public static void main(String[] args) throws UnknownHostException {
        if (args.length < 2 )
            throw new IllegalArgumentException("args should be <port> <No of threads>");
        int numOfThreads,port;
        try{
            numOfThreads = Integer.parseInt(args[1]);
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("args should be <port> <No of threads> as numbers");
        }
        Supplier<MessagingProtocol<Message>> protocolSupplier = BGUProtocol::new;
        Supplier<MessageEncoderDecoder<Message>> encDecSupplier = BGUEncoderDecoder::new;
        Server.reactor(
                numOfThreads, // num of threads
                port, //port
                protocolSupplier, //protocol factory
                encDecSupplier //message encoder decoder factory
        ).serve();
    }
}
