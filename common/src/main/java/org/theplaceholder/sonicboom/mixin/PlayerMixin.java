package org.theplaceholder.sonicboom.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.theplaceholder.sonicboom.Utils;
import org.theplaceholder.sonicboom.IPlayer;
import org.theplaceholder.sonicboom.config.Config;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements IPlayer {

    private Vec3 lastPos;
    public boolean isSonic = false;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Vec3 getLastPos() {
        return lastPos;
    }
    @Inject(at = @At("HEAD"), method = "aiStep()V")
    public void aiStep(CallbackInfo info) {
        lastPos = this.position();
    }
    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo info) {
        if(!this.level().isClientSide) return;
        if(!this.isFallFlying()) return;

        if(lastPos == null) lastPos = this.position();
        if(!isSonic && Utils.getSpeed(this) > Config.getExplosionSpeed()){
            Utils.explode(this);
            isSonic = true;
        }
        if(Utils.getSpeed(this) < Config.getExplosionThresholdSpeed())
            isSonic = false;
    }
}
