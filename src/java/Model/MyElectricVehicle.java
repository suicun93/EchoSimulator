package Model;

import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.device.housingfacilities.ElectricVehicle;
import java.io.IOException;

/**
 *
 * @author hoang-trung-duc
 */
public class MyElectricVehicle extends ElectricVehicle {

    // Mutual properties
    private final byte mInstanceCode = (byte) 0x02;
    private final byte[] mOperationStatus = {(byte) 0x30};            // EPC = 0x80
    private final byte[] mInsallationLocation = {(byte) 0x00};        // EPC = 0x81
    private final byte[] mFaultStatus = {(byte) 0x42};                // EPC = 0x88
    private final byte[] mManufacturerCode = {0, 0, 0};               // EPC = 0x8A
    // Private properties
    private final byte[] mV2hStoredElectricity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};           // EPC = 0xC0
    private final byte[] mV2hStoredElectricity2 = {(byte) 0x00, (byte) 0x93};                                     // EPC = 0xC1
    private final byte[] mV2hRemainingAvailableCapacity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};  // EPC = 0xC2
    private final byte[] mV2hRemainingAvailableCapacity2 = {(byte) 0x00, (byte) 0x93};                            // EPC = 0xC3
    private final byte[] mV2hRemainingAvailableCapacity3 = {(byte) 0x50};                                         // EPC = 0xC4
    private final byte[] mRatedChargeCapacity = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};             // EPC = 0xC5
    private final byte[] mRatedDischargeCapacity = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};          // EPC = 0xC6
    private final byte[] mChargeableDischargeAbleStatus = {(byte) 0x42};                                          // EPC = 0xC7
    private final byte[] mUsedCapacity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x93, (byte) 0x93};                   // EPC = 0xD0
    private final byte[] mUsedCapacity2 = {(byte) 0x00, (byte) 0x93};                                             // EPC = 0xD1
    private final byte[] mInstantaneousChargeDischargeCurrent = {(byte) 0x00, (byte) 0x93};                       // EPC = 0xD4
    private final byte[] mOperationModeSetting = {(byte) 0x42};                                                   // EPC = 0xDA
    private final byte[] mRemainingBatteryCapacity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};       // EPC = 0xE2
    private final byte[] mRemainingBatteryCapacity2 = {(byte) 0x00, (byte) 0x62};                                 // EPC = 0xE3
    private final byte[] mRemainingBatteryCapacity3 = {(byte) 0x60};                                              // EPC = 0xE4

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
        addGetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT);
        // Setter
        addSetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT);
        addSetProperty(EPC_REMAINING_BATTERY_CAPACITY1);
        addSetProperty(EPC_REMAINING_BATTERY_CAPACITY3);
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
            System.out.println(ex.getMessage());
        }
        return true;
    }

    @Override
    protected boolean setInstallationLocation(byte[] bytes) {
        try {
            // Announcement at status changed
            mInsallationLocation[0] = bytes[0];
            inform().reqInformInstallationLocation().send();
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
    protected boolean setOperationModeSetting(byte[] bytes) {
        try {
            // Announcement at status changed
            mOperationModeSetting[0] = bytes[0];
            inform().reqInformOperationModeSetting().send();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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
        return mRemainingBatteryCapacity3;
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
        switch (property.epc) {

            case EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT:  // EPC = 0xD4
                if (property.edt.length == 2) {
                    System.arraycopy(property.edt, 0,
                            mInstantaneousChargeDischargeCurrent, 0, 2);
                    super.setProperty(property);
                    return true;
                } else {
                    return super.setProperty(property);
                }

            case EPC_REMAINING_BATTERY_CAPACITY1:                      // EPC = 0xE2
                if (property.edt.length == 4) {
                    System.arraycopy(property.edt, 0,
                            mRemainingBatteryCapacity1, 0, 4);
                    super.setProperty(property);
                    return true;
                } else {
                    return super.setProperty(property);
                }

            case EPC_REMAINING_BATTERY_CAPACITY3:                      // EPC = 0xE4
                mRemainingBatteryCapacity3[0] = property.edt[0];
                super.setProperty(property);
                return true;

            default:
                return super.setProperty(property);
        }
    }

    @Override
    protected byte[] getMeasuredInstantaneousChargeDischargeCurrent() {
        return mInstantaneousChargeDischargeCurrent;
    }
}
