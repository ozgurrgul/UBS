package dk.ozgur.ubs.model;

import android.util.Log;

import java.util.ArrayList;

public class UserWeeklyScheduleResponse extends BaseResponse {

    private ArrayList<Course> WeeklyCourses;

    public class Course {
        private String lessonStart;
        private String lessonFinish;
        private String lessonDay;
        private String InstructorName;
        private String courseName;
        private String description;
        private String workCenterName;


        public String getCourseName() {
            return this.courseName;
        }

        public String getDescription() {
            return this.description;
        }

        public String getInstructorName() {
            return this.InstructorName;
        }

        public String getWorkCenterName() {
            return this.workCenterName;
        }

        public String getCourseDay() {
            return lessonDay;
        }

        public String getCourseFinish() {
            return lessonFinish;
        }

        public String getCourseStart() {
            return lessonStart;
        }

        public void calculateDay() {
            if (this.description.indexOf(" - ") > 0) {
                String[] split = description.split(" - ");
                if (split.length == 3) {
                    lessonDay = split[0];
                    lessonStart = split[1];
                    lessonFinish = split[2];
                }
            }
        }
    }

    public ArrayList<Course> getWeeklyCourses() {
        return this.WeeklyCourses;
    }
}