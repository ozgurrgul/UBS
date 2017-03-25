package dk.ozgur.ubs.controller;

import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dk.ozgur.ubs.Api;
import dk.ozgur.ubs.User;
import dk.ozgur.ubs.model.UserDataRequest;
import dk.ozgur.ubs.model.UserGradeResponse;
import retrofit.Call;

/**
 * Created by ozgur on 3/24/17.
 */

public class GradesHistoryController extends GradesController {

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        mButtonShowGradesHistory.setVisibility(View.GONE);
    }

    @Override
    protected Call<UserGradeResponse> getGradesRequest() {
        User user = new User(getActivity());
        UserDataRequest req = new UserDataRequest(user.getStudentNo(), user.getPassword(), user.getGroupId());
        return Api.service.getGradesHistory(req);
    }

    @Override
    protected ArrayList<Object> setModelsWithDateSeparator(UserGradeResponse res) {

        ArrayList<UserGradeResponse.Course> courses = res.getCourseDetails();
        Map<String, ArrayList<UserGradeResponse.Course>> hashMap = new HashMap();

        for (UserGradeResponse.Course course: courses) {
            String hashKey = course.getYear() + " - " + course.getSemester();
            if (hashMap.containsKey(hashKey)) {
                ((ArrayList) hashMap.get(hashKey)).add(course);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(course);
                hashMap.put(hashKey, arrayList);
            }
        }

        ArrayList<Object> models = new ArrayList<>();

        for (String hashKey : hashMap.keySet()) {

            models.add(hashKey);

            ArrayList<UserGradeResponse.Course> arr = ((ArrayList) hashMap.get(hashKey));

            for(UserGradeResponse.Course course: arr) {
                models.add(course);
            }
        }

        return models;
    }

    @Override
    public String getControllerName() {
        return "grades_history";
    }
}
