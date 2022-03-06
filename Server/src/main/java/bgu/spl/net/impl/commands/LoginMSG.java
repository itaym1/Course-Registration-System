package bgu.spl.net.impl.commands;

public class LoginMSG extends Message {

    private final String userName;
    private final String password;

    public LoginMSG(String userName,String password){
        opCode = 3;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public Message execute(String loggedUser) {
        try{
            if(db.login(userName,password))
                return new ACKMSG(opCode);
            return new ERRMSG(opCode);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

    @Override
    public String getUser() {
        return userName;
    }
}
