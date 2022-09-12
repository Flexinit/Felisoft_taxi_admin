package com.example.drupp_driver.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drupp_driver.R;
import com.example.drupp_driver.helpers.UIHelper;
import com.example.drupp_driver.ui.PrivacyPolicyActivity;
import com.example.drupp_driver.ui.TermsConditionActivity;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsFragment extends MainBaseFragment {

    @Override
    protected void initViewsForFragment(View view) {

    }

    @Override
    protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragments_settings_new, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.tv_privacy_policy)
    public void showPrivacyPolicy() {
        UIHelper.getInstance().switchActivity(getmActivity(), PrivacyPolicyActivity.class, UIHelper.ActivityAnimations.LEFT_TO_RIGHT, null, null, false);
    }

    @OnClick(R.id.tv_terms_and_condition)
    public void showTermsAndCondition() {
        UIHelper.getInstance().switchActivity(getmActivity(), TermsConditionActivity.class, UIHelper.ActivityAnimations.LEFT_TO_RIGHT, null, null, false);
    }

    @OnClick(R.id.btn_change_password)
    public void changePassword() {
        UIHelper.getInstance().switchFragment((AppCompatActivity) getmActivity(), R.id.fl_settigns_container, new ChangePasswordFragment(), ChangePasswordFragment.class.getSimpleName(), true);
    }




}
