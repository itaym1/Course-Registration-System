package bgu.spl.net.impl.commands;

public class CourseStatMSG extends Message {
    private final int courseNum;

    public CourseStatMSG(int courseNum) {
        opCode=7;
        this.courseNum = courseNum;
    }
    @Override
    public Message execute(String loggedUser) {
        try{
            String s = db.getCourseStatus(loggedUser, courseNum);
            return new ACKMSG(opCode, s);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }
}
