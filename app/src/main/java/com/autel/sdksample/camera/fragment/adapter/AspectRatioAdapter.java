package com.autel.sdksample.camera.fragment.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.autel.common.camera.CameraProduct;
import com.autel.common.camera.media.CameraAspectRatio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AspectRatioAdapter extends BaseAdapter {
    private List<CameraAspectRatio> cameraAspectRatios = new ArrayList<>();
    private Context mContext;
    private CameraProduct cameraProduct;
    public AspectRatioAdapter(Context context, CameraProduct cameraProduct) {
        mContext = context;
        this.cameraProduct = cameraProduct;
        cameraAspectRatios.addAll(Arrays.asList(cameraProduct.supportedAspectRatio()));
    }

    @Override
    public int getCount() {
        return null == cameraAspectRatios ? 0 : cameraAspectRatios.size();
    }

    @Override
    public Object getItem(int position) {
        return cameraAspectRatios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (null == convertView) {
            textView = new TextView(mContext);
            convertView = textView;
        }else{
            textView = (TextView)convertView;
        }

        textView.setText(cameraAspectRatios.get(position).toString());

        return convertView;
    }
}