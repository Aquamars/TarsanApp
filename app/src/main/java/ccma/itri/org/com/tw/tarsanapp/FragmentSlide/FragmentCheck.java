package ccma.itri.org.com.tw.tarsanapp.FragmentSlide;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.ISlidePolicy;

import ccma.itri.org.com.tw.tarsanapp.CheckService;
import ccma.itri.org.com.tw.tarsanapp.R;

/**
 * Created by A40503 on 2016/10/14.
 */

public class FragmentCheck extends Fragment implements ISlidePolicy, ISlideBackgroundColorHolder{
    private TextView hint;
    private CheckBox checkBox;
    private boolean checkService;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guide_setting, container, false);
        hint = (TextView)view.findViewById(R.id.hint);
        checkBox =(CheckBox)view.findViewById(R.id.check_enabled);
        CheckBoxController();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public int getDefaultBackgroundColor() {
        return 0;
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {

    }

    @Override
    public boolean isPolicyRespected() {
        return checkBox == null || checkBox.isChecked();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Toast.makeText(getContext(), "Please Enable Accessibility!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        Log.d("FragmentCheck","onPause");
        CheckBoxController();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("FragmentCheck","onResume");
        CheckBoxController();
        super.onResume();
    }

    private void CheckBoxController(){
        checkService = CheckService.getInstance().checkEnabled();
        checkBox.setChecked(checkService);
    }
}
