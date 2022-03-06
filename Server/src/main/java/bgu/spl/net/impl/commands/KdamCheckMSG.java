package bgu.spl.net.impl.commands;

public class KdamCheckMSG extends Message {
    private final int courseNum;

    public KdamCheckMSG(int courseNum) {
        opCode=6;
        this.courseNum = courseNum;
    }
    @Override
    public Message execute(String loggedUser) {
        try{
            String s = db.kdamCheck(loggedUser, courseNum);
            return new ACKMSG(opCode, s);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

}
