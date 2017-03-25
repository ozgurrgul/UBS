package dk.ozgur.ubs.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import dk.ozgur.ubs.R;
import dk.ozgur.ubs.activity.LoginActivity;
import dk.ozgur.ubs.User;

/**
 * Created by ozgur on 3/24/17.
 */

public class HomeController extends BaseController {

    private ImageView mImageViewImage;
    private TextView mTextViewStudentName;
    private View mViewRateApp;
    private View mViewLogout;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.controller_home, null);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        mImageViewImage = (ImageView) view.findViewById(R.id.ImageViewImage);
        mTextViewStudentName = (TextView) view.findViewById(R.id.TextViewStudentName);

        User user = new User(getActivity());
        mTextViewStudentName.setText(user.getStudentName());

        // image
        if(!TextUtils.isEmpty(user.getImage())) {
            byte[] decode = Base64.decode(user.getImage(), 0);
            mImageViewImage.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
        }

        mViewRateApp = view.findViewById(R.id.ViewRateApp);
        mViewLogout = view.findViewById(R.id.ViewLogout);

        mViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog();
            }
        });

        mViewRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=dk.ozgur.ubs")));
            }
        });
    }

    private void logoutDialog() {

        new AlertDialog.Builder(getActivity())
                .setTitle("Çıkış?")
                .setPositiveButton("Çık", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new User(getActivity()).logout();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                }).show();

    }

    @Override
    public String getControllerName() {
        return "home";
    }
}
