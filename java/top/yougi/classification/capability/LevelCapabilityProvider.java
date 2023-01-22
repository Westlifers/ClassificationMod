package top.yougi.classification.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LevelCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<LevelCapability> LEVEL_CAPABILITY = CapabilityManager.get(new CapabilityToken<LevelCapability>() { });
    private LevelCapability LevelCapability = null;
    private final LazyOptional<LevelCapability> optional = LazyOptional.of(this::createLevelCapability);

    private LevelCapability createLevelCapability() {
        if (this.LevelCapability == null) {
            this.LevelCapability = new LevelCapability();
        }

        return this.LevelCapability;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == LEVEL_CAPABILITY) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createLevelCapability().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createLevelCapability().loadNBTData(nbt);
    }
}
