package dk.ozgur.ubs.controller;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dk.ozgur.ubs.Api;
import dk.ozgur.ubs.R;
import dk.ozgur.ubs.Utils;
import dk.ozgur.ubs.activity.MainActivity;
import dk.ozgur.ubs.User;
import dk.ozgur.ubs.model.UserDataRequest;
import dk.ozgur.ubs.model.UserGradeResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


/**
 * Created by ozgur on 3/24/17.
 */

public class GradesController extends BaseController {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager llm;
    private Call<UserGradeResponse> gradesTask;

    private ArrayList<Object> grades = new ArrayList<>();
    private GradesAdapter gradesAdapter;

    private static final int TYPE_DATE = 1;
    private static final int TYPE_GRADE = 2;

    protected Button mButtonShowGradesHistory;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_grades, null);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        llm = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(llm);

        gradesAdapter = new GradesAdapter();
        recyclerView.setAdapter(gradesAdapter);

        mButtonShowGradesHistory = (Button) view.findViewById(R.id.ButtonShowGradesHistory);
        mButtonShowGradesHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity activity = (MainActivity) getActivity();

                if(activity != null) {
                    activity.openController(new GradesHistoryController());
                }
            }
        });

        getGrades();
    }

    private void getGrades() {
        showProgress();
        gradesTask = getGradesRequest();
        gradesTask.enqueue(new Callback<UserGradeResponse>() {
            @Override
            public void onResponse(Response<UserGradeResponse> response) {
                if(response.isSuccess()) {
                    if(response.body().hasError()) {
                        toast(response.body().getMessage());
                    } else {
                        onGetGrades(response.body());
                    }
                } else {
                    toast(R.string.error);
                }
                hideProgress();
            }

            @Override
            public void onFailure(Throwable t) {
                toast(R.string.error);
                hideProgress();
            }
        });
    }

    protected Call<UserGradeResponse> getGradesRequest() {
        User user = new User(getActivity());
        UserDataRequest req = new UserDataRequest(user.getStudentNo(), user.getPassword(), user.getGroupId());
        return Api.service.getGrades(req);
    }

    private void onGetGrades(UserGradeResponse res) {
        grades = setModelsWithDateSeparator(res);
        gradesAdapter.notifyDataSetChanged();
    }

    protected ArrayList<Object> setModelsWithDateSeparator(UserGradeResponse res) {
        Map<String, ArrayList<UserGradeResponse.Course>> hashMap = new HashMap();

        for (UserGradeResponse.Course course: res.getCourseDetails()) {
            String semester = course.getSemester();
            if (hashMap.containsKey(semester)) {
                ((ArrayList) hashMap.get(semester)).add(course);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.add(course);
                hashMap.put(semester, arrayList);
            }
        }

        ArrayList<Object> models = new ArrayList<>();

        for (String semester : hashMap.keySet()) {

            models.add(semester);

            ArrayList<UserGradeResponse.Course> arr = ((ArrayList) hashMap.get(semester));

            for(UserGradeResponse.Course course: arr) {
                models.add(course);
            }
        }

        return models;
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);

        if(gradesTask != null) {
            gradesTask.cancel();
        }

    }

    @Override
    public String getControllerName() {
        return "grades";
    }

    private class GradesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder vh = null;
            View view;

            if(viewType == TYPE_DATE) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.row_grades_date, null);
                Utils.matchParentRecyclerViewRow(view);
                vh = new ViewHolderDate(view);
            }

            if(viewType == TYPE_GRADE) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.row_grades_grade, null);
                Utils.matchParentRecyclerViewRow(view);
                vh = new ViewHolderGrade(view);
            }

            return vh;

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            int type = getItemViewType(position);

            if(type == TYPE_DATE) {
                String date = (String) grades.get(position);
                ViewHolderDate vh = (ViewHolderDate) holder;
                ((ViewHolderDate) holder).mTextViewGradeDate.setText(date);
            }

            if(type == TYPE_GRADE) {
                UserGradeResponse.Course course = (UserGradeResponse.Course) grades.get(position);
                ViewHolderGrade vh = (ViewHolderGrade) holder;
                vh.mTextViewGradeCourseName.setText(course.getCourseName());
                vh.mTextViewGradeLetterNote.setText(course.getLetterGradeString());
                String letter = course.getLetterGradeString();

                if (letter.equals("AA") || letter.equals("BA") || letter.equals("BB") || letter.equals("CB")) {
                    vh.mTextViewGradeLetterNote.setTextColor(ContextCompat.getColor(getActivity(), R.color.grade_good));
                }
                if (letter.equals("M")) {
                    vh.mTextViewGradeLetterNote.setTextColor(ContextCompat.getColor(getActivity(), R.color.grade_muaf));
                }
                if (letter.equals("CC") || letter.equals("DC") || letter.equals("DD")) {
                    vh.mTextViewGradeLetterNote.setTextColor(ContextCompat.getColor(getActivity(), R.color.grade_notr));
                }
                if (letter.equals("FF") || letter.equals("FD") || letter.equals("DZ")) {
                    vh.mTextViewGradeLetterNote.setTextColor(ContextCompat.getColor(getActivity(), R.color.grade_bad));
                }

                String str = course.getGrade() != -10 ? "Not: " + course.getCourseCode() + "\n" : "";
                for (UserGradeResponse.CourseExam courseExam: course.getGrades()) {
                    str = str + courseExam.getExamName() + " : " + (courseExam.getExamPoint() == -1 ? "Girmedi" : Integer.valueOf(courseExam.getExamPoint())) + "\n";
                }

                if (letter.equals("M")) {
                    str = "Muaf";
                } else if (course.getGrades().size() == 0 && course.getGrade() == -10) {
                    str = "Henüz not yok.";
                }

                vh.mTextViewGradeNotes.setText(str);
            }
        }

        @Override
        public int getItemCount() {
            return grades.size();
        }

        @Override
        public int getItemViewType(int position) {
            Object item = grades.get(position);

            if(item instanceof String) {
                return TYPE_DATE;
            }

            return TYPE_GRADE;
        }
    }

    private class ViewHolderGrade extends RecyclerView.ViewHolder {

        private TextView mTextViewGradeCourseName;
        private TextView mTextViewGradeNotes;
        private TextView mTextViewGradeLetterNote;

        public ViewHolderGrade(View itemView) {
            super(itemView);
            mTextViewGradeCourseName = (TextView) itemView.findViewById(R.id.TextViewGradeCourseName);
            mTextViewGradeLetterNote = (TextView) itemView.findViewById(R.id.TextViewGradeLetterNote);
            mTextViewGradeNotes = (TextView) itemView.findViewById(R.id.TextViewGradeNotes);
        }
    }

    private class ViewHolderDate extends RecyclerView.ViewHolder {

        private TextView mTextViewGradeDate;

        public ViewHolderDate(View itemView) {
            super(itemView);
            mTextViewGradeDate = (TextView) itemView.findViewById(R.id.TextViewGradeDate);
        }
    }

}
