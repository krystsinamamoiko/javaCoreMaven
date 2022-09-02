package lesson09;

import java.util.List;
import java.util.Objects;

public class Student {

    private final String name;
    private final List<Course> courses;

    public Student(String name, List<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(name, student.name) && Objects.equals(courses, student.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, courses);
    }

    @Override
    public String toString() {
        return "Student '" + name + "' with courses (" + courses.size() + ") = " + courses + "}\n";
    }
}
