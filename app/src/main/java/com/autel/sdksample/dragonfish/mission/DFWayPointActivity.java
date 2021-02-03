package com.autel.sdksample.dragonfish.mission;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.autel.common.CallbackWithNoParam;
import com.autel.common.CallbackWithOneParam;
import com.autel.common.CallbackWithOneParamProgress;
import com.autel.common.battery.cruiser.CruiserBatteryInfo;
import com.autel.common.error.AutelError;
import com.autel.common.flycontroller.FlightErrorState;
import com.autel.common.flycontroller.cruiser.CruiserFlyControllerInfo;
import com.autel.common.mission.AutelMission;
import com.autel.common.mission.MissionType;
import com.autel.common.mission.RealTimeInfo;
import com.autel.common.mission.cruiser.CruiserWaypointFinishedAction;
import com.autel.common.mission.cruiser.CruiserWaypointMission;
import com.autel.common.product.AutelProductType;
import com.autel.common.remotecontroller.RemoteControllerInfo;
import com.autel.internal.sdk.mission.cruiser.CruiserWaypointRealTimeInfoImpl;
import com.autel.sdk.battery.CruiserBattery;
import com.autel.sdk.flycontroller.CruiserFlyController;
import com.autel.sdk.mission.MissionManager;
import com.autel.sdk.product.BaseProduct;
import com.autel.sdk.remotecontroller.AutelRemoteController;
import com.autel.sdksample.R;
import com.autel.sdksample.TestApplication;
import com.autel.util.log.AutelLog;


public class DFWayPointActivity extends AppCompatActivity implements View.OnClickListener {

    private CruiserWaypointMission autelMission;
    private CruiserFlyController mEvoFlyController;
    private CruiserBattery battery;
    private AutelRemoteController remoteController;
    private MissionManager missionManager;
    private float lowBatteryPercent = 15f;
    private boolean isBatteryOk = false; //当前电量是否合适
    private boolean isCompassOk = false; //当前指南针状态是否OK
    private boolean isImuOk = false; //当前IMU是否OK
    private boolean isGpsOk = false; //当前gps是否OK
    private boolean isImageTransOk = false; //当前图传信号是否OK
    private boolean isCanTakeOff = false; //是否能起飞

    enum FlyState {
        Prepare, Start, Pause, None
    }

    private FlyState flyState = FlyState.None;

