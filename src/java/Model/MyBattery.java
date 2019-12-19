package Model;

import Common.Config;
import Common.Convert;
import Main.EchoController;
import Model.MyDeviceObject.Operation;
import Model.MyDeviceObject.OperationMode;
import static Model.MyDeviceObject.OperationMode.Charging;
import static Model.MyDeviceObject.OperationMode.Discharging;
import static Model.MyDeviceObject.OperationMode.RapidCharging;
import static Model.MyDeviceObject.OperationMode.Standby;

import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.device.housingfacilities.Battery;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author hoang-trung-duc
 */
public class MyBattery extends Battery {

    public static String name = "battery";

    // The OpenECHO Library is having an error in this parameter's definition
    // This error made me be angry so I want to fix it.
    public static final byte EPC_REMAINING_STORED_ELECTRICITY3
            = EPC_REMAINING_STORED_ELECTRICITY3_BATTERY_STATE_OF_HEALTH;

    // Mutual properties
    private final byte mInstanceCode = (byte) 0x02;
    private final byte[] mOperationStatus = {(byte) 0x31};                              // EPC = 0x80
    private final byte[] mInsallationLocation = {(byte) 0x00};                          // EPC = 0x81
    private final byte[] mFaultStatus = {(byte) 0x42};                                  // EPC = 0x88
    private final byte[] mManufacturerCode = {0, 0, 0};                                 // EPC = 0x8A

    // Private properties
    private final byte[] mRatedElectricEnergy = {(byte) 0x00, (byte) 0x00, (byte) 0x0B, (byte) 0xB8};                                   // EPC = 0xD0 (Wh)  3000
    private final byte[] mInstantaneousChargeDischargeElectricEnergy = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};            // EPC = 0xD3 (W)   3600
//    private final byte[] mInstantaneousChargeDischargeCurrent = {(byte) 0x00, (byte) 0x92};                                           // EPC = 0xD4 (A)
    private final byte[] mOperationModeSetting = {(byte) 0x44};                                                                         // EPC = 0xDA Standby
    private final byte[] mRemainingStoredElectricity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0xC4};                           // EPC = 0xE2 (Wh)  2500
    private final byte[] mRemainingStoredElectricity2 = {(byte) 0x00, (byte) 0x93};                                                     // EPC = 0xE3 (0.1Ah)
