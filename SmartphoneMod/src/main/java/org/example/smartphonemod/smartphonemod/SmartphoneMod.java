package org.example.smartphonemod.smartphonemod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(SmartphoneMod.MODID)
public class SmartphoneMod {
    public static final String MODID = "smartphonemod";

    public SmartphoneMod(IEventBus modEventBus, ModContainer modContainer) {
        // 최소 버전이라 여기서는 별도 등록 없음
    }
}