package ua.coolboy.quartzdefenders.nms;

import org.bukkit.Bukkit;

public abstract class NMSHandler {
    
    private static NMSAbstract nms;
    
    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        switch(version) {
            case "v1_12_R1":
                nms = new NMS_1_12_R1(version);
                break;
            default:
                nms = new NMSAbstract();
        }
    }
    
    public static NMSAbstract getNMS() {
        return nms;
    }

}
