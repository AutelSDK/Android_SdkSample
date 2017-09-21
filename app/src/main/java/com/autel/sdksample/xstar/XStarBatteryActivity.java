package com.autel.sdksample.xstar;

import android.os.Bundle;
import android.view.View;

import com.autel.common.CallbackWithOneParam;
import com.autel.common.error.AutelError;
import com.autel.sdk.battery.AutelBattery;
import com.autel.sdk.battery.XStarBattery;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.product.XStarAircraft;
import com.autel.sdksample.R;
import com.autel.sdksample.base.BatteryActivity;


public class XStarBatteryActivity extends BatteryActivity {
    private XStarBattery mXStarBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Battery");
    }

    @Override
    protected AutelBattery initController(BaseProduct product) {
        mXStarBattery = ((XStarAircraft) product).getBattery();
        return mXStarBattery;
    }

    @Override
    protected int getCustomViewResId() {
        return R.layout.activity_xstar_battery;
    }

    @Override
    protected void initUi() {
        super.initUi();
        findViewById(R.id.getCells).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getVoltageCells(new CallbackWithOneParam<int[]>() {
                    @Override
                    public void onSuccess(int[] data) {
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < data.length; i++) {
                            sb.append("index ");
                            sb.append(i);
                            sb.append(" = ");
                            sb.append(data[i]);
                            sb.append("   ");
                        }
                        logOut("getVoltageCells  " + sb.toString());
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getVoltageCells  error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getVoltage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getVoltage(new CallbackWithOneParam<Float>() {
                    @Override
                    public void onSuccess(Float data) {
                        logOut("getVoltage  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getVoltage  error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getCapacity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getCapacity(new CallbackWithOneParam<Float>() {
                    @Override
                    public void onSuccess(Float data) {
                        logOut("getCapacity  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getCapacity  error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getDesignCapacity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getDesignCapacity(new CallbackWithOneParam<Float>() {
                    @Override
                    public void onSuccess(Float data) {
                        logOut("getDesignCapacity  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getDesignCapacity  error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getVersion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getVersion(new CallbackWithOneParam<String>() {
                    @Override
                    public void onSuccess(String data) {
                        logOut("getVersion  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getVersion  error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getSerialNumber).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getSerialNumber(new CallbackWithOneParam<String>() {
                    @Override
                    public void onSuccess(String data) {
                        logOut("getSerialNumber  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getSerialNumber  error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getRemainingPercent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getRemainingPercent(new CallbackWithOneParam<Integer>() {
                    @Override
                    public void onSuccess(Integer data) {
                        logOut("getRemainingPercent  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getRemainingPercent  error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getFullChargeCapacity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getFullChargeCapacity(new CallbackWithOneParam<Integer>() {
                    @Override
                    public void onSuccess(Integer data) {
                        logOut("getFullChargeCapacity  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getFullChargeCapacity error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getDischargeCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getDischargeCount(new CallbackWithOneParam<Float>() {
                    @Override
                    public void onSuccess(Float data) {
                        logOut("getDischargeCount  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getDischargeCount error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getTemperature).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getTemperature(new CallbackWithOneParam<Float>() {
                    @Override
                    public void onSuccess(Float data) {
                        logOut("getTemperature  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getTemperature error : " + error.getDescription());
                    }
                });
            }
        });
        findViewById(R.id.getCurrent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mXStarBattery.getCurrent(new CallbackWithOneParam<Float>() {
                    @Override
                    public void onSuccess(Float data) {
                        logOut("getCurrent  " + data);
                    }

                    @Override
                    public void onFailure(AutelError error) {
                        logOut("getCurrent error : " + error.getDescription());
                    }
                });
            }
        });
    }

}