    private int id = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTitle("WayPoint");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evo2_waypoint);

        BaseProduct product = ((TestApplication) getApplicationContext()).getCurrentProduct();
        if (null != product && product.getType() == AutelProductType.DRAGONFISH) {
            missionManager = product.getMissionManager();
            missionManager.setRealTimeInfoListener(new CallbackWithOneParam<RealTimeInfo>() {
                @Override
                public void onSuccess(RealTimeInfo realTimeInfo) {
                    CruiserWaypointRealTimeInfoImpl info = (CruiserWaypointRealTimeInfoImpl) realTimeInfo;
                    AutelLog.d("MissionRunning", "timeStamp:" + info.timeStamp + ",speed:" + info.speed + ",isArrived:" + info.isArrived +
                            ",isDirecting:" + info.isDirecting + ",waypointSequence:" + info.waypointSequence + ",actionSequence:" + info.actionSequence +
                            ",photoCount:" + info.photoCount + ",MissionExecuteState:" + info.executeState + ",missionID:" + info.missionID);
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });

            battery = (CruiserBattery) product.getBattery();
            battery.getLowBatteryNotifyThreshold(new CallbackWithOneParam<Float>() {
                @Override
                public void onSuccess(Float aFloat) {
                    lowBatteryPercent = aFloat;
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });
            battery.setBatteryStateListener(new CallbackWithOneParam<CruiserBatteryInfo>() {
                @Override
                public void onSuccess(CruiserBatteryInfo batteryState) {
                    AutelLog.d(" batteryState " + batteryState.getRemainingPercent());
                    isBatteryOk = batteryState.getRemainingPercent() > lowBatteryPercent;
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });

            mEvoFlyController = (CruiserFlyController) product.getFlyController();
            mEvoFlyController.setFlyControllerInfoListener(new CallbackWithOneParam<CruiserFlyControllerInfo>() {

                @Override
                public void onSuccess(CruiserFlyControllerInfo evoFlyControllerInfo) {
                    isCompassOk = evoFlyControllerInfo.getFlyControllerStatus().isCompassValid();
                    isCanTakeOff = evoFlyControllerInfo.getFlyControllerStatus().isCanTakeOff();

                    isImuOk = evoFlyControllerInfo.getFlyControllerStatus().isIMU0Valid() && evoFlyControllerInfo.getFlyControllerStatus().isIMU1Valid();

                    isGpsOk = evoFlyControllerInfo.getFlyControllerStatus().isGpsValid();
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });

            remoteController = product.getRemoteController();
            remoteController.setInfoDataListener(new CallbackWithOneParam<RemoteControllerInfo>() {
                @Override
                public void onSuccess(RemoteControllerInfo remoteControllerInfo) {
                    isImageTransOk = remoteControllerInfo.getDSPPercentage() >= 30;
                }

                @Override
                public void onFailure(AutelError autelError) {

                }
            });
        }
        AutelLog.d("init missionManager" + missionManager);
        initView();
        initData();
    }

    private void initView() {
        findViewById(R.id.prepare).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.resume).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.download).setOnClickListener(this);
    }

    private void initData() {
        autelMission = new CruiserWaypointMission();

        autelMission.missionId = id++; //任务id
        autelMission.missionType = MissionType.Waypoint; //任务类型(Waypoint(航点)、RECTANGLE(矩形)、POLYGON(多边形))
//        autelMission.totalFlyTime = 351; //总飞行时间(单位s)
//        autelMission.totalDistance = 897; //总飞行距离(单位m)
//        autelMission.VerticalFOV = 53.6f; //相机实时心跳数据读取
//        autelMission.HorizontalFOV = 68.0f; //相机实时心跳数据读取
//        autelMission.PhotoIntervalMin = 1020;
//        autelMission.altitudeType = 1;//0-相对高度，1-海拔高度
//        autelMission.MissionName = "Mission_1";
//        autelMission.GUID = UUID.randomUUID().toString().replace("-", "");
//
//        autelMission.missionAction = 1;//1-飞机与当前航点和下一个航点在一条直线上不减速飞行,0-正常飞行
//        List<CruiserWaypoint> wpList = new ArrayList<>();
//
//        //航点1（动作：飞越）
//        CruiserWaypoint cruiserWaypoint1 = new CruiserWaypoint(new AutelCoordinate3D(22.5966492303896, 113.99885752564695, 60)); //纬度、经度、飞行高度
//        cruiserWaypoint1.wSpeed = 5; //速度（单位m/s）
//        cruiserWaypoint1.poiIndex = -1; //关联的兴趣点id
//        cruiserWaypoint1.flyTime = 0; //飞行时间（单位s）
//        cruiserWaypoint1.hoverTime = 0; //飞越时0，悬停则传入具体时间（单位s）
//        cruiserWaypoint1.flyDistance = 0;//飞行距离（单位m）
//        cruiserWaypoint1.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION; //航向沿航线
//        cruiserWaypoint1.waypointType = WaypointType.STANDARD; //航点类型 飞越（STANDARD）或者悬停（HOVER）
//        //航点1为飞越，可添加0个或1个相机动作
//        List<WaypointAction> list1 = new ArrayList<>();
//        //添加相机动作
//        WaypointAction action1 = new WaypointAction();
//        action1.actionType = MissionActionType.START_RECORD; //开始录像
//        action1.parameters = new int[]{45, 50, 0, 0, 0, 0, 0, 20, 0, 0}; //设置录像参数(参数1：云台pitch角度 参数2：机头朝向角度 余下参数根据相机动作不同而不同)
//        list1.add(action1);
//        cruiserWaypoint1.actions = list1;
//        wpList.add(cruiserWaypoint1);
//
//        //航点2（动作：悬停）
//        CruiserWaypoint cruiserWaypoint2 = new CruiserWaypoint(new AutelCoordinate3D(22.59628621670881, 113.99741950976092, 60));
//        cruiserWaypoint2.wSpeed = 5;
////        cruiserWaypoint2.poiIndex = 1; //关联的兴趣点id，即关联了第二个兴趣点
//        cruiserWaypoint2.poiIndex = -1;
//        cruiserWaypoint2.flyTime = 40;
//        cruiserWaypoint2.hoverTime = 110;
//        cruiserWaypoint2.flyDistance = 153;
//        cruiserWaypoint2.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION;
//        cruiserWaypoint2.waypointType = WaypointType.HOVER;
//        //航点2为悬停点，可添加0个或多个相机动作
//        List<WaypointAction> list2 = new ArrayList<>();
//        //添加相机动作1 定实拍 (定时拍间隔为2s，定实拍时长为40s）
//        WaypointAction point2Action1 = new WaypointAction();
//        point2Action1.actionType = MissionActionType.START_TIME_LAPSE_SHOOT;
//        point2Action1.parameters = new int[]{-45, 90, 2, 40, 0, 0, 0, 20, 0, 0};
//        list2.add(point2Action1);
//        wpList.add(cruiserWaypoint2);
//        //添加相机动作2 开始录像 (录像时长为60s)
//        WaypointAction point2Action2 = new WaypointAction();
//        point2Action2.actionType = MissionActionType.START_RECORD;
//        point2Action2.parameters = new int[]{-23, 90, 0, 0, 0, 60, 0, 20, 0, 0};
//        list2.add(point2Action2);
//
//        cruiserWaypoint2.actions = list2;
////        wpList.add(cruiserWaypoint2);
//
//        //航点3（动作：飞跃）
//        CruiserWaypoint cruiserWaypoint3 = new CruiserWaypoint(new AutelCoordinate3D(22.59563928164338, 113.99866562877735, 60));
//        cruiserWaypoint3.wSpeed = 5;
//        cruiserWaypoint3.poiIndex = -1;
//        cruiserWaypoint3.flyTime = 190;
//        cruiserWaypoint3.hoverTime = 0;
//        cruiserWaypoint3.flyDistance = 300;
//        cruiserWaypoint3.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION;
//        cruiserWaypoint3.waypointType = WaypointType.STANDARD;
//        List<WaypointAction> list3 = new ArrayList<>();
//        WaypointAction point3Action1 = new WaypointAction();
//        point3Action1.actionType = MissionActionType.TAKE_PHOTO; //拍照
//        point3Action1.parameters = new int[]{0, 90, 0, 0, 0, 0, 0, 20, 0, 0};
//        list3.add(point3Action1);
//        cruiserWaypoint3.actions = list3;
//        wpList.add(cruiserWaypoint3);
//
//        //航点4（动作：飞跃）
//        CruiserWaypoint cruiserWaypoint4 = new CruiserWaypoint(new AutelCoordinate3D(22.595273074299133, 113.9969537182374, 60));
//        cruiserWaypoint4.wSpeed = 5;
//        cruiserWaypoint4.poiIndex = -1;
//        cruiserWaypoint4.flyTime = 234;
//        cruiserWaypoint4.hoverTime = 0;
//        cruiserWaypoint4.flyDistance = 481;
//        cruiserWaypoint4.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION;
//        cruiserWaypoint4.waypointType = WaypointType.STANDARD;
//        List<WaypointAction> list4 = new ArrayList<>();
//        WaypointAction point4Action1 = new WaypointAction();
//        point4Action1.actionType = MissionActionType.START_TIME_LAPSE_SHOOT; //定时拍照(2s间隔)
//        point4Action1.parameters = new int[]{0, 90, 2, 0, 0, 0, 0, 20, 0, 0};
//        list4.add(point4Action1);
//        cruiserWaypoint4.actions = list4;
////        wpList.add(cruiserWaypoint4);
//
//        //航点5（动作：飞跃）
//        CruiserWaypoint cruiserWaypoint5 = new CruiserWaypoint(new AutelCoordinate3D(22.595157667753398, 113.99928502161195, 60));
//        cruiserWaypoint5.wSpeed = 5;
//        cruiserWaypoint5.poiIndex = -1;
//        cruiserWaypoint5.flyTime = 295;
//        cruiserWaypoint5.hoverTime = 0;
//        cruiserWaypoint5.flyDistance = 722;
//        cruiserWaypoint5.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION;
//        cruiserWaypoint5.waypointType = WaypointType.STANDARD;
//        List<WaypointAction> list5 = new ArrayList<>();
//        WaypointAction point5Action1 = new WaypointAction();
//        point5Action1.actionType = MissionActionType.START_DISTANCE_SHOOT; //定距拍照(10m间隔)
//        point5Action1.parameters = new int[]{0, 90, 0, 0, 10, 0, 0, 20, 0, 0};
//        list5.add(point5Action1);
//        cruiserWaypoint5.actions = list5;
////        wpList.add(cruiserWaypoint5);
//
//        //航点6（动作：飞跃）
//        CruiserWaypoint cruiserWaypoint6 = new CruiserWaypoint(new AutelCoordinate3D(22.59583649616868, 22.59583649616868, 60));
//        cruiserWaypoint6.wSpeed = 5;
//        cruiserWaypoint6.poiIndex = -1;
//        cruiserWaypoint6.flyTime = 326;
//        cruiserWaypoint6.hoverTime = 0;
//        cruiserWaypoint6.flyDistance = 825;
//        cruiserWaypoint6.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION;
//        cruiserWaypoint6.waypointType = WaypointType.STANDARD;
//        List<WaypointAction> list6 = new ArrayList<>();
//        WaypointAction point6Action1 = new WaypointAction();
//        point6Action1.actionType = MissionActionType.START_RECORD; //开始录像
//        point6Action1.parameters = new int[]{0, 90, 0, 0, 0, 0, 0, 20, 0, 0};
//        list6.add(point6Action1);
//        cruiserWaypoint6.actions = list6;
////        wpList.add(cruiserWaypoint6);
//
//        //航点7（动作：飞跃）
//        CruiserWaypoint cruiserWaypoint7 = new CruiserWaypoint(new AutelCoordinate3D(22.59583649616868, 22.59583649616868, 60));
//        cruiserWaypoint7.wSpeed = 5;
//        cruiserWaypoint7.poiIndex = -1;
//        cruiserWaypoint7.flyTime = 351;
//        cruiserWaypoint7.hoverTime = 0;
//        cruiserWaypoint7.flyDistance = 897;
//        cruiserWaypoint7.headingMode = WaypointHeadingMode.CUSTOM_DIRECTION;
//        cruiserWaypoint7.waypointType = WaypointType.STANDARD;
//        List<WaypointAction> list7 = new ArrayList<>();
//        WaypointAction point7Action1 = new WaypointAction();
//        point7Action1.actionType = MissionActionType.STOP_RECORD; //结束录像
//        point7Action1.parameters = new int[]{30, 90, 0, 0, 0, 0, 0, 20, 0, 0};
//        list7.add(point7Action1);
//        cruiserWaypoint7.actions = list7;
//        wpList.add(cruiserWaypoint7);


        //添加兴趣点（可添加0个或多个兴趣点）
//        List<Poi> poiList = new ArrayList<>();
//        Poi cruiserPoi1 = new Poi();
//        cruiserPoi1.id = 0;
//        cruiserPoi1.coordinate3D = new AutelCoordinate3D(22.59594093275901, 113.99941807396686, 60);
//        poiList.add(cruiserPoi1);
//
//        Poi cruiserPoi2 = new Poi();
//        cruiserPoi2.id = 1;
//        cruiserPoi2.coordinate3D = new AutelCoordinate3D(22.595821147877796, 113.99901208495906, 60);
//        poiList.add(cruiserPoi2);

//        autelMission.wpoiList = poiList;
//        autelMission.wpList = wpList;
        autelMission.finishedAction = CruiserWaypointFinishedAction.RETURN_HOME;
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.prepare: {
                //飞行之前，必须进行必要的飞行检查
                if (!flyCheck()) {
                    return;
                }

                if (flyState != FlyState.None) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.prepareMission(autelMission, new CallbackWithOneParamProgress<Boolean>() {
                        @Override
                        public void onProgress(float v) {

                        }

                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            flyState = FlyState.Prepare;
                            AutelLog.d("prepareMission success");
                            Toast.makeText(DFWayPointActivity.this, "prepare success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {
                            AutelLog.d("prepareMission onFailure");
                            Toast.makeText(DFWayPointActivity.this, "prepare failed", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }
            break;

            case R.id.start: {
                if (flyState != FlyState.Prepare) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.startMission(new CallbackWithOneParam<Pair<Boolean, FlightErrorState>>() {
                        @Override
                        public void onSuccess(Pair<Boolean, FlightErrorState> booleanFlightErrorStatePair) {
                            flyState = FlyState.Start;
                            Toast.makeText(DFWayPointActivity.this, "start result " + booleanFlightErrorStatePair.first, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });

                }
            }
            break;

            case R.id.pause: {
                if (flyState != FlyState.Start) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.pauseMission(new CallbackWithNoParam() {
                        @Override
                        public void onSuccess() {
                            flyState = FlyState.Pause;
                            Toast.makeText(DFWayPointActivity.this, "pause success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });
                }
            }
            break;

            case R.id.resume: {
                if (flyState != FlyState.Pause) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.resumeMission(new CallbackWithNoParam() {
                        @Override
                        public void onSuccess() {
                            flyState = FlyState.Start;
                            Toast.makeText(DFWayPointActivity.this, "continue success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });
                }
            }
            break;

            case R.id.cancel: {
                if (flyState == FlyState.None) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.cancelMission(new CallbackWithNoParam() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(DFWayPointActivity.this, "cancel success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });
                }
            }
            break;

            case R.id.download: {
                if (flyState == FlyState.None) {
                    Toast.makeText(DFWayPointActivity.this, "当前状态，不能执行", Toast.LENGTH_LONG).show();
                    return;
                }
                if (null != missionManager) {
                    missionManager.downloadMission(new CallbackWithOneParamProgress<AutelMission>() {
                        @Override
                        public void onProgress(float v) {

                        }

                        @Override
                        public void onSuccess(AutelMission autelMission) {
                            Toast.makeText(DFWayPointActivity.this, "download success", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(AutelError autelError) {

                        }
                    });
                }
            }
            break;
        }
    }

    private boolean flyCheck() {
        if (!isBatteryOk) {
            Toast.makeText(DFWayPointActivity.this, "当前电池电量不足", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isImuOk) {
            Toast.makeText(DFWayPointActivity.this, "IMU异常", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isGpsOk) {
            Toast.makeText(DFWayPointActivity.this, "GPS异常", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isCompassOk) {
            Toast.makeText(DFWayPointActivity.this, "指南针异常", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!isImageTransOk) {
            Toast.makeText(DFWayPointActivity.this, "图传信号异常", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isCanTakeOff) {
            Toast.makeText(DFWayPointActivity.this, "飞行器不能起飞", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}