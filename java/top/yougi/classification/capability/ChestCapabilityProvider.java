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

public class ChestCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ChestCapability> CHEST_CAPABILITY = CapabilityManager.get(new CapabilityToken<ChestCapability>() { });
    private ChestCapability chestCapability = null;
    private final LazyOptional<ChestCapability> optional = LazyOptional.of(this::createChestCapability);

    private ChestCapability createChestCapability() {
        if (this.chestCapability == null) {
            this.chestCapability = new ChestCapability();
        }

        return this.chestCapability;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CHEST_CAPABILITY) {
            return optional.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createChestCapability().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createChestCapability().loadNBTData(nbt);
    }
}
