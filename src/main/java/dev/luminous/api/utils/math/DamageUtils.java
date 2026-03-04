/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntArrayMap
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  it.unimi.dsi.fastutil.objects.Object2IntMaps
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  net.minecraft.entity.DamageUtil
 *  net.minecraft.entity.damage.DamageSource
 *  net.minecraft.entity.effect.StatusEffectInstance
 *  net.minecraft.entity.effect.StatusEffects
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EquipmentSlot
 *  net.minecraft.entity.LivingEntity
 *  net.minecraft.entity.attribute.EntityAttributeModifier
 *  net.minecraft.entity.attribute.EntityAttributeInstance
 *  net.minecraft.entity.player.PlayerEntity
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.Items
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.enchantment.Enchantments
 *  net.minecraft.world.BlockView
 *  net.minecraft.world.GameMode
 *  net.minecraft.block.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Position
 *  net.minecraft.util.math.Box
 *  net.minecraft.util.hit.HitResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.block.BlockState
 *  net.minecraft.world.Heightmap$Type
 *  net.minecraft.registry.tag.EntityTypeTags
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.RaycastContext
 *  net.minecraft.world.RaycastContext$FluidHandling
 *  net.minecraft.world.RaycastContext$ShapeType
 *  net.minecraft.util.hit.BlockHitResult
 *  net.minecraft.entity.attribute.EntityAttributes
 *  net.minecraft.registry.RegistryKey
 *  net.minecraft.client.network.PlayerListEntry
 *  net.minecraft.registry.entry.RegistryEntry
 *  net.minecraft.registry.tag.DamageTypeTags
 *  net.minecraft.component.type.AttributeModifiersComponent
 *  net.minecraft.component.type.ItemEnchantmentsComponent
 *  net.minecraft.component.DataComponentTypes
 *  net.minecraft.item.MaceItem
 *  org.apache.commons.lang3.mutable.MutableInt
 *  org.joml.Math
 */
package dev.luminous.api.utils.math;

import dev.luminous.Alien;
import dev.luminous.api.utils.Wrapper;
import dev.luminous.api.utils.combat.CombatUtil;
import dev.luminous.core.impl.PlayerManager;
import dev.luminous.mod.modules.impl.client.ClientSetting;
import dev.luminous.mod.modules.impl.combat.Criticals;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.function.BiFunction;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameMode;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Box;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.world.Heightmap;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.MaceItem;
import org.apache.commons.lang3.mutable.MutableInt;

