package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.commands.BGUEncoderDecoder;
import bgu.spl.net.impl.commands.BGUProtocol;
import bgu.spl.net.impl.commands.Message;
import bgu.spl.net.srv.Server;

import java.util.function.Supplier;

public class TPCMain {
    public static void main(String[] args) {
        if (args.length < 1 )
            throw new IllegalArgumentException("args should be <port>");
        int port;
        try{
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("args should be <port> as number");
        }
        Supplier<MessagingProtocol<Message>> protocolSupplier = BGUProtocol::new;
        Supplier<MessageEncoderDecoder<Message>> encDecSupplier = BGUEncoderDecoder::new;
        Server.threadPerClient(
                port,
                protocolSupplier,
                encDecSupplier
        ).serve();
    }
}
