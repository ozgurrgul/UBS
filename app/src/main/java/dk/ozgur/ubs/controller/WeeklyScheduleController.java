package dk.ozgur.ubs.controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import dk.ozgur.ubs.Api;
import dk.ozgur.ubs.R;
import dk.ozgur.ubs.Utils;
import dk.ozgur.ubs.User;
import dk.ozgur.ubs.model.UserDataRequest;
import dk.ozgur.ubs.model.UserWeeklyScheduleResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by ozgur on 3/24/17.
 */

public class WeeklyScheduleController extends BaseController {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager llm;
    private Call<UserWeeklyScheduleResponse> weeklyTask;

    private ArrayList<Object> weeklyCourses = new ArrayList<>();
    private WeeklyScheduleAdapter weeklyScheduleAdapter;

    private static final int TYPE_DAY = 1;
    private static final int TYPE_COURSE = 2;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_weekly_schedule, null);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        llm = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(llm);

        weeklyScheduleAdapter = new WeeklyScheduleAdapter();
        recyclerView.setAdapter(weeklyScheduleAdapter);

        getWeeklyScheduleList();
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);

        if(weeklyTask != null) {
            weeklyTask.cancel();
        }

    }

    private void getWeeklyScheduleList() {
        showProgress();
        User user = new User(getActivity());
        UserDataRequest req = new UserDataRequest(user.getStudentNo(), user.getPassword(), user.getGroupId());
        weeklyTask = Api.service.getWeeklySchedule(req);
        weeklyTask.enqueue(new Callback<UserWeeklyScheduleResponse>() {
            @Override
            public void onResponse(Response<UserWeeklyScheduleResponse> response) {
                if(response.isSuccess()) {

                    if(response.body().hasError()) {
                        toast(response.body().getMessage());
                    } else {
                        onGetWeeklyScheduleListResponse(response.body());
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

    private void onGetWeeklyScheduleListResponse(UserWeeklyScheduleResponse res) {
        weeklyCourses = setDateSeparator(res.getWeeklyCourses());
        weeklyScheduleAdapter.notifyDataSetChanged();
    }

    private ArrayList<Object> setDateSeparator(ArrayList<UserWeeklyScheduleResponse.Course> weeklyCourses) {

        ArrayList<Object> models = new ArrayList<>();
        String lastDay = "";

        for (UserWeeklyScheduleResponse.Course course: weeklyCourses) {

            course.calculateDay();
            String courseDay = course.getCourseDay();

            if (courseDay.equals(lastDay)) {
                //noop
            } else {
                models.add(courseDay);
            }
            models.add(course);
            lastDay = courseDay;
        }

        return models;
    }

    @Override
    public String getControllerName() {
        return "weekly";
    }

    private class WeeklyScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            RecyclerView.ViewHolder vh = null;
            View view;

            if(viewType == TYPE_DAY) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.row_weekly_schedule_day, null);
                Utils.matchParentRecyclerViewRow(view);
                vh = new ViewHolderDay(view);
            }

            if(viewType == TYPE_COURSE) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.row_weekly_schedule_course, null);
                Utils.matchParentRecyclerViewRow(view);
                vh = new ViewHolderCourse(view);
            }

            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            int type = getItemViewType(position);

            if(type == TYPE_DAY) {
                ViewHolderDay vh = (ViewHolderDay) holder;
                String day = (String) weeklyCourses.get(position);
                vh.mTextViewCourseDay.setText(day);
            }

            if(type == TYPE_COURSE) {
                ViewHolderCourse vh = (ViewHolderCourse) holder;
                final UserWeeklyScheduleResponse.Course course = (UserWeeklyScheduleResponse.Course) weeklyCourses.get(position);
                vh.mTextViewCourseName.setText(course.getCourseName().substring(11));
                vh.mTextViewCourseDate.setText(course.getCourseStart() + " - " +  course.getCourseFinish());
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast(course.getInstructorName());
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return weeklyCourses.size();
        }

        @Override
        public int getItemViewType(int position) {

            Object item = weeklyCourses.get(position);

            if(item instanceof String) {
                return TYPE_DAY;
            }

            return TYPE_COURSE;
        }
    }

    private class ViewHolderCourse extends RecyclerView.ViewHolder {

        private TextView mTextViewCourseName;
        private TextView mTextViewCourseDate;

        public ViewHolderCourse(View itemView) {
            super(itemView);
            mTextViewCourseName = (TextView) itemView.findViewById(R.id.TextViewCourseName);
            mTextViewCourseDate = (TextView) itemView.findViewById(R.id.TextViewCourseDate);
        }
    }

    private class ViewHolderDay extends RecyclerView.ViewHolder {

        private TextView mTextViewCourseDay;


        public ViewHolderDay(View itemView) {
            super(itemView);
            mTextViewCourseDay = (TextView) itemView.findViewById(R.id.TextViewCourseDay);
        }
    }
}