//    private final byte[] mRemainingStoredElectricity3 = {(byte) 0x60};                                                                // EPC = 0xE4 (%)
    private final byte[] mBatteryType = {(byte) 0x01};                                                                                  // EPC = 0xE6
    private Timer startPowerConsumption, endPowerConsumption;
    private Timer increaseE2;
    private float currentE2;

    /**
     * Setup Property maps for getter, setter, status announcement changed
     * notifier
     *
     * @Getter:
     * @ - EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT
     *
     * @Setter:
     * @ - EPC_REMAINING_STORED_ELECTRICITY1
     * @ - EPC_REMAINING_STORED_ELECTRICITY3
     * @ - EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT
     */
    @Override
    public void onNew() {
        super.onNew();
        try {
            schedule();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void stop() {
        if (startPowerConsumption != null) {
            startPowerConsumption.cancel();
            startPowerConsumption = null;
        }
        if (endPowerConsumption != null) {
            endPowerConsumption.cancel();
            endPowerConsumption = null;
        }
        if (increaseE2 != null) {
            increaseE2.cancel();
            increaseE2 = null;
        }

    }

    @Override
    protected void setupPropertyMaps() {
        super.setupPropertyMaps();
        // Getter
        addGetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY);
        addGetProperty(EPC_RATED_ELECTRIC_ENERGY);

        // Setter
        addSetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY);
        addSetProperty(EPC_REMAINING_STORED_ELECTRICITY1);
//        addSetProperty(EPC_REMAINING_STORED_ELECTRICITY3);
//        addSetProperty(EPC_RATED_ELECTRIC_ENERGY);

        // Inform
        addStatusChangeAnnouncementProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY);
    }

    /**
     * Mutual methods
     *
     * These methods are implemented for every nodes mandatory.
     *
     * @Link:
     * https://echonet.jp/wp/wp-content/uploads/pdf/General/Standard/Release/Release_L_en/Appendix_Release_L_E.pdf#page=36
     *
     * @return
     */
    @Override
    protected byte[] getOperationStatus() {
        return mOperationStatus;
    }

    @Override
    protected boolean setOperationStatus(byte[] edt) {
        try {
            // Announcement at status change
            mOperationStatus[0] = edt[0];
            inform().reqInformOperationStatus().send();
        } catch (Exception ex) {
            System.out.println("Battery setOperationStatus: " + ex.getMessage());
        }
        return true;
    }

    @Override
    protected boolean setInstallationLocation(byte[] edt) {
        try {
            // Announcement at status change
            mInsallationLocation[0] = edt[0];
            inform().reqInformOperationStatus().send();
        } catch (Exception ex) {
            System.out.println("Battery setInstallationLocation: " + ex.getMessage());
        }
        return true;
    }

    @Override
    protected byte[] getInstallationLocation() {
        return mInsallationLocation;
    }

    @Override
    protected byte[] getFaultStatus() {
        return mFaultStatus;
    }

    @Override
    protected byte[] getManufacturerCode() {
        return mManufacturerCode;
    }

    @Override
    public byte getInstanceCode() {
        return mInstanceCode;
    }

    /**
     * Private methods
     *
     * These methods are implemented for only EV mandatory.
     *
     * @Link:
     * https://echonet.jp/wp/wp-content/uploads/pdf/General/Standard/Release/Release_L_en/Appendix_Release_L_E.pdf#page=319
     *
     * @return
     */
    @Override
    public byte[] getOperationModeSetting() {
        return mOperationModeSetting;
    }

    @Override
    public boolean setOperationModeSetting(byte[] edt) {
        try {
            // Announcement at status change
            mOperationModeSetting[0] = edt[0];
            // Running like a actual device.
            // Get D0, E2
            int d0 = Convert.byteArrayToInt(getRatedElectricEnergy());
            int d3 = Convert.byteArrayToInt(getMeasuredInstantaneousChargeDischargeElectricEnergy());
            currentE2 = Convert.byteArrayToInt(getRemainingStoredElectricity1());
            // Loop every second
            int delay = 0;
            int period = 1000;
            long secondInHour = TimeUnit.SECONDS.convert(1, TimeUnit.HOURS);

            switch (OperationMode.from(edt[0])) {
                case RapidCharging:
                case Charging:
                    System.out.println("\n\nBattery Charging Started: E2 = " + currentE2);
                    if (increaseE2 != null) {
                        increaseE2.cancel();
                    }
                    increaseE2 = new Timer();
                    increaseE2.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            currentE2 = currentE2 + ((float) d3 / secondInHour);
                            System.out.println("Battery Time " + Convert.getCurrentTime() + ", E2 = " + currentE2);
                            setProperty(new EchoProperty(EPC_REMAINING_STORED_ELECTRICITY1, Convert.intToByteArray((int) currentE2)));

                            if (currentE2 >= d0) {
                                // If 0xE2 >= 0xD0, 0xE2 = 0xD0  AND  0xDA = 0x44.
                                setProperty(new EchoProperty(EPC_REMAINING_STORED_ELECTRICITY1, getRatedElectricEnergy())); // 0xE2 = 0xD0
                                setOperationModeSetting(new byte[]{Standby.value});                                       // 0xDA = 0x44.
                                // Log and cancel
                                System.out.println("Battery Charged Full E2 = " + Convert.byteArrayToInt(getRemainingStoredElectricity1()));
                                increaseE2.cancel();
                                increaseE2 = null;
                            }
                        }
                    }, delay, period);
                    break;
                case Discharging:
                    System.out.println("\n\nBattery Discharging Started: E2 = " + currentE2);
                    if (increaseE2 != null) {
                        increaseE2.cancel();
                    }
                    increaseE2 = new Timer();
                    increaseE2.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            currentE2 = currentE2 - ((float) d3 / secondInHour);
                            System.out.println("Battery Time " + Convert.getCurrentTime() + ", E2 = " + currentE2);
                            setProperty(new EchoProperty(EPC_REMAINING_STORED_ELECTRICITY1, Convert.intToByteArray((int) currentE2)));

                            if (currentE2 <= 0) {
                                // If 0xE2 <= 0, 0xE2 = 0  AND  0xDA = 0x44.
                                setProperty(new EchoProperty(EPC_REMAINING_STORED_ELECTRICITY1, Convert.intToByteArray(0)));  // 0xE2 = 0.
                                setOperationModeSetting(new byte[]{Standby.value});                                         // 0xDA = 0x44.
                                // Log and cancel
                                System.out.println("Battery DisCharge out of energy E2 = " + Convert.byteArrayToInt(getRemainingStoredElectricity1()));
                                increaseE2.cancel();
                                increaseE2 = null;
                            }
                        }
                    }, delay, period);
                    break;
                default:
                    if (increaseE2 != null) {
                        increaseE2.cancel();
                        increaseE2 = null;
                        System.out.println("\n\nBattery Cancelled: E2 = " + currentE2);
                    }
                    setProperty(new EchoProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY, Convert.intToByteArray(0)));
                    break;
            }

            inform().reqInformOperationModeSetting().send();
        } catch (Exception ex) {
            System.out.println("Battery setOperationModeSetting: " + ex.getMessage());
        }
        return true;
    }

    @Override
    protected byte[] getRemainingStoredElectricity1() {
        return mRemainingStoredElectricity1;
    }

    @Override
    protected byte[] getRemainingStoredElectricity2() {
        return mRemainingStoredElectricity2;
    }

    @Override
    protected byte[] getRemainingStoredElectricity3BatteryStateOfHealth() {
        // e4 = e2/d0 * 100%
        int e2 = Convert.byteArrayToInt(getRemainingStoredElectricity1());
        int d0 = Convert.byteArrayToInt(getRatedElectricEnergy());
        int percent = (int) (((float) e2 / d0) * 100);
        return new byte[]{Convert.intToByte(percent)};
    }

    @Override
    protected byte[] getBatteryType() {
        return mBatteryType;
    }

    /**
     * Add for my self challenging.
     *
     * @Setter for
     * @ - EPC_REMAINING_STORED_ELECTRICITY1
     * @ - EPC_REMAINING_STORED_ELECTRICITY3
     * @ - EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT
     *
     * This is not allowed in document but I want it so I changed some methods.
     * @Link:
     * https://echonet.jp/wp/wp-content/uploads/pdf/General/Standard/Release/Release_L_en/Appendix_Release_L_E.pdf#page=319
     *
     * @param property
     * @return super.setProperty(property);
     */
    @Override
    protected synchronized boolean setProperty(EchoProperty property
    ) {
        boolean success = super.setProperty(property);
        if (success) {
            return success;
        }

        // My Set property
        switch (property.epc) {
//            // EPC = 0xD0 Cummulative Chargable Electric Energy
//            case EPC_RATED_ELECTRIC_ENERGY:
//                System.arraycopy(property.edt, 0, mRatedElectricEnergy, 0, 4);
//                return true;

            // EPC = 0xD3 đạo hàm của E2
            case EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY:
                System.arraycopy(property.edt, 0, mInstantaneousChargeDischargeElectricEnergy, 0, 4);
                try {
                    inform().reqInformMeasuredInstantaneousChargeDischargeElectricEnergy().send();
                } catch (IOException ex) {
                    System.out.println("Battery EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY inform: " + ex.getMessage());
                }
                return true;

            // EPC = 0xE2 lượng điện hiện thời
            case EPC_REMAINING_STORED_ELECTRICITY1:
                System.arraycopy(property.edt, 0, mRemainingStoredElectricity1, 0, 4);
                return true;

//            // EPC = 0xE4 lượng điện hiện thời %
//            case EPC_REMAINING_STORED_ELECTRICITY3:
//                System.arraycopy(property.edt, 0, mRemainingStoredElectricity3, 0, 1);
//                return true;
            default:
                return false;
        }
    }

    @Override
    protected byte[] getRatedElectricEnergy() {
        return mRatedElectricEnergy;
    }

    @Override
    protected byte[] getMeasuredInstantaneousChargeDischargeElectricEnergy() {
        return mInstantaneousChargeDischargeElectricEnergy;
    }

    public void schedule() throws Exception {
        if (!EchoController.contains("battery")) {
            throw new Exception("Battery did not run yet.");
        }

        // Load config
        String paramString = Config.load("battery.txt");
        if (paramString.isEmpty()) {
            throw new Exception("Config file " + "Battery" + " is Empty");
        }
        String[] params = paramString.split("\\,");
        String startTimeStr = params[0];
        String endTimeStr = params[1];
        String modeStr = params[2];
        String d3Str = params[3];

        // Time, Mode, D3
        String[] startTime = startTimeStr.split("\\:");
        String[] endTime = endTimeStr.split("\\:");
        int hourStart = Integer.parseInt(startTime[0]);
        int minuteStart = Integer.parseInt(startTime[1]);
        int hourEnd = Integer.parseInt(endTime[0]);
        int minuteEnd = Integer.parseInt(endTime[1]);
        byte[] mode = Convert.hexStringToByteArray(modeStr);
        int d3 = Integer.parseInt(d3Str);

        // SetTimeForCalendar
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, hourStart);
        startCalendar.set(Calendar.MINUTE, minuteStart);
        startCalendar.set(Calendar.SECOND, 0);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, hourEnd);
        endCalendar.set(Calendar.MINUTE, minuteEnd);
        endCalendar.set(Calendar.SECOND, 0);

        // Start Timer Initilization.
        if (startPowerConsumption != null) {
            startPowerConsumption.cancel();
        }

        startPowerConsumption = new Timer();
        startPowerConsumption.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                // 0x80 = 0x30
                setOperationStatus(new byte[]{Operation.ON.value});

                // 0xD3 = d3
                setProperty(new EchoProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY, Convert.intToByteArray(d3)));

                // 0xDA = mode
                setOperationModeSetting(mode);
            }
        }, startCalendar.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));

        // End Timer Initilization.
        if (endPowerConsumption != null) {
            endPowerConsumption.cancel();
        }
        endPowerConsumption = new Timer();
        endPowerConsumption.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
                if (increaseE2 != null) {
                    increaseE2.cancel();
                    increaseE2 = null;
                    // 0xDA = 0x44.
                    setOperationModeSetting(new byte[]{Standby.value});
                    // Log and cancel
                    System.out.println("Battery Charging Ends E2 = " + Convert.byteArrayToInt(getRemainingStoredElectricity1()));
                }
            }
        }, endCalendar.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }
}
