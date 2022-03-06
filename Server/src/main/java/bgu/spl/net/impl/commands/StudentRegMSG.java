package bgu.spl.net.impl.commands;

public class StudentRegMSG extends Message {

    private final String userName;
    private final String password;

    public StudentRegMSG(String userName,String password){
        opCode = 2;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Message execute(String loggedUser) {
        try{
            if (db.studentRegister(userName,password))
                return new ACKMSG(opCode);
            return new ERRMSG(opCode);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

}
