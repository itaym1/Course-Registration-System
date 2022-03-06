package bgu.spl.net.srv;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final boolean isAdmin;
    private final String userName;
    private final String password;
    private final List<Integer> registeredCourses;
    private boolean isLoggedIn;

    public User(String userName, String password,boolean admin){
        this.userName = userName;
        this.password = password;
        this.isAdmin = admin;
        registeredCourses = new ArrayList<>();
        isLoggedIn = false;
    }

    public boolean isAdmin(){
        return this.isAdmin;
    }

    public boolean registerCourse(Integer courseNum) {
        if (registeredCourses.contains(courseNum)) return false;
        return registeredCourses.add(courseNum);
    }

    public boolean unregisterCourse(Integer courseNum) {
        return registeredCourses.remove(courseNum);
    }

    public boolean login(String password) {
        if (!this.password.equals(password) || isLoggedIn) return false;
        isLoggedIn = true;
        return true;
    }

    public boolean logout() {
        if (!isLoggedIn) return false;
        isLoggedIn = false;
        return true;
    }

    public boolean checkKdam(List<Integer> kdamCoursesList) {
        for (Integer course : kdamCoursesList) {
            if (!registeredCourses.contains(course)) return false;
        }
        return true;
    }

    public List<Integer> getRegisteredCourses() {
        return registeredCourses;
    }
}
