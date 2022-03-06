package bgu.spl.net.impl.commands;

public class ACKMSG extends Message {

    private final String msg;
    private final short messageOpCode;

    @Override
    public Message execute(String loggedUser) {
        return null;
    }


    public ACKMSG(short opCode){
        this.opCode = 12;
        this.msg = "";
        this.messageOpCode = opCode;
    }

    public ACKMSG(short opCode,String msg){
        this.opCode = 12;
        this.msg = msg;
        this.messageOpCode = opCode;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public short getMessageOpCode() {
        return messageOpCode;
    }


}
