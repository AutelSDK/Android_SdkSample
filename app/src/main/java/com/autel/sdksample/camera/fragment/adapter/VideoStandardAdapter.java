package com.autel.sdksample.camera.fragment.adapter;

import android.content.Context;

import com.autel.common.camera.media.VideoStandard;
import com.autel.sdksample.adapter.SelectorAdapter;


public class VideoStandardAdapter extends SelectorAdapter<VideoStandard> {

    public VideoStandardAdapter(Context context) {
        super(context);
        elementList.add(VideoStandard.NTSC);
        elementList.add(VideoStandard.PAL);
    }

}
