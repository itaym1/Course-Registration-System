package bgu.spl.net.impl.commands;

public class LogoutMSG extends Message {

    public LogoutMSG(){
        opCode = 4;
    }

    @Override
    public Message execute(String loggedUser) {
        try{
            if (db.logout(loggedUser))
                return new ACKMSG(opCode);
            return new ERRMSG(opCode);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }
}
