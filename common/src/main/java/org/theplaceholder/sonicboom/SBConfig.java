package org.theplaceholder.sonicboom;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import static org.theplaceholder.sonicboom.SonicBoom.*;

@Config(name = "sonicboom")
public class SBConfig implements ConfigData {
    public float explosionSpeed = 35f;
    public float explosionThresholdSpeed = 30f;
    
    public static void register(){
        AutoConfig.register(SBConfig.class, GsonConfigSerializer::new);
        SBConfig config = AutoConfig.getConfigHolder(SBConfig.class).getConfig();
        EXPLOSION_SPEED = config.explosionSpeed;
        EXPLOSION_THRESHOLD_SPEED = config.explosionThresholdSpeed;
    }
}
