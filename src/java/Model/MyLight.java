/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Common.DebugLog;
import java.io.IOException;

import Model.MyDeviceObject.Operation;
import static Model.MyDeviceObject.Operation.OFF;
import static Model.MyDeviceObject.Operation.ON;

import Common.GPIOManager;
import Common.GPIOManager.PinState;
import Common.GPIOManager.Port;

/**
 *
 * @author hoang-trung-duc
 */
public class MyLight extends com.sonycsl.echo.eoj.device.housingfacilities.GeneralLighting {

    public static String name = "light";

    // Mutual properties
    private final byte mInstanceCode = (byte) 0x02;
//    private final byte[] mOperationStatus = {(byte) 0x31};                              // EPC = 0x80
    private final byte[] mInsallationLocation = {(byte) 0x00};                          // EPC = 0x81
    private final byte[] mFaultStatus = {(byte) 0x42};                                  // EPC = 0x88
    private final byte[] mManufacturerCode = {0, 0, 0};                                 // EPC = 0x8A

    // Private properties
    private final byte[] mLightingModeSetting = {(byte) 0x00};

    private final GPIOManager GPIO = GPIOManager.getInstance();
    private final Port lightPort = GPIOManager.Port.GPIO0;

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
        try {
            PinState pinState = GPIO.getState(lightPort);
            Operation operation = (pinState == PinState.ON) ? ON : OFF;
            return new byte[]{operation.value};
        } catch (NumberFormatException | IOException ex) {
            DebugLog.log(ex);
            return new byte[]{OFF.value};
        }
    }

    @Override
    protected boolean setOperationStatus(byte[] edt) {
        try {
            PinState pinState = PinState.from(edt);
            GPIO.setState(lightPort, pinState);
            // Announcement at status change
            inform().reqInformOperationStatus().send();
        } catch (IOException ex) {
            DebugLog.log(ex);
        }
        return true;
    }

    @Override
    protected boolean setInstallationLocation(byte[] bytes) {
        try {
            // Announcement at status change
            mInsallationLocation[0] = bytes[0];
            inform().reqInformOperationStatus().send();
        } catch (IOException ex) {
            DebugLog.log(ex);
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
    protected boolean setLightingModeSetting(byte[] bytes) {
        mLightingModeSetting[0] = bytes[0];
        return true;
    }

    @Override
    protected byte[] getLightingModeSetting() {
        return mLightingModeSetting;
    }

    public void switchStatus() {
        MyDeviceObject.Operation temp = Operation.from(getOperationStatus()[0]) == ON ? OFF : ON;
        setOperationStatus(new byte[]{temp.value});
    }
}
