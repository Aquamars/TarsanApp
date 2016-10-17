package ccma.itri.org.com.tw.tarsanapp;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroViewPager;

import ccma.itri.org.com.tw.tarsanapp.FragmentSlide.FragmentCheck;
import ccma.itri.org.com.tw.tarsanapp.FragmentSlide.FragmentSlide;


/**
 * Created by A40503 on 2016/10/13.
 */

public final class DefaultGuide extends AppIntro {
    private boolean enabled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(FragmentSlide.newInstance(R.layout.guide_one));
        addSlide(FragmentSlide.newInstance(R.layout.guide_two));
        addSlide(FragmentSlide.newInstance(R.layout.guide_three));
        addSlide(new FragmentCheck());
        setBarColor(Color.parseColor("#3F51B5"));
        enabled = CheckService.getInstance().checkEnabled();
        showSkipButton(enabled);

//        addSlide(new InputDemoSlede);
    }

    public void showdone(View v){
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        TextView hint = (TextView)findViewById(R.id.hint);
        hint.setText("!!");
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
        Toast.makeText(getApplicationContext(), "SKIP", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        loadMainActivity();
    }

    void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onResume() {
        Log.d("guide","onResume");
        enabled = CheckService.getInstance().checkEnabled();
        showSkipButton(enabled);
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("guide","onPause");
        enabled = CheckService.getInstance().checkEnabled();
        showSkipButton(enabled);
        super.onPause();
    }

    public void getStarted(View v) {
        loadMainActivity();
    }
}
