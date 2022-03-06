package bgu.spl.net.impl.commands;

public class StudentStatMSG extends Message {
    private final String userName;

    public StudentStatMSG(String userName) {
        opCode=8;
        this.userName = userName;
    }
    @Override
    public Message execute(String loggedUser) {
        try{
            String s = db.getStudentStatus(loggedUser, userName);
            return new ACKMSG(opCode, s);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

}
