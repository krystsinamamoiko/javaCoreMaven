package lesson09;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Data {

    private static List<Course> coursesList = Arrays.asList(
        new Course("Java Basic"),
        new Course("Java Core"),
        new Course("Web UI Automation"),
        new Course("Back Automation"),
        new Course("QA Basic Theory. Manual testing"),
        new Course("QA Advance Theory. Manual testing"),
        new Course("Selenium"),
        new Course("Cypress"),
        new Course("Cucumber"),
        new Course("Postman"),
        new Course("SoapUI"),
        new Course("Charles"),
        new Course("Appium"),
        new Course("XML and JSON"),
        new Course("HTML and CSS for QA"),
        new Course("Git Basic"));

    static public List<Student> getStudentsList(int number) {
        List<Student> students = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= number; i++) {
            Student student = new Student("Student" + i, getRandomCourseList(random.nextInt(coursesList.size())));
            students.add(student);
        }
        return students;
    }

    static private List<Course> getRandomCourseList(int listSize) {
        List<Course> courses = new ArrayList<>();
        Random random = new Random();
        while(courses.size() != listSize) {
            Course course = coursesList.get(random.nextInt(coursesList.size()));
            if (!courses.contains(course)) {
                courses.add(course);
            }
        }
        return courses;
    }

    static public Course getRandomCourse() {
        Random random = new Random();
        return coursesList.get(random.nextInt(coursesList.size()));
    }
}
