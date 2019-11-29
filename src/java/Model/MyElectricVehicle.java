package Model;

import Common.Config;
import Common.Convert;
import Main.EchoController;
import Model.MyDeviceObject.Operation;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.device.housingfacilities.ElectricVehicle;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author hoang-trung-duc
 */
public class MyElectricVehicle extends ElectricVehicle {

    // Mutual properties
    private final byte mInstanceCode = (byte) 0x02;
    private final byte[] mOperationStatus = {(byte) 0x31};            // EPC = 0x80
    private final byte[] mInsallationLocation = {(byte) 0x00};        // EPC = 0x81
    private final byte[] mFaultStatus = {(byte) 0x42};                // EPC = 0x88
    private final byte[] mManufacturerCode = {0, 0, 0};               // EPC = 0x8A
    // Private properties
    private final byte[] mV2hStoredElectricity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};                         // EPC = 0xC0
    private final byte[] mV2hStoredElectricity2 = {(byte) 0x00, (byte) 0x93};                                                   // EPC = 0xC1
    private final byte[] mV2hRemainingAvailableCapacity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};                // EPC = 0xC2
    private final byte[] mV2hRemainingAvailableCapacity2 = {(byte) 0x00, (byte) 0x93};                                          // EPC = 0xC3
    private final byte[] mV2hRemainingAvailableCapacity3 = {(byte) 0x50};                                                       // EPC = 0xC4
    private final byte[] mRatedChargeCapacity = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};                           // EPC = 0xC5
    private final byte[] mRatedDischargeCapacity = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};                        // EPC = 0xC6
    private final byte[] mChargeableDischargeAbleStatus = {(byte) 0x42};                                                        // EPC = 0xC7
    private final byte[] mUsedCapacity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x0B, (byte) 0xB8};                                 // EPC = 0xD0 (Wh)  3000 
    private final byte[] mUsedCapacity2 = {(byte) 0x00, (byte) 0x93};                                                           // EPC = 0xD1
    private final byte[] mInstantaneousChargeDischargeElectricEnergy = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x64};    // EPC = 0xD3 (W)   3600
//    private final byte[] mInstantaneousChargeDischargeCurrent = {(byte) 0x00, (byte) 0x93};                                   // EPC = 0xD4 (A)
    private final byte[] mOperationModeSetting = {(byte) 0x44};                                                                 // EPC = 0xDA Discharging 
    private final byte[] mRemainingBatteryCapacity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0xC4};                     // EPC = 0xE2 (Wh)  2500 
    private final byte[] mRemainingBatteryCapacity2 = {(byte) 0x00, (byte) 0x62};                                               // EPC = 0xE3 (0.1Ah)
//    private final byte[] mRemainingBatteryCapacity3 = {(byte) 0x60};                                                          // EPC = 0xE4 (%) => calculated
    private Timer startPowerConsumption, endPowerConsumption;

    /**
     * Setup Property maps for getter, setter, status announcement changed
     * notifier
     *
     * @Getter:
     * @ - EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT
     *
     * @Setter:
     * @ - EPC_REMAINING_BATTERY_CAPACITY1
     * @ - EPC_REMAINING_BATTERY_CAPACITY3
     * @ - EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT
     */
    @Override
    protected void setupPropertyMaps() {
        super.setupPropertyMaps();
        // Getter
        addGetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY);

        // Setter
        addSetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY);
        addSetProperty(EPC_REMAINING_BATTERY_CAPACITY1);
