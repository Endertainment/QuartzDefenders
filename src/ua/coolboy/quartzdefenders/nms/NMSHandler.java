package ua.coolboy.quartzdefenders.nms;

import org.bukkit.Bukkit;

public class NMSHandler {
    
    private static NMSAbstract nms;
    
    public NMSHandler() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch(version) {
            case "v1_12_R1":
                nms = new NMS1_12_R1(version);
                break;
            case "v1_13_R1":
                nms = new NMS1_13_R1(version);
                break;
            default:
                nms = new NMSAbstract();
        }
    }
    
    public NMSAbstract getNMS() {
        return nms;
    }

}
