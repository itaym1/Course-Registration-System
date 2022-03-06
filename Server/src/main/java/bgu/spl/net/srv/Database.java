package bgu.spl.net.srv;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {

    private final ConcurrentHashMap<String, User> users; //string <-- userName
    private final LinkedHashMap<Integer, Course> courses; //Integer <-- courseNum


    private static class SingletonHolder {
        private static final Database instance = new Database();
    }


    //to prevent user from creating new Database
    private Database() {
        // TODO: implement
        users = new ConcurrentHashMap<>();
        courses = new LinkedHashMap<>();
        initialize("./Courses.txt");
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    public boolean initialize(String coursesFilePath) {
        // TODO: implement
        File file = new File(coursesFilePath);
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) { // every iteration read one line = one course info
                String currLine = reader.nextLine();
                int courseNum = Integer.parseInt(currLine.substring(0, currLine.indexOf("|")));
                currLine = currLine.substring(currLine.indexOf("|") + 1);
                String courseName = currLine.substring(0, currLine.indexOf("|"));
                currLine = currLine.substring(currLine.indexOf("|") + 1);
                // read the kdamCourses
                String kdamCourses = currLine.substring(1, currLine.indexOf("|") - 1);
                LinkedList<Integer> kdamCoursesList = new LinkedList<>();
                while (kdamCourses.length() > 0) {
                    int currCourse;
                    if (kdamCourses.contains(",")) { // kdamCourses have more courses
                        currCourse = Integer.parseInt(kdamCourses.substring(0, kdamCourses.indexOf(",")));
                        kdamCoursesList.add(currCourse);
                        kdamCourses = kdamCourses.substring(kdamCourses.indexOf(",") + 1);
                    } else { // last course
                        kdamCoursesList.add(Integer.parseInt(kdamCourses));
                        kdamCourses = "";
                    }
                }
                currLine = currLine.substring(currLine.indexOf("|") + 1);
                // read the numOfMaxStudents
                int numOfMaxStudents = Integer.parseInt(currLine);
                // add course to the DB
                courses.put(courseNum, new Course(courseNum, courseName, kdamCoursesList, numOfMaxStudents));
            }
        } catch (FileNotFoundException exception) {
            return false;
        }
        return true;
    }

    public boolean adminRegister(String userName, String password) throws Exception {
        if (users.containsKey(userName)) {
            throw new Exception("this user:" + userName + " is already registered");
        }
        User newAdmin = new User(userName, password, true);
        return (users.putIfAbsent(userName, newAdmin) == null);
    }

    public boolean studentRegister(String userName, String password) throws Exception {
        if (users.containsKey(userName)) {
            throw new Exception("this user:" + userName + " is already registered");
        }
        User newUser = new User(userName, password, false);
        return users.putIfAbsent(userName, newUser) == null;
    }

    public boolean login(String userName, String password) throws Exception {
        if (!users.containsKey(userName)) {
            throw new Exception("this user:" + userName + " is not exist");
        }
        User currUser = users.get(userName);
        return currUser.login(password);
    }

    public boolean logout(String loggedUser) {
        if (loggedUser != null) {
            User currUser = users.get(loggedUser);
            return currUser.logout();
        }
        return false;
    }

    public boolean registerUserForCourse(String loggedUser, Integer course) throws Exception {
        User currUser = users.get(loggedUser);
        if (currUser.isAdmin()) {
            throw new Exception("admin cannot registered to courses");
        }
        Course currCourse = courses.get(course);
        if (currCourse == null || !currUser.checkKdam(currCourse.getKdamCoursesList())) { // this is also check if course exist
            throw new Exception("this user don't have all the kdam courses");
        }
        synchronized (currCourse) {
            if (currCourse.isFull()) return false;
            return currUser.registerCourse(course) && currCourse.registerStudent(loggedUser); // check already registered
        }
    }


    public String kdamCheck(String loggedUser, Integer courseNum) throws Exception {
        Course c = courses.get(courseNum);
		if (users.get(loggedUser).isAdmin()) {
			throw new Exception("only student can get course kdams");
		}
        if (c == null) {
            throw new Exception("this course:" + courseNum + " is not exist");
        }
        return c.getKdamCoursesList().toString().replaceAll(" ", "");
    }

    public String getCourseStatus(String loggedUser, Integer courseNum) throws Exception {
        if (!users.get(loggedUser).isAdmin()) {
            throw new Exception("only admin can get course info");
        }
        Course course = courses.get(courseNum);
        if (course == null) {
            throw new Exception("the course: " + courseNum + " is not exist");
        }
        List<String> studentNames = course.getRegisteredStudents();
        studentNames.sort(Comparator.naturalOrder());
        int maxStudents = course.getNumOfMaxStudents();
        return  "Course: (" + courseNum + ") " + course.getCourseName() + "\n" +
                "Seats Available: " + (maxStudents -studentNames.size()) + "/" + maxStudents + "\n" +
                "Students Registered: " + studentNames.toString().replaceAll(" ", "");
    }

	public String getStudentStatus(String loggedUser, String userName) throws Exception {
		if (!users.get(loggedUser).isAdmin()) {
            throw new Exception("only admin can get course info");
		}
		User requestedUser = users.get(userName);
		if (requestedUser == null) {
            throw new Exception("the student: " + userName + " is not exist");
		}
		List<Integer> userCourses = requestedUser.getRegisteredCourses();
		ArrayList<Integer> courseByOrder = new ArrayList<>();
        for (Integer c : courses.keySet()) {
            if (userCourses.contains(c)) {
                courseByOrder.add(c);
            }
        }
        return "Student: " + userName  + "\n" +
				"Courses: " + courseByOrder.toString().replaceAll(" ","");
	}

	public boolean isRegistered(String loggedUser, Integer courseNum) throws Exception {
        User currUser = users.get(loggedUser);
		if (currUser.isAdmin()) {
			throw new Exception("only student can get check register");
		}
		if(!courses.containsKey(courseNum)){
			throw new Exception("this course is not exist");
		}
		return currUser.getRegisteredCourses().contains(courseNum);
	}

	public boolean unregisterCourse(String loggedUser, Integer courseNum) throws Exception{
        User currUser = users.get(loggedUser);
        if (currUser.isAdmin()) {
            throw new Exception("admin cannot registered to courses");
        }
        Course currCourse = courses.get(courseNum);
        if (currCourse == null) { // this is also check if course exist
            throw new Exception("this user don't have all the kdam courses");
        }
        synchronized (currCourse) {
            return currUser.unregisterCourse(courseNum) && currCourse.unRegisterStudent(loggedUser);
        }
	}

	public List<Integer> getCourses(String loggedUser) throws Exception {
		User currUser = users.get(loggedUser);
		if (users.get(loggedUser).isAdmin()) {
			throw new Exception("only student can view his courses");
		}
		return currUser.getRegisteredCourses();
	}

}
