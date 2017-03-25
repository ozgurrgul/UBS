package dk.ozgur.ubs.controller;

import com.bluelinelabs.conductor.Controller;

import dk.ozgur.ubs.activity.BaseActivity;
import dk.ozgur.ubs.activity.MainActivity;

/**
 * Created by ozgur on 3/24/17.
 */

public abstract class BaseController extends Controller {

    public void toast(int resId) {
        toast(getActivity().getResources().getString(resId));
    }

    public void toast(String str) {
        BaseActivity activity = (MainActivity) getActivity();
        activity.toast(str);
    }

    public void showProgress() {
        MainActivity activity = (MainActivity) getActivity();
        activity.showProgress();
    }

    public void hideProgress() {
        MainActivity activity = (MainActivity) getActivity();
        activity.hideProgress();
    }

    public abstract String getControllerName();

}
