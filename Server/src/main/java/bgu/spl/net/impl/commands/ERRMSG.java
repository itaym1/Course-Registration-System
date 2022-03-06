package bgu.spl.net.impl.commands;

public class ERRMSG extends Message {
    private final short messageOpCode;

    @Override
    public Message execute(String loggedUser) {
        return null;
    }

    public ERRMSG(short messageOpCode){
        opCode = 13;
        this.messageOpCode = messageOpCode;
    }

    public short getMessageOpCode() {
        return messageOpCode;
    }

}
