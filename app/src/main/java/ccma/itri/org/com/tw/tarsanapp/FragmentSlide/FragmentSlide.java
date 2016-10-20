package ccma.itri.org.com.tw.tarsanapp.FragmentSlide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by A40503 on 2016/10/13.
 */

public class FragmentSlide extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private int layoutResId;

    public static FragmentSlide newInstance(int layoutResId){
        FragmentSlide fragmentSlide = new FragmentSlide();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID,layoutResId);
        fragmentSlide.setArguments(args);

        return fragmentSlide;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID)){
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
            Log.d("layoutResId", String.valueOf(layoutResId));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutResId, container, false);
    }
}
