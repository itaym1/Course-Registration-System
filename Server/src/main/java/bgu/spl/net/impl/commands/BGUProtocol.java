package bgu.spl.net.impl.commands;

import bgu.spl.net.api.MessagingProtocol;

public class BGUProtocol implements MessagingProtocol<Message> {

    private String loggedUser;
    private boolean shouldTerminate;

    public BGUProtocol(){
        loggedUser = null;
        shouldTerminate = false;
    }

    @Override
    public Message process(Message msg) {
        short op = msg.getOpCode();

        if (loggedUser != null && ( op == 1 || op == 2 || op==3 ))
            return new ERRMSG(op);
        if (loggedUser == null && op != 1 & op != 2 & op != 3)
            return new ERRMSG(op);
        Message m = msg.execute(loggedUser);
        if (op == 3 && m.getOpCode() == 12) // login
            loggedUser = msg.getUser();
        if (op == 4 && m.getOpCode() == 12) {// logout
            shouldTerminate = true;
            loggedUser = null;
        }
        return m;
    }

    @Override
    public boolean shouldTerminate() {
        return shouldTerminate;
    }
}
