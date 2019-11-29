/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Common.Config;
import Common.Convert;
import Main.EchoController;
import com.sonycsl.echo.EchoProperty;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import Model.MyDeviceObject.Operation;

/**
 *
 * @author hoang-trung-duc
 */
public class MySolar extends com.sonycsl.echo.eoj.device.housingfacilities.HouseholdSolarPowerGeneration {

    // Mutual properties
    private final byte mInstanceCode = (byte) 0x02;
    private final byte[] mOperationStatus = new byte[]{Operation.OFF.value};              // EPC = 0x80
    private final byte[] mInsallationLocation = {(byte) 0x00};                          // EPC = 0x81
    private final byte[] mFaultStatus = {(byte) 0x42};                                  // EPC = 0x88
    private final byte[] mManufacturerCode = {0, 0, 0};                                 // EPC = 0x8A

    // Private properties
    private final byte[] mInstantaneousAmountOfElectricityGenerated = {(byte) 0x00, (byte) 0x15};                           // 0xE0 W
    private final byte[] mCumulativeAmountOfElectricityGenerated = {(byte) 0x00, (byte) 0x00, (byte) 0x0B, (byte) 0xB8};    // 0xE1 kWh 2500
    private Timer startPowerConsumption, endPowerConsumption;

     @Override
    public void onNew() {
        super.onNew();
        try {
            schedule();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
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
    protected void setupPropertyMaps() {
        super.setupPropertyMaps();
        // Setter
        addSetProperty(EPC_MEASURED_INSTANTANEOUS_AMOUNT_OF_ELECTRICITY_GENERATED);
        addSetProperty(EPC_MEASURED_CUMULATIVE_AMOUNT_OF_ELECTRICITY_GENERATED);
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
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return true;
    }

    @Override
    protected boolean setInstallationLocation(byte[] edt) {
        try {
            // Announcement at status change
            mInsallationLocation[0] = edt[0];
            inform().reqInformOperationStatus().send();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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
    protected byte[] getMeasuredInstantaneousAmountOfElectricityGenerated() {
        return mInstantaneousAmountOfElectricityGenerated;
    }

    @Override
    protected byte[] getMeasuredCumulativeAmountOfElectricityGenerated() {
        return mCumulativeAmountOfElectricityGenerated;
    }

    @Override
    protected synchronized boolean setProperty(EchoProperty property) {
        boolean success = super.setProperty(property);
        if (success) {
            return success;
        }

        switch (property.epc) {

            // EPC = 0xE0
            case EPC_MEASURED_INSTANTANEOUS_AMOUNT_OF_ELECTRICITY_GENERATED:
                System.arraycopy(property.edt, 0, mInstantaneousAmountOfElectricityGenerated, 0, 2);
                return true;
            // EPC = 0xE1
            case EPC_MEASURED_CUMULATIVE_AMOUNT_OF_ELECTRICITY_GENERATED:
                System.arraycopy(property.edt, 0, mCumulativeAmountOfElectricityGenerated, 0, 4);
                return true;
            default:
                return false;
        }
    }

    // My challenge
    private Timer increaseE1;
    private float currentE1;

    public void schedule() throws Exception {
        if (!EchoController.contains("solar")) {
            throw new Exception("Solar did not run yet.");
        }

        // Load config
        String paramString = Config.load("solar.txt");
        String[] params = paramString.split("\\,");
        String startTimeStr = params[0];
        String endTimeStr = params[1];
        String e0Str = params[3];

        // Time, Mode, E0
        String[] startTime = startTimeStr.split("\\:");
        String[] endTime = endTimeStr.split("\\:");
        int hourStart = Integer.parseInt(startTime[0]);
        int minuteStart = Integer.parseInt(startTime[1]);
        int hourEnd = Integer.parseInt(endTime[0]);
        int minuteEnd = Integer.parseInt(endTime[1]);
        int e0 = Integer.parseInt(e0Str);

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
                // 0xE0 = e0
                setProperty(new EchoProperty(EPC_MEASURED_INSTANTANEOUS_AMOUNT_OF_ELECTRICITY_GENERATED, Convert.intToByteArray(e0)));

                // Get E1
                currentE1 = Convert.byteArrayToInt(getMeasuredCumulativeAmountOfElectricityGenerated());
                System.out.println("\n\nSolar Charging Started: E1 = " + currentE1);

                // Loop every second
                int delay = 0;
                int period = 1000;
                long secondInHour = TimeUnit.SECONDS.convert(1, TimeUnit.HOURS);
                if (increaseE1 != null) {
                    increaseE1.cancel();
                }
                increaseE1 = new Timer();
                increaseE1.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        currentE1 = currentE1 + ((float) e0 / secondInHour);
                        System.out.println("Solar Second " + Convert.getCurrentTime() + ", E1 = " + currentE1);
                        setProperty(new EchoProperty(EPC_MEASURED_CUMULATIVE_AMOUNT_OF_ELECTRICITY_GENERATED, Convert.intToByteArray((int) currentE1)));
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
                if (increaseE1 != null) {
                    increaseE1.cancel();
                }
                //  0x80 = 0x31.
                setOperationStatus(new byte[]{Operation.OFF.value});
                // Log and cancel
                System.out.println("Solar Charging Ends E1 = " + Convert.byteArrayToInt(getMeasuredCumulativeAmountOfElectricityGenerated()));
            }
        }, endCalendar.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }
}
