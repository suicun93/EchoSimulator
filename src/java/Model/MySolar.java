/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import com.sonycsl.echo.EchoProperty;
import java.io.IOException;

/**
 *
 * @author hoang-trung-duc
 */
public class MySolar extends com.sonycsl.echo.eoj.device.housingfacilities.HouseholdSolarPowerGeneration {

    // Mutual properties
    private final byte mInstanceCode = (byte) 0x02;
    private final byte[] mOperationStatus = {(byte) 0x31};                              // EPC = 0x80
    private final byte[] mInsallationLocation = {(byte) 0x00};                          // EPC = 0x81
    private final byte[] mFaultStatus = {(byte) 0x42};                                  // EPC = 0x88
    private final byte[] mManufacturerCode = {0, 0, 0};                                 // EPC = 0x8A

    // Private properties
    private final byte[] mInstantaneousAmountOfElectricityGenerated = {(byte) 0x00, (byte) 0x15};                           // 0xE0 W
    private final byte[] mCumulativeAmountOfElectricityGenerated = {(byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x15};    // 0xE1 kWh

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

            // EPC = 0xD4
            case EPC_MEASURED_INSTANTANEOUS_AMOUNT_OF_ELECTRICITY_GENERATED:
                System.arraycopy(property.edt, 0, mInstantaneousAmountOfElectricityGenerated, 0, 2);
                return true;

            default:
                return false;
        }
    }
}
