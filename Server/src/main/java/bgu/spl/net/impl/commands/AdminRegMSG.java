package bgu.spl.net.impl.commands;

public class AdminRegMSG extends Message {
    private final String userName;
    private final String password;

    public AdminRegMSG(String userName,String password){
        opCode = 1;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Message execute(String loggedUser) {
        try{
            if (db.adminRegister(userName,password))
                return new ACKMSG(opCode);
            return new ERRMSG(opCode);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

}
