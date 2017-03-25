package dk.ozgur.ubs;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by ozgur on 3/24/17.
 */

public class User {

    private CPM cpm;

    public User(Context context) {
        cpm = new CPM(context);
    }

    // 142802007
    public User setStudentNo(String str) {
        cpm.putString("studentNo_", str);
        return this;
    }

    // pw
    public User setPassword(String str) {
        cpm.putString("password_", str);
        return this;
    }

    // Özgür GÜL
    public User setStudentName(String str) {
        cpm.putString("studentName_", str);
        return this;
    }

    // img data
    public User setImage(String str) {
        cpm.putString("image_", str);
        return this;
    }

    public User setGroupId(int i) {
        cpm.putInt("groupId_", i);
        return this;
    }

    public User setStudentId(int i) {
        cpm.putInt("studentId_", i);
        return this;
    }

    public String getStudentNo() {
        return cpm.getString("studentNo_");
    }

    public String getPassword() {
        return cpm.getString("password_");
    }

    public String getStudentName() {
        return cpm.getString("studentName_");
    }

    public String getImage() {
        return cpm.getString("image_");
    }

    public int getStudentId() {
        return cpm.getInt("studentId_");
    }

    public int getGroupId() {
        return cpm.getInt("groupId_");
    }

    public boolean isLogged() {
        return !TextUtils.isEmpty(getStudentNo());
    }


    public void logout() {
        setStudentNo(null);
        setPassword(null);
        setGroupId(-1);
        setImage(null);
        setStudentName(null);
    }
}
