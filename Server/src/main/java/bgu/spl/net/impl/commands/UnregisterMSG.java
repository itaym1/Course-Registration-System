package bgu.spl.net.impl.commands;

public class UnregisterMSG extends Message {
    private final int courseNum;

    public UnregisterMSG(int courseNum) {
        opCode=10;
        this.courseNum = courseNum;
    }
    @Override
    public Message execute(String loggedUser) {
        try {
            if(db.unregisterCourse(loggedUser, courseNum))
                return new ACKMSG(opCode);
            return new ERRMSG(opCode);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

}
