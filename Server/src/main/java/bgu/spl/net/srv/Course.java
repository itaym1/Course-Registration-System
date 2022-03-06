package bgu.spl.net.srv;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Course {

    private final int courseNum;
    private final String courseName;
    private final List<Integer> kdamCoursesList;
    private final int numOfMaxStudents;
    private final List<String> registeredStudents;


    public Course(int courseNum, String courseName,LinkedList<Integer> kdamCoursesList,int numOfMaxStudents){
        this.courseName = courseName;
        this.courseNum = courseNum;
        this.kdamCoursesList = kdamCoursesList;
        this.numOfMaxStudents = numOfMaxStudents;
        registeredStudents = new ArrayList<>();
    }

    public String toString(){
        return courseNum +": "+ courseName+", " + kdamCoursesList +", " +numOfMaxStudents;
    }

    public int getNumOfMaxStudents() {
        return numOfMaxStudents;
    }

    public List<Integer> getKdamCoursesList() {
        return kdamCoursesList;
    }

    public String getCourseName() {
        return courseName;
    }

    public boolean isFull() {
        return registeredStudents.size() == numOfMaxStudents;
    }
    public boolean registerStudent(String userName) {
        if (isFull()) return false;
        return registeredStudents.add(userName);
    }

    public boolean unRegisterStudent(String userName) {
        if (!registeredStudents.contains(userName)) return false;
        return registeredStudents.remove(userName);
    }

    public List<String> getRegisteredStudents() {
        return registeredStudents;
    }

}