//        addSetProperty(EPC_REMAINING_BATTERY_CAPACITY3);
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
            // Announcement at status changed
            mOperationStatus[0] = edt[0];
            inform().reqInformOperationStatus().send();
        } catch (IOException ex) {
            System.out.println("EV setOperationStatus: " + ex.getMessage());
        }
        return true;
    }

    @Override
    protected boolean setInstallationLocation(byte[] edt) {
        try {
            // Announcement at status changed
            mInsallationLocation[0] = edt[0];
            inform().reqInformInstallationLocation().send();
        } catch (IOException ex) {
            System.out.println("EV setInstallationLocation: " + ex.getMessage());
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
     * https://echonet.jp/wp/wp-content/uploads/pdf/General/Standard/Release/Release_L_en/Appendix_Release_L_E.pdf#page=341
     *
     * @return
     */
    @Override
    protected byte[] getV2hStoredElectricity1() {
        return mV2hStoredElectricity1;
    }

    @Override
    protected byte[] getV2hStoredElectricity2() {
        return mV2hStoredElectricity2;
    }

    @Override
    protected byte[] getV2hRemainingAvailableCapacity1() {
        return mV2hRemainingAvailableCapacity1;
    }

    @Override
    protected byte[] getV2hRemainingAvailableCapacity2() {
        return mV2hRemainingAvailableCapacity2;
    }

    @Override
    protected byte[] getV2hRemainingAvailableCapacity3() {
        return mV2hRemainingAvailableCapacity3;
    }

    @Override
    protected byte[] getRatedChargeCapacity() {
        return mRatedChargeCapacity;
    }

    @Override
    protected byte[] getRatedDischargeCapacity() {
        return mRatedDischargeCapacity;
    }

    @Override
    protected byte[] getChargeableDischargeAbleStatus() {
        return mChargeableDischargeAbleStatus;
    }

    @Override
    protected byte[] getUsedCapacity1() {
        return mUsedCapacity1;
    }

    @Override
    protected byte[] getUsedCapacity2() {
        return mUsedCapacity2;
    }

    @Override
    protected boolean setOperationModeSetting(byte[] edt) {
        try {
            // Announcement at status changed
            mOperationModeSetting[0] = edt[0];
            inform().reqInformOperationModeSetting().send();
        } catch (IOException ex) {
            System.out.println("EV setOperationModeSetting: " + ex.getMessage());
        }
        return true;
    }

    @Override
    protected byte[] getOperationModeSetting() {
        return mOperationModeSetting;
    }

    @Override
    protected byte[] getRemainingBatteryCapacity1() {
        return mRemainingBatteryCapacity1;
    }

    @Override
    protected byte[] getRemainingBatteryCapacity2() {
        return mRemainingBatteryCapacity2;
    }

    @Override
    protected byte[] getRemainingBatteryCapacity3() {
        // e4 = e2/d0 * 100%
        int e2 = Convert.byteArrayToInt(getRemainingBatteryCapacity1());
        int d0 = Convert.byteArrayToInt(getUsedCapacity1());
        int percent = (int) ((float) e2 / d0) * 100;
        return new byte[]{Convert.intToByte(percent)};
    }

    /**
     * Add for my self challenging.
     *
     * @Setter for
     * @ - EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT
     * @ - EPC_REMAINING_BATTERY_CAPACITY1
     * @ - EPC_REMAINING_BATTERY_CAPACITY3
     *
     * This is not allowed in document but I want it so I changed some methods.
     * @Link:
     * https://echonet.jp/wp/wp-content/uploads/pdf/General/Standard/Release/Release_L_en/Appendix_Release_L_E.pdf#page=341
     *
     * @param property
     * @return super.setProperty(property);
     */
    @Override
    protected synchronized boolean setProperty(EchoProperty property) {
        boolean success = super.setProperty(property);
        if (success) {
            return success;
        }

        switch (property.epc) {
            // EPC = 0xD3
            case EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY:
                System.arraycopy(property.edt, 0, mInstantaneousChargeDischargeElectricEnergy, 0, 4);
                return true;

            // EPC = 0xE2
            case EPC_REMAINING_BATTERY_CAPACITY1:
                System.arraycopy(property.edt, 0, mRemainingBatteryCapacity1, 0, 4);
                return true;

//            // EPC = 0xE4
//            case EPC_REMAINING_BATTERY_CAPACITY3:
//                System.arraycopy(property.edt, 0, mRemainingBatteryCapacity3, 0, 1);
//                return true;
            default:
                return false;
        }
    }

    @Override
    protected byte[] getMeasuredInstantaneousChargeDischargeElectricEnergy() {
        return mInstantaneousChargeDischargeElectricEnergy;
    }

    private Timer increaseE2;
    private float currentE2;

    public void schedule() throws Exception {
        if (!EchoController.contains("ev")) {
            throw new Exception("EV did not run yet.");
        }

        // Load config
        String paramString = Config.load("ev.txt");
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
                // 0xDA = mode
                setOperationModeSetting(mode);
                // 0xD3 = d3
                setProperty(new EchoProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY, Convert.intToByteArray(d3)));

                // Get D0, E2
                int d0 = Convert.byteArrayToInt(getUsedCapacity1());
                currentE2 = Convert.byteArrayToInt(getRemainingBatteryCapacity1());
                System.out.println("\n\nEV Charging Started: E2 = " + currentE2);

                // Loop every second
                int delay = 0;
                int period = 1000;
                long secondInHour = TimeUnit.SECONDS.convert(1, TimeUnit.HOURS);
                if (increaseE2 != null) {
                    increaseE2.cancel();
                }
                increaseE2 = new Timer();
                increaseE2.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        currentE2 = currentE2 + ((float) d3 / secondInHour);
                        System.out.println("EV Second " + Convert.getCurrentTime() + ", E2 = " + currentE2);
                        setProperty(new EchoProperty(EPC_REMAINING_BATTERY_CAPACITY1, Convert.intToByteArray((int) currentE2)));

                        if (currentE2 >= d0) {
                            // If 0xE2 >= 0xD0, 0xE2 = 0xD0  AND  0xDA = 0x44.
                            setProperty(new EchoProperty(EPC_REMAINING_BATTERY_CAPACITY1, getUsedCapacity1())); // 0xE2 = 0xD0
                            setOperationModeSetting(new byte[]{(byte) 0x44});                                   // 0xDA = 0x44.
                            // Log and cancel
                            System.out.println("EV Charged Full E2 = " + Convert.byteArrayToInt(getRemainingBatteryCapacity1()));
                            increaseE2.cancel();
                        }
                    }
                }, delay, period);
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
                }
                // If 0xE2 >= 0xD0,  0xDA = 0x44.
                setOperationModeSetting(new byte[]{(byte) 0x44});                                       // 0xDA = 0x44.
                // Log and cancel
                System.out.println("EV Charging Ends E2 = " + Convert.byteArrayToInt(getRemainingBatteryCapacity1()));
            }
        }, endCalendar.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }
}
