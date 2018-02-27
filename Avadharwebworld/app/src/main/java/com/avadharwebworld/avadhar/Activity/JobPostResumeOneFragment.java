package com.avadharwebworld.avadhar.Activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avadharwebworld.avadhar.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class JobPostResumeOneFragment extends Fragment {

    public JobPostResumeOneFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_post_resume_one, container, false);
    }
}
