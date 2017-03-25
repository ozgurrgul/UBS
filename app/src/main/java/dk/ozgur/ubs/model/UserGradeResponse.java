package dk.ozgur.ubs.model;

import java.util.ArrayList;

public class UserGradeResponse extends BaseResponse {

    private ArrayList<Course> courseDetails;

    public class Course {
        private String LetterGradeString;
        private String courseCode;
        private String courseName;
        private int grade;
        private ArrayList<CourseExam> grades;
        private String instructorName;
        private String instructorSurname;
        private String instructorTitle;
        private String semester;
        private int year;

        public String getCourseCode() {
            return this.courseCode;
        }

        public String getCourseName() {
            return this.courseName;
        }

        public int getGrade() {
            return this.grade;
        }

        public ArrayList<CourseExam> getGrades() {
            return this.grades;
        }

        public String getInstructorName() {
            return this.instructorName;
        }

        public String getInstructorSurname() {
            return this.instructorSurname;
        }

        public String getInstructorTitle() {
            return this.instructorTitle;
        }

        public String getLetterGradeString() {
            return this.LetterGradeString;
        }

        public String getSemester() {
            return this.semester;
        }

        public int getYear() {
            return this.year;
        }
    }

    public class CourseExam {
        private String examName;
        private int examPoint;

        public String getExamName() {
            return this.examName;
        }

        public int getExamPoint() {
            return this.examPoint;
        }
    }

    public ArrayList<Course> getCourseDetails() {
        return this.courseDetails;
    }
}