package com.example.drupp_driver.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.drupp_driver.R;
import com.example.drupp_driver.ui.base.MainBaseFragment;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {



    // not in use
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.sign_container,new SignInFragment(), SignInFragment.class.getSimpleName())
                .commit();

    }

    public static class SignInFragment extends MainBaseFragment{
        private Button btnRecoverPassword;
        private TextView tvForgotPassword,tvSignUp;

        @Override
        protected void initViewsForFragment(View view) {
            SpannableString spannableString=new SpannableString("New Driver? Sign Up Here");
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorLightBlack)),12,spannableString.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new UnderlineSpan(),12,spannableString.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            tvSignUp.setText(spannableString);
            btnRecoverPassword.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(),StartUpActivity.class));



            });
            tvForgotPassword.setOnClickListener(v -> {
                        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack(SignInFragment.class.getSimpleName(),0);
                        fragmentManager .beginTransaction()
                                .replace(R.id.sign_container,new ForgotPasswordFragment(),ForgotPasswordFragment.class.getSimpleName())
                                .addToBackStack(SignInFragment.class.getSimpleName())
                                .commit();
            });
            tvSignUp.setOnClickListener(v -> {
                startActivity(new Intent(getActivity(),StartUpActivity.class));
            });

        }

        @Override
        protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
            View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
            ButterKnife.bind(this, view);
            btnRecoverPassword = view.findViewById(R.id.button_sign_in);

            tvForgotPassword = view.findViewById(R.id.tvForgotPassword);
            tvSignUp = view.findViewById(R.id.tv_sign_up);


            return view;
        }
    }


    public static class ForgotPasswordFragment extends MainBaseFragment{
        private Button btnRecoverPassword;
        @Override
        protected void initViewsForFragment(View view) {

        }

        @Override
        protected View inflateFragmentView(LayoutInflater inflater, ViewGroup container) {
            View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
            ButterKnife.bind(this, view);
            btnRecoverPassword = view.findViewById(R.id.button_recover_password);


            return view;
        }


    }

}

