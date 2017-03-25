package dk.ozgur.ubs;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;

import dk.ozgur.ubs.model.UserGradeResponse;
import dk.ozgur.ubs.model.UserLoginRequest;
import dk.ozgur.ubs.model.UserLoginResponse;
import dk.ozgur.ubs.model.UserDataRequest;
import dk.ozgur.ubs.model.UserWeeklyScheduleResponse;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit.http.POST;


/**
 * Created by ozgur on 10/8/15 at 11:40 PM.
 */
public class Api {

    public interface Service {

        @FormUrlEncoded
        @POST("GetLogin")
        Call<UserLoginResponse> getLogin(@Body UserLoginRequest _);

        @FormUrlEncoded
        @POST("GetWeeklySchedule")
        Call<UserWeeklyScheduleResponse> getWeeklySchedule(@Body UserDataRequest _);

        @FormUrlEncoded
        @POST("GetGrades")
        Call<UserGradeResponse> getGrades(@Body UserDataRequest _);

        @FormUrlEncoded
        @POST("GetGradesHistory")
        Call<UserGradeResponse> getGradesHistory(@Body UserDataRequest _);
    }

    private static final String API_URL = "http://ubs.cbu.edu.tr/Mobil/Rest/UBSIntegration/";

    public static Service service = null;

    public static void init(Context c) {

        /**/
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(30, TimeUnit.SECONDS);

        initService(okHttpClient);
    }

    private static void initService(OkHttpClient okHttpClient) {

        /**/
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        /**/
        service = retrofit.create(Service.class);
    }

}
