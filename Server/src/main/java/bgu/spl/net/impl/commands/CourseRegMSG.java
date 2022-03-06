package bgu.spl.net.impl.commands;

public class CourseRegMSG extends Message {

    private final int courseNum;

    public CourseRegMSG(int courseNum) {
        opCode=5;
        this.courseNum = courseNum;
    }
    @Override
    public Message execute(String loggedUser) {
        try{
            if (db.registerUserForCourse(loggedUser, courseNum))
                return new ACKMSG(opCode);
            return new ERRMSG(opCode);
        }
        catch(Exception exception){
            return new ERRMSG(opCode);
        }
    }

}
