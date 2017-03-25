package dk.ozgur.ubs.model;

/**
 * Created by ozgur on 3/24/17.
 */


public class UserLoginResponse extends BaseResponse {

    private UserLoginResponseUser user;

    public UserLoginResponseUser getUser() {
        return this.user;
    }

    public class UserLoginResponseUser {
        private UserLoginResponseStudent student;

        public UserLoginResponseStudent getStudent() {
            return this.student;
        }
    }

    public class UserLoginResponseStudent {
        private String DepartmentName;
        private int Id;
        private int StudentId;
        private int UsersGroupId;
        private String image;
        private String name;
        private String status;
        private String studentNo;
        private int subUnitId;
        private String surname;

        public String getDepartmentName() {
            return this.DepartmentName;
        }

        public int getId() {
            return this.Id;
        }

        public String getImage() {
            return this.image;
        }

        public String getName() {
            return this.name;
        }

        public String getStatus() {
            return this.status;
        }

        public int getStudentId() {
            return this.StudentId;
        }

        public String getStudentNo() {
            return this.studentNo;
        }

        public int getSubUnitId() {
            return this.subUnitId;
        }

        public String getSurname() {
            return this.surname;
        }

        public int getUsersGroupId() {
            return this.UsersGroupId;
        }
    }
}