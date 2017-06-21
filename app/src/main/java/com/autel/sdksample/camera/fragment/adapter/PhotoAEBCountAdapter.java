package com.autel.sdksample.camera.fragment.adapter;

import android.content.Context;

import com.autel.common.camera.media.PhotoAEBCount;
import com.autel.sdksample.adapter.SelectorAdapter;


public class PhotoAEBCountAdapter extends SelectorAdapter<PhotoAEBCount> {

    public PhotoAEBCountAdapter(Context context) {
        super(context);
        elementList.add(PhotoAEBCount.CAPTURE_3);
        elementList.add(PhotoAEBCount.CAPTURE_5);
    }
}
