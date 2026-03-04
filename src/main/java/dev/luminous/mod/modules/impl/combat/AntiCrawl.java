/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package dev.luminous.mod.modules.impl.combat;

import dev.luminous.Alien;
import dev.luminous.api.events.eventbus.EventListener;
import dev.luminous.api.events.impl.UpdateEvent;
import dev.luminous.api.utils.world.BlockPosX;
import dev.luminous.api.utils.world.BlockUtil;
import dev.luminous.mod.modules.Module;
import dev.luminous.mod.modules.impl.player.SpeedMine;
import dev.luminous.mod.modules.settings.impl.BooleanSetting;
import dev.luminous.mod.modules.settings.impl.EnumSetting;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class AntiCrawl
extends Module {
    public static AntiCrawl INSTANCE;
    final double[] xzOffset = new double[]{0.0, 0.3, -0.3};
    private final EnumSetting<While> whileSetting = this.add(new EnumSetting<While>("While", While.Crawling));
    private final BooleanSetting web = this.add(new BooleanSetting("Web", true));
    public boolean work = false;

    public AntiCrawl() {
        super("AntiCrawl", Module.Category.Combat);
        this.setChinese("\u53cd\u8db4\u4e0b");
        INSTANCE = this;
    }

    @EventListener
    public void onUpdate(UpdateEvent event) {
        this.work = false;
        if (!AntiCrawl.mc.field_1724.method_6128() && (this.whileSetting.is(While.Always) && BlockUtil.getBlock(AntiCrawl.mc.field_1724.method_24515()) != class_2246.field_9987 || AntiCrawl.mc.field_1724.method_20448() || this.whileSetting.is(While.Mining) && Alien.BREAK.isMining(AntiCrawl.mc.field_1724.method_24515()))) {
            for (double offset : this.xzOffset) {
                for (double offset2 : this.xzOffset) {
                    BlockPosX web;
                    BlockPosX pos = new BlockPosX(AntiCrawl.mc.field_1724.method_23317() + offset, AntiCrawl.mc.field_1724.method_23318() + 1.2, AntiCrawl.mc.field_1724.method_23321() + offset2);
                    if (this.canBreak(pos)) {
                        SpeedMine.INSTANCE.mine(pos);
                        this.work = true;
                        return;
                    }
                    if (!this.web.getValue() || AntiCrawl.mc.field_1687.method_8320((class_2338)(web = new BlockPosX(AntiCrawl.mc.field_1724.method_23317() + offset, AntiCrawl.mc.field_1724.method_23318(), AntiCrawl.mc.field_1724.method_23321() + offset2))).method_26204() != class_2246.field_10343 || !this.canBreak(web)) continue;
                    SpeedMine.INSTANCE.mine(web);
                    this.work = true;
                    return;
                }
            }
        }
    }

    private boolean canBreak(class_2338 pos) {
        return (BlockUtil.getClickSideStrict(pos) != null || pos.equals((Object)SpeedMine.getBreakPos())) && !SpeedMine.unbreakable(pos) && !AntiCrawl.mc.field_1687.method_22347(pos);
    }

    private static enum While {
        Crawling,
        Mining,
        Always;

    }
}

