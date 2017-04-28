package com.autel.sdksample.camera.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.camera.base.PhotoFormat;
import com.autel.common.camera.media.CameraAntiFlicker;
import com.autel.common.camera.media.CameraAspectRatio;
import com.autel.common.camera.media.CameraAutoExposureLockState;
import com.autel.common.camera.media.CameraColorStyle;
import com.autel.common.camera.media.CameraExposureCompensation;
import com.autel.common.camera.media.CameraExposureMode;
import com.autel.common.camera.media.CameraISO;
import com.autel.common.camera.media.CameraShutterSpeed;
import com.autel.common.camera.media.CameraSpotMeteringArea;
import com.autel.common.camera.media.CameraWhiteBalance;
import com.autel.common.camera.media.CameraWhiteBalanceType;
import com.autel.common.camera.media.PhotoAEBCount;
import com.autel.common.camera.media.PhotoBurstCount;
import com.autel.common.camera.media.PhotoStyle;
import com.autel.common.camera.media.PhotoStyleType;
import com.autel.common.camera.media.PhotoSum;
import com.autel.common.camera.media.PhotoTimelapseInterval;
import com.autel.common.camera.media.VideoFormat;
import com.autel.common.camera.media.VideoResolutionAndFps;
import com.autel.common.camera.media.VideoStandard;
import com.autel.common.camera.media.VideoSum;
import com.autel.common.error.AutelError;
import com.autel.sdk.camera.AutelR12;
import com.autel.sdksample.R;
import com.autel.sdksample.camera.CameraActivity;
import com.autel.sdksample.camera.fragment.adapter.AntiFlickerAdapter;
import com.autel.sdksample.camera.fragment.adapter.AspectRatioAdapter;
import com.autel.sdksample.camera.fragment.adapter.AutoExposureLockStateAdapter;
import com.autel.sdksample.camera.fragment.adapter.ColorStyleAdapter;
import com.autel.sdksample.camera.fragment.adapter.ExposureValueAdapter;
import com.autel.sdksample.camera.fragment.adapter.ISOValueAdapter;
import com.autel.sdksample.camera.fragment.adapter.PhotoAEBCountAdapter;
import com.autel.sdksample.camera.fragment.adapter.PhotoBurstAdapter;
import com.autel.sdksample.camera.fragment.adapter.PhotoFormatAdapter;
import com.autel.sdksample.camera.fragment.adapter.PhotoStyleAdapter;
import com.autel.sdksample.camera.fragment.adapter.PhotoTimelapseIntervalAdapter;
import com.autel.sdksample.camera.fragment.adapter.ShutterValueAdapter;
import com.autel.sdksample.camera.fragment.adapter.VideoFormatAdapter;
import com.autel.sdksample.camera.fragment.adapter.VideoStandardAdapter;
import com.autel.sdksample.camera.fragment.adapter.WhiteBalanceTypeAdapter;

/**
 * Created by A16343 on 2017/2/22.
 */

public class CameraR12Fragment extends CameraBaseFragment {
    AutelR12 autelR12;

    Button setCameraColor;
    Spinner colorStyle;

    Button setExposureMode;
    Spinner exposureModeList;

    EditText spotMeteringAreaX;
    EditText spotMeteringAreaY;

    EditText photoCustomStyleContrast;
    EditText photoCustomStyleSaturation;
    EditText photoCustomStyleSharpness;

    Spinner exposureValueList;

