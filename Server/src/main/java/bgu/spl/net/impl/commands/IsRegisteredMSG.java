package bgu.spl.net.impl.commands;

public class IsRegisteredMSG extends Message {
    private final int courseNum;

    public IsRegisteredMSG(int courseNum) {
        opCode=9;
        this.courseNum = courseNum;
    }
    @Override
    public Message execute(String loggedUser) {
        try {
            String s;
            if (db.isRegistered(loggedUser, courseNum))
                s = "REGISTERED";
            else
                s = "NOT REGISTERED";
            return new ACKMSG(opCode, s);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

}
