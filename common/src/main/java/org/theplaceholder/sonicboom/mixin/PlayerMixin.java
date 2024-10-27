package org.theplaceholder.sonicboom.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.theplaceholder.sonicboom.SonicBoom.*;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    private Vec3 sonicBoom$lastPos;
    private boolean sonicBoom$isSonic = false;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "aiStep()V")
    public void aiStep(CallbackInfo info) {
        sonicBoom$lastPos = this.position();
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo info) {
        if(!this.level().isClientSide) return;
        if(!this.isFallFlying()) return;

        if(sonicBoom$lastPos == null) sonicBoom$lastPos = this.position();
        if(!sonicBoom$isSonic && sonicBoom$getSpeed() > EXPLOSION_SPEED){
            sonicBoom$explode();
            sonicBoom$isSonic = true;
        }
        if(sonicBoom$getSpeed() < EXPLOSION_THRESHOLD_SPEED)
            sonicBoom$isSonic = false;
    }

    private double sonicBoom$getSpeed(){
        return this.sonicBoom$getLastPos().distanceTo(position()) * 20;
    }

    private void sonicBoom$explode(){
        Level level = this.level();
        level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 1.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1.0D, 0.0D, 0.0D);
        level.playLocalSound(this.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS,4.0F, 1, false);
    }

    public Vec3 sonicBoom$getLastPos() {
        return sonicBoom$lastPos;
    }
}