    CameraColorStyle cameraColorStyle = CameraColorStyle.None;
    CameraExposureMode cameraExposureMode = CameraExposureMode.Auto;
    CameraExposureCompensation cameraExposureCompensation = CameraExposureCompensation.NEGATIVE_0p3;
    CameraISO cameraISO = CameraISO.ISO_100;
    CameraShutterSpeed cameraShutterSpeed = CameraShutterSpeed.ShutterSpeed_1;
    CameraWhiteBalanceType cameraWhiteBalanceType = CameraWhiteBalanceType.AUTO;
    CameraAntiFlicker cameraAntiFlicker = CameraAntiFlicker.AUTO;
    CameraAutoExposureLockState cameraAutoExposureLockState = CameraAutoExposureLockState.LOCK;
    PhotoStyleType photoStyleType = PhotoStyleType.Standard;
    PhotoBurstCount photoBurstCount = PhotoBurstCount.BURST_3;
    PhotoTimelapseInterval photoTimelapseInterval = PhotoTimelapseInterval.SECOND_5;
    PhotoAEBCount photoAEBCount = PhotoAEBCount.CAPTURE_3;
    VideoFormat videoFormat = VideoFormat.MOV;
    VideoStandard videoStandard = VideoStandard.NTSC;
    PhotoFormat photoFormat = PhotoFormat.JPEG;
    CameraAspectRatio aspectRatio = CameraAspectRatio.Aspect_16_9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_camera_r12, null);
        autelR12 = (AutelR12) ((CameraActivity) getActivity()).getCamera();
        initView(view);
        initClick(view);
        initR12Click(view);
        return view;
    }


    private void initR12Click(View view) {
        view.findViewById(R.id.setSpotMeteringArea).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NoX = spotMeteringAreaX.getText().toString();
                String NoY = spotMeteringAreaY.getText().toString();
                autelR12.setSpotMeteringArea(isEmpty(NoX) ? 1 : Integer.valueOf(NoX), isEmpty(NoY) ? 1 : Integer.valueOf(NoY), new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        logOut("setSpotMeteringArea  onSuccess  ");
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("setSpotMeteringArea  description  " + autelError.getDescription());
                    }
                });
            }
        });
        view.findViewById(R.id.getSpotMeteringArea).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autelR12.getSpotMeteringArea(new CallbackWithOneParam<CameraSpotMeteringArea>() {
                            @Override
                            public void onFailure(AutelError error) {
                                logOut("getSpotMeteringArea  description  " + error.getDescription());
                            }

                            @Override
                            public void onSuccess(CameraSpotMeteringArea data) {
                                logOut("getSpotMeteringArea X " + data.X + "  Y " + data.Y);
                            }
                        });
                    }
                });

        view.findViewById(R.id.setExposure).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autelR12.setExposure(cameraExposureCompensation, new CallbackWithNoParam() {
                            @Override
                            public void onSuccess() {
                                logOut("setExposure  onSuccess  ");
                            }

                            @Override
                            public void onFailure(AutelError autelError) {
                                logOut("setExposure  description  " + autelError.getDescription());
                            }
                        });
                    }
                });


        view.findViewById(R.id.getExposure).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autelR12.getExposure(new CallbackWithOneParam<CameraExposureCompensation>() {
                            @Override
                            public void onSuccess(CameraExposureCompensation cameraExposureCompensation) {
                                logOut("getExposure  onSuccess  " + cameraExposureCompensation);
                            }

                            @Override
                            public void onFailure(AutelError autelError) {
                                logOut("getExposure  description  " + autelError.getDescription());
                            }
                        });
                    }
                });


        view.findViewById(R.id.setISO).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autelR12.setISO(cameraISO, new CallbackWithNoParam() {
                            @Override
                            public void onSuccess() {
                                logOut("setISO  onSuccess  ");
                            }

                            @Override
                            public void onFailure(AutelError autelError) {
                                logOut("setISO  description  " + autelError.getDescription());
                            }
                        });
                    }
                });

        view.findViewById(R.id.getISO).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autelR12.getISO(new CallbackWithOneParam<CameraISO>() {
                            @Override
                            public void onFailure(AutelError error) {
                                logOut("getISO  description  " + error.getDescription());
                            }

                            @Override
                            public void onSuccess(CameraISO data) {
                                logOut("getISO " + data);
                            }
                        });
                    }
                });


        view.findViewById(R.id.setShutter).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autelR12.setShutter(cameraShutterSpeed, new CallbackWithNoParam() {
                            @Override
                            public void onFailure(AutelError error) {
                                logOut("setShutter  description  " + error.getDescription());
                            }

                            @Override
                            public void onSuccess() {
                                logOut("setShutter state onSuccess");
                            }
                        });
                    }
                });

        view.findViewById(R.id.getShutter).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        autelR12.getShutter(new CallbackWithOneParam<CameraShutterSpeed>() {
                            @Override
                            public void onFailure(AutelError error) {
                                logOut("getShutter  description  " + error.getDescription());
                            }

                            @Override
                            public void onSuccess(CameraShutterSpeed data) {
                                logOut("getShutter " + data);
                            }
                        });
                    }
                });

        view.findViewById(R.id.setColorStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setColorStyle(cameraColorStyle, new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        logOut("setColorStyle  onSuccess  ");
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("setAutoExposureLockState  description  " + autelError.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.getColorStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getColorStyle(new CallbackWithOneParam<CameraColorStyle>() {
                    @Override
                    public void onSuccess(CameraColorStyle cameraColorStyle) {
                        logOut("getColorStyle  onSuccess  " + cameraColorStyle);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("getColorStyle  description  " + autelError.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.setWhiteBalance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraWhiteBalance cameraWhiteBalance = new CameraWhiteBalance();
                cameraWhiteBalance.type = cameraWhiteBalanceType;

                autelR12.setWhiteBalance(cameraWhiteBalance, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setWhiteBalance  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setWhiteBalance state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.getWhiteBalance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getWhiteBalance(new CallbackWithOneParam<CameraWhiteBalance>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getWhiteBalance  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(CameraWhiteBalance data) {
                        logOut("getWhiteBalance " + data.type + "  colorTemperature  " + data.colorTemperature);
                    }
                });
            }
        });

        ((Switch) view.findViewById(R.id.set3DNoiseReductionEnable)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autelR12.set3DNoiseReductionEnable(isChecked, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("set3DNoiseReductionEnable  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("set3DNoiseReductionEnable state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.is3DNoiseReductionEnable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.is3DDenoiseEnable(new CallbackWithOneParam<Boolean>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("is3DNoiseReductionEnable  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(Boolean data) {
                        logOut("is3DNoiseReductionEnable " + data);
                    }
                });
            }
        });


        view.findViewById(R.id.setAntiFlicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setAntiFlicker(cameraAntiFlicker, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setAntiFlicker  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setAntiFlicker state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.getAntiFlicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getAntiFlicker(new CallbackWithOneParam<CameraAntiFlicker>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getAntiFlicker  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(CameraAntiFlicker data) {
                        logOut("getAntiFlicker " + data);
                    }
                });
            }
        });

        view.findViewById(R.id.setAutoExposureLockState).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setAutoExposureLockState(cameraAutoExposureLockState, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setAutoExposureLockState  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setAutoExposureLockState  onSuccess  ");
                    }
                });
            }
        });

        view.findViewById(R.id.getAutoExposureLockState).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getAutoExposureLockState(new CallbackWithOneParam<CameraAutoExposureLockState>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getAutoExposureLockState  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(CameraAutoExposureLockState data) {
                        logOut("getAutoExposureLockState  " + data);
                    }
                });
            }
        });

        view.findViewById(R.id.isHistogramStatusEnable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.isHistogramStatusEnable(new CallbackWithOneParam<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        logOut("isHistogramStatusEnable " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("isHistogramStatusEnable " + error.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.setExposureMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setExposureMode(cameraExposureMode, new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        logOut("setExposureMode  onSuccess  ");
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("setAutoExposureLockState  description  " + autelError.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.getExposureMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getExposureMode(new CallbackWithOneParam<CameraExposureMode>() {
                    @Override
                    public void onSuccess(CameraExposureMode cameraExposureMode) {
                        logOut("getExposureMode  onSuccess  " + cameraExposureMode);
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("getExposureMode  description  " + autelError.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.setPhotoStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoStyleType == PhotoStyleType.Custom) {
                    photoCustomStyleContrast.setVisibility(View.VISIBLE);
                    photoCustomStyleSaturation.setVisibility(View.VISIBLE);
                    photoCustomStyleSharpness.setVisibility(View.VISIBLE);
                    String contrastValue = photoCustomStyleContrast.getText().toString();
                    String saturationValue = photoCustomStyleSaturation.getText().toString();
                    String sharpnessValue = photoCustomStyleSharpness.getText().toString();

                    autelR12.setPhotoStyle(isEmpty(contrastValue) ? 1 : Integer.valueOf(contrastValue),
                            isEmpty(saturationValue) ? 2 : Integer.valueOf(saturationValue),
                            isEmpty(sharpnessValue) ? 3 : Integer.valueOf(sharpnessValue), new CallbackWithNoParam() {
                                @Override
                                public void onFailure(AutelError error) {
                                    logOut("setPhotoStyle  description  " + error.getDescription());
                                }

                                @Override
                                public void onSuccess() {
                                    logOut("setPhotoStyle state onSuccess");
                                }
                            });
                } else {
                    autelR12.setPhotoStyle(photoStyleType, new CallbackWithNoParam() {
                        @Override
                        public void onFailure(AutelError error) {
                            logOut("setPhotoStyle  description  " + error.getDescription());
                        }

                        @Override
                        public void onSuccess() {
                            logOut("setPhotoStyle state onSuccess");
                        }
                    });
                }
            }
        });

        view.findViewById(R.id.getPhotoStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getPhotoStyle(new CallbackWithOneParam<PhotoStyle>() {
                    @Override
                    public void onSuccess(PhotoStyle data) {
                        logOut("getPhotoStyle " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getPhotoStyle " + error.getDescription());
                    }
                });
            }
        });

        ((Switch) view.findViewById(R.id.setVideoSubtitleEnable)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autelR12.setVideoSubtitleEnable(isChecked, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setVideoSubtitleEnable  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setVideoSubtitleEnable onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.isSubtitleEnable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.isSubtitleSwitchEnable(new CallbackWithOneParam<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        logOut("isSubtitleEnable " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("isSubtitleEnable " + error.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.setPhotoBurstCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setPhotoBurstCount(photoBurstCount, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setPhotoBurstCount  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setPhotoBurstCount state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.getPhotoBurstCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getBurstCount(new CallbackWithOneParam<PhotoBurstCount>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getPhotoBurstCount  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(PhotoBurstCount data) {
                        logOut("getPhotoBurstCount " + data);
                    }
                });
            }
        });

        view.findViewById(R.id.setPhotoTimelapseInterval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setPhotoTimelapseInterval(photoTimelapseInterval, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setPhotoTimelapseInterval  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setPhotoTimelapseInterval state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.getPhotoTimelapseInterval).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getPhotoTimelapseInterval(new CallbackWithOneParam<PhotoTimelapseInterval>() {
                    @Override
                    public void onSuccess(PhotoTimelapseInterval data) {
                        logOut("getPhotoTimelapseInterval " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getPhotoTimelapseInterval " + error.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.setPhotoAEBCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setPhotoAEBCount(photoAEBCount, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setPhotoAEBCount  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setPhotoAEBCount state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.getPhotoAEBCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getPhotoAEBCount(new CallbackWithOneParam<PhotoAEBCount>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getPhotoAEBCount  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(PhotoAEBCount data) {
                        logOut("getPhotoAEBCount " + data);
                    }
                });
            }
        });

        view.findViewById(R.id.getVideoSum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getVideoSum(new CallbackWithOneParam<VideoSum>() {
                    @Override
                    public void onSuccess(VideoSum data) {
                        logOut("getVideoSum " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getVideoSum " + error.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.getPhotoSum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getPhotoSum(new CallbackWithOneParam<PhotoSum>() {
                    @Override
                    public void onSuccess(PhotoSum data) {
                        logOut("getPhotoSum " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getPhotoSum " + error.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.getCurrentRecordTimeSeconds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getCurrentRecordTimeSeconds(new CallbackWithOneParam<Integer>() {
                    @Override
                    public void onSuccess(Integer data) {
                        logOut("getCurrentRecordTimeSeconds " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getCurrentRecordTimeSeconds " + error.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.setVideoFormat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setVideoFormat(videoFormat, new CallbackWithNoParam() {
                    @Override
                    public void onSuccess() {
                        logOut("setVideoFormat onSuccess");
                    }

                    @Override
                    public void onFailure(AutelError autelError) {
                        logOut("setVideoFormat " + autelError.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.getVideoFormat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getVideoFormat(new CallbackWithOneParam<VideoFormat>() {
                    @Override
                    public void onSuccess(VideoFormat data) {
                        logOut("getVideoFormat " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getVideoFormat " + error.getDescription());
                    }
                });
            }
        });

        view.findViewById(R.id.setVideoStandard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setVideoStandard(videoStandard, new CallbackWithNoParam() {

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setVideoStandard  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setVideoStandard state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.getVideoStandard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getVideoStandard(new CallbackWithOneParam<VideoStandard>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getVideoStandard  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(VideoStandard data) {
                        logOut("getVideoStandard " + data);
                    }
                });
            }
        });

        view.findViewById(R.id.setPhotoFormat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setPhotoFormat(photoFormat, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setPhotoFormat  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setPhotoFormat state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.getPhotoFormat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getPhotoFormat(new CallbackWithOneParam<PhotoFormat>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getPhotoFormat  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(PhotoFormat data) {
                        logOut("getPhotoFormat " + data);
                    }
                });
            }
        });

        view.findViewById(R.id.setAspectRatio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.setAspectRatio(aspectRatio, new CallbackWithNoParam() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("setAspectRatio  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess() {
                        logOut("setAspectRatio state onSuccess");
                    }
                });
            }
        });

        view.findViewById(R.id.getAspectRatio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getAspectRatio(new CallbackWithOneParam<CameraAspectRatio>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getAspectRatio  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(CameraAspectRatio data) {
                        logOut("getAspectRatio " + data);
                    }
                });
            }
        });

        view.findViewById(R.id.getVideoResolutionAndFrameRate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autelR12.getVideoResolutionAndFps(new CallbackWithOneParam<VideoResolutionAndFps>() {
                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getVideoResolutionAndFrameRate  description  " + error.getDescription());
                    }

                    @Override
                    public void onSuccess(VideoResolutionAndFps data) {
                        logOut("getVideoResolutionAndFrameRate " + data);
                    }
                });
            }
        });
    }

    private void initView(View view) {
        Spinner aspectRatioList = (Spinner) view.findViewById(R.id.aspectRatioList);
        aspectRatioList.setAdapter(new AspectRatioAdapter(getContext()));
        aspectRatioList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                aspectRatio = (CameraAspectRatio) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner photoFormatList = (Spinner) view.findViewById(R.id.photoFormatList);
        photoFormatList.setAdapter(new PhotoFormatAdapter(getContext()));
        photoFormatList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                photoFormat = (PhotoFormat) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner videoStandardList = (Spinner) view.findViewById(R.id.videoStandardList);
        videoStandardList.setAdapter(new VideoStandardAdapter(getContext()));
        videoStandardList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                videoStandard = (VideoStandard) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner videoFormatList = (Spinner) view.findViewById(R.id.videoFormatList);
        videoFormatList.setAdapter(new VideoFormatAdapter(getContext()));
        videoFormatList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                videoFormat = (VideoFormat) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner photoAEBCountList = (Spinner) view.findViewById(R.id.photoAEBCountList);
        photoAEBCountList.setAdapter(new PhotoAEBCountAdapter(getContext()));
        photoAEBCountList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                photoAEBCount = (PhotoAEBCount) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner photoTimelapseIntervalList = (Spinner) view.findViewById(R.id.photoTimelapseIntervalList);
        photoTimelapseIntervalList.setAdapter(new PhotoTimelapseIntervalAdapter(getContext()));
        photoTimelapseIntervalList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                photoTimelapseInterval = (PhotoTimelapseInterval) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner photoBurstCountList = (Spinner) view.findViewById(R.id.photoBurstCountList);
        photoBurstCountList.setAdapter(new PhotoBurstAdapter(getContext()));
        photoBurstCountList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                photoBurstCount = (PhotoBurstCount) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        photoCustomStyleContrast = (EditText) view.findViewById(R.id.photoCustomStyleContrast);
        photoCustomStyleSaturation = (EditText) view.findViewById(R.id.photoCustomStyleSaturation);
        photoCustomStyleSharpness = (EditText) view.findViewById(R.id.photoCustomStyleSharpness);

        Spinner photoStyleList = (Spinner) view.findViewById(R.id.photoStyleList);
        photoStyleList.setAdapter(new PhotoStyleAdapter(getContext()));
        photoStyleList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                photoStyleType = (PhotoStyleType) parent.getAdapter().getItem(position);
                if (photoStyleType == PhotoStyleType.Custom) {
                    photoCustomStyleContrast.setVisibility(View.VISIBLE);
                    photoCustomStyleSaturation.setVisibility(View.VISIBLE);
                    photoCustomStyleSharpness.setVisibility(View.VISIBLE);
                } else {
                    photoCustomStyleContrast.setVisibility(View.GONE);
                    photoCustomStyleSaturation.setVisibility(View.GONE);
                    photoCustomStyleSharpness.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        exposureValueList = (Spinner) view.findViewById(R.id.exposureValueList);
        exposureValueList.setAdapter(new ExposureValueAdapter(getContext()));
        exposureValueList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cameraExposureCompensation = (CameraExposureCompensation) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner ISOList = (Spinner) view.findViewById(R.id.ISOList);
        ISOList.setAdapter(new ISOValueAdapter(getContext()));
        ISOList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cameraISO = (CameraISO) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        colorStyle = (Spinner) view.findViewById(R.id.colorStyle);
        colorStyle.setAdapter(new ColorStyleAdapter(getContext()));
        colorStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cameraColorStyle = (CameraColorStyle) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner whiteBalanceTypeList = (Spinner) view.findViewById(R.id.whiteBalanceTypeList);
        whiteBalanceTypeList.setAdapter(new WhiteBalanceTypeAdapter(getContext()));
        whiteBalanceTypeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cameraWhiteBalanceType = (CameraWhiteBalanceType) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner antiFlickerList = (Spinner) view.findViewById(R.id.antiFlickerList);
        antiFlickerList.setAdapter(new AntiFlickerAdapter(getContext()));
        antiFlickerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cameraAntiFlicker = (CameraAntiFlicker) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner autoExposureLockStateList = (Spinner) view.findViewById(R.id.autoExposureLockStateList);
        autoExposureLockStateList.setAdapter(new AutoExposureLockStateAdapter(getContext()));
        autoExposureLockStateList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cameraAutoExposureLockState = (CameraAutoExposureLockState) parent.getAdapter().getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        exposureModeList = (Spinner) view.findViewById(R.id.exposureModeList);
        exposureModeList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        cameraExposureMode = CameraExposureMode.Auto;
                        break;
                    case 1:
                        cameraExposureMode = CameraExposureMode.Manual;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spotMeteringAreaX = (EditText) view.findViewById(R.id.spotMeteringAreaX);
        spotMeteringAreaY = (EditText) view.findViewById(R.id.spotMeteringAreaY);

    }
}