public class DamageUtils {
    public static final RaycastFactory HIT_FACTORY = (context, blockPos) -> {
        class_2680 blockState = Wrapper.mc.field_1687.method_8320(blockPos);
        return blockState.method_26204().method_9520() < 600.0f ? null : blockState.method_26220((class_1922)Wrapper.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
    };

    public static float calculateDamage(class_2338 pos, class_1309 entity) {
        return DamageUtils.explosionDamage(entity, null, new class_243((double)pos.method_10263() + 0.5, (double)(pos.method_10264() + 1), (double)pos.method_10260() + 0.5), 12.0f);
    }

    public static float calculateDamage(class_243 pos, class_1309 entity) {
        return DamageUtils.explosionDamage(entity, null, pos, 12.0f);
    }

    public static float explosionDamage(class_1309 target, class_243 targetPos, class_238 targetBox, class_243 explosionPos, float power, RaycastFactory raycastFactory) {
        double modDistance = DamageUtils.distance(targetPos.field_1352, targetPos.field_1351, targetPos.field_1350, explosionPos.field_1352, explosionPos.field_1351, explosionPos.field_1350);
        if (modDistance > (double)power) {
            return 0.0f;
        }
        double exposure = DamageUtils.getExposure(explosionPos, targetBox, raycastFactory);
        double impact = (1.0 - modDistance / (double)power) * exposure;
        float damage = (int)((impact * impact + impact) / 2.0 * 7.0 * 12.0 + 1.0);
        return DamageUtils.calculateReductionsExplosion(damage, target, Wrapper.mc.field_1687.method_48963().method_48807(null));
    }

    public static float anchorDamage(class_1309 target, class_1309 predict, class_243 anchor) {
        return DamageUtils.overridingExplosionDamage(target, predict, anchor, 10.0f, class_2338.method_49638((class_2374)anchor), class_2246.field_10124.method_9564());
    }

    public static float overridingExplosionDamage(class_1309 target, class_1309 predict, class_243 explosionPos, float power, class_2338 overridePos, class_2680 overrideState) {
        return DamageUtils.explosionDamage(target, predict, explosionPos, power, DamageUtils.getOverridingHitFactory(overridePos, overrideState));
    }

    private static float explosionDamage(class_1309 target, class_1309 predict, class_243 explosionPos, float power, RaycastFactory raycastFactory) {
        class_1657 player;
        if (target == null) {
            return 0.0f;
        }
        return target instanceof class_1657 && DamageUtils.getGameMode(player = (class_1657)target) == class_1934.field_9220 ? 0.0f : DamageUtils.explosionDamage(target, predict != null ? predict.method_19538() : target.method_19538(), predict != null ? predict.method_5829() : target.method_5829(), explosionPos, power, raycastFactory);
    }

    public static float explosionDamage(class_1309 target, class_1309 predict, class_243 explosionPos, float power) {
        class_1657 player;
        if (target == null) {
            return 0.0f;
        }
        return target instanceof class_1657 && DamageUtils.getGameMode(player = (class_1657)target) == class_1934.field_9220 ? 0.0f : DamageUtils.explosionDamage(target, predict != null ? predict.method_19538() : target.method_19538(), predict != null ? predict.method_5829() : target.method_5829(), explosionPos, power, HIT_FACTORY);
    }

    public static RaycastFactory getOverridingHitFactory(class_2338 overridePos, class_2680 overrideState) {
        return (context, blockPos) -> {
            class_2680 blockState;
            if (blockPos.equals((Object)overridePos)) {
                blockState = overrideState;
            } else {
                blockState = Wrapper.mc.field_1687.method_8320(blockPos);
                if (blockState.method_26204().method_9520() < 600.0f) {
                    return null;
                }
            }
            return blockState.method_26220((class_1922)Wrapper.mc.field_1687, blockPos).method_1092(context.start(), context.end(), blockPos);
        };
    }

    public static float getAttackDamage(class_1309 attacker, class_1309 target) {
        class_1282 class_12822;
        float itemDamage = (float)attacker.method_45325(class_5134.field_23721);
        if (attacker instanceof class_1657) {
            class_1657 player = (class_1657)attacker;
            class_12822 = Wrapper.mc.field_1687.method_48963().method_48802(player);
        } else {
            class_12822 = Wrapper.mc.field_1687.method_48963().method_48812(attacker);
        }
        class_1282 damageSource = class_12822;
        class_1293 effect = attacker.method_6112(class_1294.field_5910);
        if (effect != null) {
            itemDamage += 3.0f * (float)(effect.method_5578() + 1);
        }
        float damage = DamageUtils.modifyAttackDamage(attacker, target, attacker.method_59958(), damageSource, itemDamage);
        return DamageUtils.calculateReductions(damage, target, damageSource);
    }

    public static float getAttackDamage(class_1309 attacker, class_1309 target, class_1799 weapon) {
        class_1282 class_12822;
        class_1324 original = attacker.method_5996(class_5134.field_23721);
        class_1324 copy = new class_1324(class_5134.field_23721, o -> {});
        copy.method_6192(original.method_6201());
        for (class_1322 modifier2 : original.method_6195()) {
            copy.method_26835(modifier2);
        }
        copy.method_6200(class_1792.field_8006);
        class_9285 attributeModifiers = (class_9285)weapon.method_57824(class_9334.field_49636);
        if (attributeModifiers != null) {
            attributeModifiers.method_57482(class_1304.field_6173, (entry, modifier) -> {
                if (entry == class_5134.field_23721) {
                    copy.method_55696(modifier);
                }
            });
        }
        float itemDamage = (float)copy.method_6194();
        if (attacker instanceof class_1657) {
            class_1657 player = (class_1657)attacker;
            class_12822 = Wrapper.mc.field_1687.method_48963().method_48802(player);
        } else {
            class_12822 = Wrapper.mc.field_1687.method_48963().method_48812(attacker);
        }
        class_1282 damageSource = class_12822;
        float damage = DamageUtils.modifyAttackDamage(attacker, target, weapon, damageSource, itemDamage);
        return DamageUtils.calculateReductions(damage, target, damageSource);
    }

    private static float modifyAttackDamage(class_1309 attacker, class_1309 target, class_1799 weapon, class_1282 damageSource, float damage) {
        int smite;
        int impaling;
        int baneOfArthropods;
        Object2IntOpenHashMap enchantments = new Object2IntOpenHashMap();
        DamageUtils.getEnchantments(weapon, (Object2IntMap<class_6880<class_1887>>)enchantments);
        float enchantDamage = 0.0f;
        int sharpness = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9118);
        if (sharpness > 0) {
            enchantDamage += 1.0f + 0.5f * (float)(sharpness - 1);
        }
        if ((baneOfArthropods = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9112)) > 0 && target.method_5864().method_20210(class_3483.field_48285)) {
            enchantDamage += 2.5f * (float)baneOfArthropods;
        }
        if ((impaling = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9106)) > 0 && target.method_5864().method_20210(class_3483.field_48284)) {
            enchantDamage += 2.5f * (float)impaling;
        }
        if ((smite = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9123)) > 0 && target.method_5864().method_20210(class_3483.field_49931)) {
            enchantDamage += 2.5f * (float)smite;
        }
        if (attacker instanceof class_1657) {
            class_9362 item;
            float bonusDamage;
            class_1657 playerEntity = (class_1657)attacker;
            float charge = playerEntity.method_7261(0.5f);
            damage *= 0.2f + charge * charge * 0.8f;
            enchantDamage *= charge;
            class_1792 class_17922 = weapon.method_7909();
            if (class_17922 instanceof class_9362 && (bonusDamage = (item = (class_9362)class_17922).method_58403((class_1297)target, damage, damageSource)) > 0.0f) {
                int density = DamageUtils.getEnchantmentLevel(weapon, (class_5321<class_1887>)class_1893.field_50157);
                if (density > 0) {
                    bonusDamage += 0.5f * attacker.field_6017;
                }
                damage += bonusDamage;
            }
            if (!(!(charge > 0.9f) || !(attacker.field_6017 > 0.0f) && (attacker != Wrapper.mc.field_1724 || !Criticals.INSTANCE.isOn() || Criticals.INSTANCE.mode.is(Criticals.Mode.Ground) || !Wrapper.mc.field_1724.method_24828() && Criticals.INSTANCE.onlyGround.getValue()) || attacker.method_24828() && (attacker != Wrapper.mc.field_1724 || !Criticals.INSTANCE.isOn() || Criticals.INSTANCE.mode.is(Criticals.Mode.Ground)) || attacker.method_6101() || attacker.method_5799() || attacker.method_6059(class_1294.field_5919) || attacker.method_5765())) {
                damage *= 1.5f;
            }
        }
        return damage + enchantDamage;
    }

    public static float fallDamage(class_1309 entity) {
        if (entity instanceof class_1657) {
            class_1657 player = (class_1657)entity;
            if (player.method_31549().field_7479) {
                return 0.0f;
            }
        }
        if (!entity.method_6059(class_1294.field_5906) && !entity.method_6059(class_1294.field_5902)) {
            int surface = Wrapper.mc.field_1687.method_8500(entity.method_24515()).method_12032(class_2902.class_2903.field_13197).method_12603(entity.method_31477() & 0xF, entity.method_31479() & 0xF);
            if (entity.method_31478() >= surface) {
                return DamageUtils.fallDamageReductions(entity, surface);
            }
            class_3965 raycastResult = Wrapper.mc.field_1687.method_17742(new class_3959(entity.method_19538(), new class_243(entity.method_23317(), (double)Wrapper.mc.field_1687.method_31607(), entity.method_23321()), class_3959.class_3960.field_17558, class_3959.class_242.field_36338, (class_1297)entity));
            return raycastResult.method_17783() == class_239.class_240.field_1333 ? 0.0f : DamageUtils.fallDamageReductions(entity, raycastResult.method_17777().method_10264());
        }
        return 0.0f;
    }

    private static float fallDamageReductions(class_1309 entity, int surface) {
        int fallHeight = (int)(entity.method_23318() - (double)surface + (double)entity.field_6017 - 3.0);
        class_1293 jumpBoostInstance = entity.method_6112(class_1294.field_5913);
        if (jumpBoostInstance != null) {
            fallHeight -= jumpBoostInstance.method_5578() + 1;
        }
        return DamageUtils.calculateReductions(fallHeight, entity, Wrapper.mc.field_1687.method_48963().method_48827());
    }

    public static float calculateReductionsExplosion(float damage, class_1309 entity, class_1282 damageSource) {
        if (damageSource.method_5514()) {
            switch (Wrapper.mc.field_1687.method_8407()) {
                case field_5805: {
                    damage = Math.min(damage / 2.0f + 1.0f, damage);
                    break;
                }
                case field_5807: {
                    damage *= 1.5f;
                }
            }
        }
        damage = class_1280.method_5496((class_1309)entity, (float)damage, (class_1282)damageSource, (float)DamageUtils.getArmor(entity), (float)((float)DamageUtils.getGENERIC_ARMOR_TOUGHNESS(entity)));
        damage = DamageUtils.resistanceReduction(entity, damage);
        damage = class_1280.method_5497((float)damage, (float)DamageUtils.getProtectionAmount(entity.method_5661()));
        return Math.max(damage, 0.0f);
    }

    public static float calculateReductions(float damage, class_1309 entity, class_1282 damageSource) {
        if (damageSource.method_5514()) {
            switch (Wrapper.mc.field_1687.method_8407()) {
                case field_5805: {
                    damage = Math.min(damage / 2.0f + 1.0f, damage);
                    break;
                }
                case field_5807: {
                    damage *= 1.5f;
                }
            }
        }
        damage = class_1280.method_5496((class_1309)entity, (float)damage, (class_1282)damageSource, (float)DamageUtils.getArmor(entity), (float)((float)DamageUtils.getGENERIC_ARMOR_TOUGHNESS(entity)));
        damage = DamageUtils.resistanceReduction(entity, damage);
        damage = DamageUtils.protectionReduction(entity, damage, damageSource);
        return Math.max(damage, 0.0f);
    }

    public static double getGENERIC_ARMOR_TOUGHNESS(class_1309 entity) {
        class_1657 player;
        PlayerManager.EntityAttribute entityAttribute;
        if (entity instanceof class_1657 && (entityAttribute = Alien.PLAYER.map.get(player = (class_1657)entity)) != null) {
            return entityAttribute.toughness();
        }
        return entity.method_45325(class_5134.field_23725);
    }

    private static float getArmor(class_1309 entity) {
        class_1657 player;
        PlayerManager.EntityAttribute entityAttribute;
        if (entity instanceof class_1657 && (entityAttribute = Alien.PLAYER.map.get(player = (class_1657)entity)) != null) {
            return entityAttribute.armor();
        }
        return (float)Math.floor(entity.method_45325(class_5134.field_23724));
    }

    private static float protectionReduction(class_1309 player, float damage, class_1282 source) {
        if (source.method_48789(class_8103.field_42242)) {
            return damage;
        }
        int damageProtection = 0;
        for (class_1799 stack : player.method_56674()) {
            int featherFalling;
            int projectileProtection;
            int blastProtection;
            int fireProtection;
            Object2IntOpenHashMap enchantments = new Object2IntOpenHashMap();
            DamageUtils.getEnchantments(stack, (Object2IntMap<class_6880<class_1887>>)enchantments);
            int protection = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9111);
            if (protection > 0) {
                damageProtection += protection;
            }
            if ((fireProtection = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9095)) > 0 && source.method_48789(class_8103.field_42246)) {
                damageProtection += 2 * fireProtection;
            }
            if ((blastProtection = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9107)) > 0 && source.method_48789(class_8103.field_42249)) {
                damageProtection += 2 * blastProtection;
            }
            if ((projectileProtection = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9096)) > 0 && source.method_48789(class_8103.field_42247)) {
                damageProtection += 2 * projectileProtection;
            }
            if ((featherFalling = DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)enchantments, (class_5321<class_1887>)class_1893.field_9129)) <= 0 || !source.method_48789(class_8103.field_42250)) continue;
            damageProtection += 3 * featherFalling;
        }
        return class_1280.method_5497((float)damage, (float)damageProtection);
    }

    public static int getProtectionAmount(Iterable<class_1799> equipment) {
        MutableInt mutableInt = new MutableInt();
        equipment.forEach(i -> mutableInt.add(DamageUtils.getProtectionAmount(i)));
        return mutableInt.intValue();
    }

    public static int getProtectionAmount(class_1799 stack) {
        int modifierBlast = class_1890.method_8225((class_6880)((class_6880)Wrapper.mc.field_1687.method_30349().method_46762(class_1893.field_9107.method_58273()).method_46746(class_1893.field_9107).get()), (class_1799)stack);
        int modifier = class_1890.method_8225((class_6880)((class_6880)Wrapper.mc.field_1687.method_30349().method_46762(class_1893.field_9111.method_58273()).method_46746(class_1893.field_9111).get()), (class_1799)stack);
        return modifierBlast * 2 + modifier;
    }

    private static float resistanceReduction(class_1309 player, float damage) {
        class_1293 resistance = player.method_6112(class_1294.field_5907);
        if (resistance != null) {
            int lvl = resistance.method_5578() + 1;
            damage *= 1.0f - (float)lvl * 0.2f;
        }
        return Math.max(damage, 0.0f);
    }

    private static float getExposure(class_243 source, class_238 box, RaycastFactory raycastFactory) {
        if (ClientSetting.INSTANCE.optimizedCalc.getValue()) {
            int miss = 0;
            int hit = 0;
            for (int k = 0; k <= 1; ++k) {
                for (int l = 0; l <= 1; ++l) {
                    for (int m = 0; m <= 1; ++m) {
                        double p;
                        double o;
                        double n = class_3532.method_16436((double)k, (double)box.field_1323, (double)box.field_1320);
                        class_243 vec3d = new class_243(n, o = class_3532.method_16436((double)l, (double)box.field_1322, (double)box.field_1325), p = class_3532.method_16436((double)m, (double)box.field_1321, (double)box.field_1324));
                        if (DamageUtils.raycast(vec3d, source, CombatUtil.terrainIgnore) == class_239.class_240.field_1333) {
                            ++miss;
                        }
                        ++hit;
                    }
                }
            }
            return (float)miss / (float)hit;
        }
        double xDiff = box.field_1320 - box.field_1323;
        double yDiff = box.field_1325 - box.field_1322;
        double zDiff = box.field_1324 - box.field_1321;
        double xStep = 1.0 / (xDiff * 2.0 + 1.0);
        double yStep = 1.0 / (yDiff * 2.0 + 1.0);
        double zStep = 1.0 / (zDiff * 2.0 + 1.0);
        if (xStep > 0.0 && yStep > 0.0 && zStep > 0.0) {
            int misses = 0;
            int hits = 0;
            double xOffset = (1.0 - Math.floor(1.0 / xStep) * xStep) * 0.5;
            double zOffset = (1.0 - Math.floor(1.0 / zStep) * zStep) * 0.5;
            xStep *= xDiff;
            yStep *= yDiff;
            zStep *= zDiff;
            double startX = box.field_1323 + xOffset;
            double startY = box.field_1322;
            double startZ = box.field_1321 + zOffset;
            double endX = box.field_1320 + xOffset;
            double endY = box.field_1325;
            double endZ = box.field_1324 + zOffset;
            for (double x = startX; x <= endX; x += xStep) {
                for (double y = startY; y <= endY; y += yStep) {
                    for (double z = startZ; z <= endZ; z += zStep) {
                        class_243 position = new class_243(x, y, z);
                        if (DamageUtils.raycast(new ExposureRaycastContext(position, source), raycastFactory) == null) {
                            ++misses;
                        }
                        ++hits;
                    }
                }
            }
            return (float)misses / (float)hits;
        }
        return 0.0f;
    }

    public static class_239.class_240 raycast(class_243 start, class_243 end, boolean ignoreTerrain) {
        return (class_239.class_240)class_1922.method_17744((class_243)start, (class_243)end, null, (innerContext, blockPos) -> {
            class_2680 blockState = Wrapper.mc.field_1687.method_8320(blockPos);
            if (blockState.method_26204().method_9520() < 600.0f && ignoreTerrain) {
                return null;
            }
            class_3965 hitResult = blockState.method_26220((class_1922)Wrapper.mc.field_1687, blockPos).method_1092(start, end, blockPos);
            return hitResult == null ? null : hitResult.method_17783();
        }, innerContext -> class_239.class_240.field_1333);
    }

    public static class_3965 raycast(ExposureRaycastContext context, RaycastFactory raycastFactory) {
        return (class_3965)class_1922.method_17744((class_243)context.start(), (class_243)context.end(), (Object)context, (BiFunction)raycastFactory, ctx -> null);
    }

    public static int getEnchantmentLevel(class_1799 itemStack, class_5321<class_1887> enchantment) {
        if (itemStack.method_7960()) {
            return 0;
        }
        Object2IntArrayMap itemEnchantments = new Object2IntArrayMap();
        DamageUtils.getEnchantments(itemStack, (Object2IntMap<class_6880<class_1887>>)itemEnchantments);
        return DamageUtils.getEnchantmentLevel((Object2IntMap<class_6880<class_1887>>)itemEnchantments, enchantment);
    }

    public static int getEnchantmentLevel(Object2IntMap<class_6880<class_1887>> itemEnchantments, class_5321<class_1887> enchantment) {
        for (Object2IntMap.Entry entry : Object2IntMaps.fastIterable(itemEnchantments)) {
            if (!((class_6880)entry.getKey()).method_40225(enchantment)) continue;
            return entry.getIntValue();
        }
        return 0;
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Math.sqrt(DamageUtils.squaredDistance(x1, y1, z1, x2, y2, z2));
    }

    public static class_1934 getGameMode(class_1657 player) {
        if (player == null) {
            return null;
        }
        class_640 playerListEntry = Wrapper.mc.method_1562().method_2871(player.method_5667());
        return playerListEntry == null ? null : playerListEntry.method_2958();
    }

    public static double squaredDistanceTo(class_1297 entity) {
        return DamageUtils.squaredDistanceTo(entity.method_23317(), entity.method_23318(), entity.method_23321());
    }

    public static double squaredDistanceTo(class_2338 blockPos) {
        return DamageUtils.squaredDistanceTo(blockPos.method_10263(), blockPos.method_10264(), blockPos.method_10260());
    }

    public static double squaredDistanceTo(double x, double y, double z) {
        return DamageUtils.squaredDistance(Wrapper.mc.field_1724.method_23317(), Wrapper.mc.field_1724.method_23318(), Wrapper.mc.field_1724.method_23321(), x, y, z);
    }

    public static double squaredDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double f = x1 - x2;
        double g = y1 - y2;
        double h = z1 - z2;
        return org.joml.Math.fma((double)f, (double)f, (double)org.joml.Math.fma((double)g, (double)g, (double)(h * h)));
    }

    public static void getEnchantments(class_1799 itemStack, Object2IntMap<class_6880<class_1887>> enchantments) {
        enchantments.clear();
        if (!itemStack.method_7960()) {
            for (Object2IntMap.Entry entry : itemStack.method_7909() == class_1802.field_8598 ? ((class_9304)itemStack.method_57824(class_9334.field_49643)).method_57539() : itemStack.method_58657().method_57539()) {
                enchantments.put((Object)((class_6880)entry.getKey()), entry.getIntValue());
            }
        }
    }

    @FunctionalInterface
    public static interface RaycastFactory
    extends BiFunction<ExposureRaycastContext, class_2338, class_3965> {
    }

    public record ExposureRaycastContext(class_243 start, class_243 end) {
    }
}

