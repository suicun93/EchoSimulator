package Model;

import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.device.housingfacilities.Battery;
import java.io.IOException;

/**
 *
 * @author hoang-trung-duc
 */
public class MyBattery extends Battery {

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
    private final byte[] mInstantaneousChargeDischargeElectricEnergy = {(byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x92};            // EPC = 0xD3 (W)
    private final byte[] mInstantaneousChargeDischargeCurrent = {(byte) 0x00, (byte) 0x92};                                             // EPC = 0xD4 (A)
    private final byte[] mOperationModeSetting = {(byte) 0x44};                                                                         // EPC = 0xDA
    private final byte[] mRemainingStoredElectricity1 = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x93};                           // EPC = 0xE2 
    private final byte[] mRemainingStoredElectricity2 = {(byte) 0x00, (byte) 0x93};                                                     // EPC = 0xE3
    private final byte[] mRemainingStoredElectricity3 = {(byte) 0x60};                                                                  // EPC = 0xE4
    private final byte[] mBatteryType = {(byte) 0x01};                                                                                  // EPC = 0xE6

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
        // Getter
        addGetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT);
        addGetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY);

        // Setter
        addSetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT);
        addSetProperty(EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY);
        addSetProperty(EPC_REMAINING_STORED_ELECTRICITY1);
        addSetProperty(EPC_REMAINING_STORED_ELECTRICITY3);
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
    protected byte[] getOperationModeSetting() {
        return mOperationModeSetting;
    }

    @Override
    protected boolean setOperationModeSetting(byte[] edt) {
        try {
            // Announcement at status change
            mOperationModeSetting[0] = edt[0];
            inform().reqInformOperationModeSetting().send();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
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
        return mRemainingStoredElectricity3;
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
    protected synchronized boolean setProperty(EchoProperty property) {
        boolean success = super.setProperty(property);
        if (success) {
            return success;
        }

        // My Set property
        switch (property.epc) {

            // EPC = 0xD4
            case EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_CURRENT:
                System.arraycopy(property.edt, 0, mInstantaneousChargeDischargeCurrent, 0, 2);
                return true;

            // EPC = 0xD3
            case EPC_MEASURED_INSTANTANEOUS_CHARGE_DISCHARGE_ELECTRIC_ENERGY:
                System.arraycopy(property.edt, 0, mInstantaneousChargeDischargeElectricEnergy, 0, 4);
                return true;

            // EPC = 0xE2
            case EPC_REMAINING_STORED_ELECTRICITY1:
                System.arraycopy(property.edt, 0, mRemainingStoredElectricity1, 0, 4);
                return true;

            // EPC = 0xE4
            case EPC_REMAINING_STORED_ELECTRICITY3:
                System.arraycopy(property.edt, 0, mRemainingStoredElectricity3, 0, 1);
                return true;

            default:
                return false;
        }
    }

    @Override
    protected byte[] getMeasuredInstantaneousChargeDischargeCurrent() {
        return mInstantaneousChargeDischargeCurrent;
    }

    @Override
    protected byte[] getMeasuredInstantaneousChargeDischargeElectricEnergy() {
        return mInstantaneousChargeDischargeElectricEnergy;
    }
}
