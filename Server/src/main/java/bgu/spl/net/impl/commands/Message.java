package bgu.spl.net.impl.commands;

import bgu.spl.net.srv.Database;

public abstract class Message {

    protected short opCode;
    protected final Database db = Database.getInstance();

    public abstract Message execute(String loggedUser);

    public String getUser() {
        return null;
    }

    public short getOpCode() {
        return opCode;
    }

    public short getMessageOpCode() {
        return 0;
    }

    public String getMsg() {
        return null;
    }

}
