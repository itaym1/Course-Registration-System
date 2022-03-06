package bgu.spl.net.impl.commands;

public class MyCoursesMSG extends Message {
    public MyCoursesMSG(){
        opCode = 11;
    }

    @Override
    public Message execute(String loggedUser) {
        try{
            String s = db.getCourses(loggedUser).toString().replaceAll(" ","");
            return new ACKMSG(opCode, s);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

}
