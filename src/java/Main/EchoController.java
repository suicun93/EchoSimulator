/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Common.Config;
import Common.Convert;
import Model.MyBattery;
import Model.MyElectricVehicle;
import Model.MyLight;
import Model.MySolar;
import com.sonycsl.echo.Echo;
import com.sonycsl.echo.EchoProperty;
import com.sonycsl.echo.eoj.EchoObject;
import com.sonycsl.echo.eoj.device.DeviceObject;
import com.sonycsl.echo.eoj.device.housingfacilities.ElectricVehicle;
import com.sonycsl.echo.eoj.profile.NodeProfile;
import com.sonycsl.echo.node.EchoNode;
import com.sonycsl.echo.processing.defaults.DefaultNodeProfile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

/**
 *
 * @author hoang-trung-duc
 */
public class EchoController {

    private static final String CONFIG_FILE = "Config";

    // Init nodeProfile, controller, ev, battery, ...
    private static final DefaultNodeProfile NODE_PROFILE = new DefaultNodeProfile();
    public static final MyElectricVehicle EV = new MyElectricVehicle();
    public static final MyBattery BATTERY = new MyBattery();
    public static final MySolar SOLAR = new MySolar();
    public static final MyLight LIGHT = new MyLight();

    private static void saveConfig() {
        try {
            StringBuilder listConfig = new StringBuilder("");
            for (DeviceObject deviceObject : listDevice()) {
                if (deviceObject instanceof MyBattery) {
                    listConfig.append(MyBattery.name).append(",");
                }
                if (deviceObject instanceof MyElectricVehicle) {
                    listConfig.append(MyElectricVehicle.name).append(",");
                }
                if (deviceObject instanceof MySolar) {
                    listConfig.append(MySolar.name).append(",");
                }
                if (deviceObject instanceof MyLight) {
                    listConfig.append(MyLight.name).append(",");
                }
            }
            if (listConfig.length() != 0) {
                listConfig.deleteCharAt(listConfig.length() - 1); // delete ","
            }
            Config.save(CONFIG_FILE, listConfig.toString());
        } catch (Exception ex) {
            System.out.println("Save Config failed: " + ex.getMessage());
        }
    }

//    public static void loadConfig() {
//        try {
//            String content = Config.load(CONFIG_FILE);
//            System.out.println("Config: " + content);
//            String[] devices = content.split("\\,");
//            for (String device : devices) {
//                if (device.equalsIgnoreCase(MyBattery.name)) {
//                    listDevice.add(BATTERY);
//                }
//                if (device.equalsIgnoreCase(MyElectricVehicle.name)) {
//                    listDevice.add(EV);
//                }
//                if (device.equalsIgnoreCase(MySolar.name)) {
//                    listDevice.add(SOLAR);
//                }
//                if (device.equalsIgnoreCase(MyLight.name)) {
//                    listDevice.add(LIGHT);
//                }
//            }
//
//            // Start devices
//            try {
//                addEvent();  // -> Log to debug.
//                Echo.start(NODE_PROFILE, listDevice());
//            } catch (IOException e) {
//                listDevice.clear();
//                throw e;
//            }
//        } catch (Exception e) {
//            System.out.println("Load config failed: " + e.getMessage());
//        }
//    }

    public static void start(DeviceObject device) throws IOException {
        // notify to others
        if (listDevice().contains(device)) {
            return;
        }
        try {
            // Start Node
            if (!Echo.isStarted()) {
                addEvent();  // -> Log to debug.
                Echo.start(NODE_PROFILE, new DeviceObject[]{});
            }
            Echo.getSelfNode().addDevice(device);
            NodeProfile.informG().reqInformInstanceListNotification().send();
            saveConfig();
        } catch (IOException e) {
            throw new IOException("Start: " + e.getMessage());
        }
    }

    public static void stop(DeviceObject device) throws IOException {
        // Stop Node
        try {
            if (Echo.isStarted()) {
                if (device instanceof MyBattery) {
                    ((MyBattery) device).stop();
                }
                if (device instanceof MyElectricVehicle) {
                    ((MyElectricVehicle) device).stop();
                }
                if (device instanceof MySolar) {
                    ((MySolar) device).stop();
                }
                Echo.getSelfNode().removeDevice(device);
                device.removeNode();
                NodeProfile.informG().reqInformInstanceListNotification().send();
                saveConfig();
            }
        } catch (IOException e) {
            throw new IOException("Stop: " + e.getMessage());
        }
    }

    public static boolean contains(String device) {
        if (device.equalsIgnoreCase(MyBattery.name)) {
            return listDevice().contains(BATTERY);
        }
        if (device.equalsIgnoreCase(MyElectricVehicle.name)) {
            return listDevice().contains(EV);
        }
        if (device.equalsIgnoreCase(MySolar.name)) {
            return listDevice().contains(SOLAR);
        }
        if (device.equalsIgnoreCase(MyLight.name)) {
            return listDevice().contains(LIGHT);
        }
        return false;
    }

    // Device Object List to array
    private static ArrayList<DeviceObject> listDevice() {
        ArrayList<DeviceObject> listDevice = new ArrayList<>();
        if (Echo.getSelfNode() != null) {
            listDevice.addAll(Arrays.asList(Echo.getSelfNode().getDevices()));
        }
        return listDevice;
    }

    // Add Event
    private static void addEvent() {
        Echo.addEventListener(new Echo.EventListener() {

            // Found new Node.
            @Override
            public void onNewNode(EchoNode node) {
                super.onNewNode(node);
                // Found new Node.
                System.out.println("Node = " + node.getNodeProfile());
                System.out.println("--------");
            }

            // Found new DeviceObject.
            @Override
            public void onNewDeviceObject(DeviceObject device) {
                super.onNewDeviceObject(device);
                System.out.println("\t   Device = " + device);
                // Set Receiver for getter setter.
                device.setReceiver(new ElectricVehicle.Receiver() {
                    // Getter Receiver 
                    @Override
                    protected boolean onGetProperty(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        if (!success) {
                            System.out.println("onGetProperty Failed: EPC = " + Convert.byteToHex(property.epc));
                        } else {
                            System.out.println("onGetProperty Success:  EPC = " + Convert.byteToHex(property.epc) + " Data: ");
                            Convert.printHexArray(property.edt);
                        }
                        return super.onGetProperty(eoj, tid, esv, property, success);
                    }

                    // Setter Receiver
                    @Override
                    protected boolean onSetProperty(EchoObject eoj, short tid, byte esv, EchoProperty property, boolean success) {
                        if (!success) {
                            System.out.println("onSetProperty Failed: EPC = " + Convert.byteToHex(property.epc));
                        } else {
                            System.out.println("onSetProperty Success:  EPC = " + Convert.byteToHex(property.epc));
                        }
                        return super.onSetProperty(eoj, tid, esv, property, success);
                    }
                });

            }
        }); // No more events.
    }
}
