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
import com.sonycsl.echo.eoj.device.housingfacilities.Battery;
import com.sonycsl.echo.eoj.device.housingfacilities.ElectricVehicle;
import com.sonycsl.echo.eoj.device.housingfacilities.GeneralLighting;
import com.sonycsl.echo.eoj.device.housingfacilities.HouseholdSolarPowerGeneration;
import com.sonycsl.echo.eoj.device.managementoperation.Controller;
import com.sonycsl.echo.node.EchoNode;
import com.sonycsl.echo.processing.defaults.DefaultNodeProfile;
import java.io.IOException;
import java.util.ArrayList;

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

    private static ArrayList<DeviceObject> listDevice = new ArrayList<>();

    private static void saveConfig() {
        try {
            StringBuilder listConfig = new StringBuilder("");
            for (DeviceObject deviceObject : listDevice) {
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
        if (listDevice.contains(device)) {
            return;
        }
        try {
            // Start Node
            if (Echo.isStarted()) {
                listDevice.add(device);
                Echo.clear();
            } else {
                listDevice = new ArrayList<>();
                listDevice.add(device);
            }
            addEvent();  // -> Log to debug.
            Echo.start(NODE_PROFILE, listDevice());
            saveConfig();
        } catch (IOException e) {
            listDevice.remove(device);
            throw e;
        }
    }

    public static void stop(DeviceObject device) throws IOException {
        // Start Node
        if (Echo.isStarted()) {
            try {
                if (device instanceof MyBattery) {
                    ((MyBattery) device).stop();
                }
                if (device instanceof MyElectricVehicle) {
                    ((MyElectricVehicle) device).stop();
                }
                if (device instanceof MySolar) {
                    ((MySolar) device).stop();
                }
                listDevice.remove(device);
                Echo.clear();
                if (!listDevice.isEmpty()) {
                    addEvent();  // -> Log to debug.
                    Echo.start(NODE_PROFILE, listDevice());
                }
                saveConfig();
            } catch (IOException e) {
                listDevice.add(device);
                throw e;
            }
        }
    }

    public static boolean contains(String device) {
        if (device.equalsIgnoreCase(MyBattery.name)) {
            return listDevice.contains(BATTERY);
        }
        if (device.equalsIgnoreCase(MyElectricVehicle.name)) {
            return listDevice.contains(EV);
        }
        if (device.equalsIgnoreCase(MySolar.name)) {
            return listDevice.contains(SOLAR);
        }
        if (device.equalsIgnoreCase(MyLight.name)) {
            return listDevice.contains(LIGHT);
        }
        return false;
    }

    // Device Detection
    private static String deviceDetect(DeviceObject device) {
        // Device detection
        if (device instanceof Controller) {
            return "Controller";
        }
        if (device instanceof MyBattery) {
            return "My Battery";
        }
        if (device instanceof MyElectricVehicle) {
            return "My Electric Vehicle";
        }
        if (device instanceof MySolar) {
            return "My Solar";
        }
        if (device instanceof MyLight) {
            return "My Light";
        }
        if (device instanceof ElectricVehicle) {
            return "Electric Vehicle";
        }
        if (device instanceof Battery) {
            return "Battery";
        }
        if (device instanceof HouseholdSolarPowerGeneration) {
            return "Solar";
        }
        if (device instanceof GeneralLighting) {
            return "Light";
        }
        return "Unknown";
    }

    // Device Object List to array
    private static DeviceObject[] listDevice() {
        DeviceObject[] deviceObjects = new DeviceObject[listDevice.size()];
        return listDevice.toArray(deviceObjects);
    }

    // Add Event
    private static void addEvent() {
        Echo.addEventListener(new Echo.EventListener() {

            // Found new Node.
            @Override
            public void onNewNode(EchoNode node) {
                super.onNewNode(node);
                // Found new Node.
                System.out.println("New Node found.");
                System.out.println("Node id = " + node.getAddress().getHostAddress());
                System.out.println("Node = " + node.getNodeProfile());
                System.out.println("--------\n");
            }

            // Found new DeviceObject.
            @Override
            public void onNewDeviceObject(DeviceObject device) {
                super.onNewDeviceObject(device);
                System.out.println("\t   New " + deviceDetect(device) + " found.");
                System.out.println("\t   Device = " + device);
                System.out.println("\t   ----\n\n");
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

//                try {
//                    System.out.println("Property Map: ");
//                    device.get().reqGetProperty(device.EPC_GET_PROPERTY_MAP).send();
//                } catch (IOException ex) {
//                    Logger.getLogger(EchoController.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }
        }); // No more events.
    }

}
