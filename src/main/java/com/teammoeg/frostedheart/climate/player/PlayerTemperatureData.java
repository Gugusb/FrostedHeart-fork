package com.teammoeg.frostedheart.climate.player;

import javax.annotation.Nullable;

import com.teammoeg.frostedheart.FHMain;
import com.teammoeg.frostedheart.climate.data.DeathInventoryData;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerTemperatureData implements ICapabilitySerializable<CompoundNBT>  {
	float previousTemp;
	float bodyTemp;
	float envTemp;
	float feelTemp;
	public float smoothedBody;//Client only, smoothed body temperature
	public float smoothedBodyPrev;//Client only, smoothed body temperature
    @CapabilityInject(PlayerTemperatureData.class)
    public static Capability<PlayerTemperatureData> CAPABILITY;
    public static final ResourceLocation ID = new ResourceLocation(FHMain.MODID, "temperature");
    private final LazyOptional<PlayerTemperatureData> capability=LazyOptional.of(()->this);
	public PlayerTemperatureData() {
	}
	public void load (CompoundNBT nbt) {
		previousTemp=nbt.getFloat("previous_body_temperature");
		bodyTemp=nbt.getFloat("bodytemperature");
		envTemp=nbt.getFloat("envtemperature");
		feelTemp=nbt.getFloat("feeltemperature");
	}
	public void save(CompoundNBT nc) {
        nc.putFloat("previous_body_temperature",previousTemp);
        nc.putFloat("bodytemperature",bodyTemp);
        nc.putFloat("envtemperature",envTemp);
        nc.putFloat("feeltemperature",feelTemp);
	}
	public void reset() {
		previousTemp=0;
		bodyTemp=0;
		envTemp=0;
		feelTemp=0;
		smoothedBody=0;
	}
    public void update(float body, float env,float feel) {
        // update delta before body
    	previousTemp=bodyTemp;
    	bodyTemp=body;
    	envTemp=env;
    	feelTemp=feel;
    }
    public static void setup() {
        CapabilityManager.INSTANCE.register(PlayerTemperatureData.class, new Capability.IStorage<PlayerTemperatureData>() {
            public void readNBT(Capability<PlayerTemperatureData> capability, PlayerTemperatureData instance, Direction side, INBT nbt) {
                instance.deserializeNBT((CompoundNBT) nbt);
            }

            public INBT writeNBT(Capability<PlayerTemperatureData> capability, PlayerTemperatureData instance, Direction side) {
                return instance.serializeNBT();
            }
        }, PlayerTemperatureData::new);
    }
    public static LazyOptional<PlayerTemperatureData> getCapability(@Nullable PlayerEntity player) {
        if (player != null) {
            return player.getCapability(CAPABILITY);
        }
        return LazyOptional.empty();
    }
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CAPABILITY)
            return capability.cast();
        return LazyOptional.empty();
	}
	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT saved=new CompoundNBT();
		save(saved);
		return saved;
	}
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		load(nbt);
	}
	public float getPreviousTemp() {
		return previousTemp;
	}
	public float getBodyTemp() {
		return bodyTemp;
	}
	public float getEnvTemp() {
		return envTemp;
	}
	public float getFeelTemp() {
		return feelTemp;
	}
	public void setPreviousTemp(float previousTemp) {
		this.previousTemp = previousTemp;
	}
	public void setBodyTemp(float bodyTemp) {
		this.bodyTemp = bodyTemp;
	}
	public void setEnvTemp(float envTemp) {
		this.envTemp = envTemp;
	}
	public void setFeelTemp(float feelTemp) {
		this.feelTemp = feelTemp;
	}
}
