package org.confluence.terra_furniture.common.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.confluence.terra_furniture.common.init.TFBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HangingPotBlock extends Block implements EntityBlock {
    public HangingPotBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BEntity bEntity) {
            if (bEntity.getFeatureStack().isEmpty()) return InteractionResult.PASS;
            else {
                bEntity.drops();
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof BEntity bEntity) {
            if (bEntity.placeFeature(player, player.getItemInHand(hand))) return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public static final class BEntity extends BlockEntity {
        private final ItemStackHandler itemStackHandler = new ItemStackHandler(1){
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
                if (level != null) {
                    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
                }
            }
            @Override
            public void setSize(int size) {
                super.setSize(1);
            }
        };
        public BEntity(BlockPos pos, BlockState blockState) {
            super(TFBlocks.HANGING_POT_ENTITY.get(), pos, blockState);
        }
        private Lazy<IItemHandler> lazyItemHandler = Lazy.of(() -> itemStackHandler);
        public static final String INVENTORY = "inventory";

        /**You can't change this output */
        public ItemStack getFeatureStack() {
            return itemStackHandler.getStackInSlot(0).copy();
        }

        public void changeFeature(ItemStack stack) {
            itemStackHandler.setStackInSlot(0, stack);
            this.markUpdated();
        }

        public boolean placeFeature(Entity entity, ItemStack stack) {
            if (!getFeatureStack().isEmpty() || !canBeHold(stack)) return false;
            changeFeature(stack.split(1));
            if (this.level != null) {
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, this.getBlockPos(), GameEvent.Context.of(entity, this.getBlockState()));
            }
            return true;
        }

        public boolean canBeHold(ItemStack stack) {
            if (!(stack.getItem() instanceof BlockItem blockItem)) return false;
            Block block = blockItem.getBlock();

            FlowerPotBlock potBlock = (FlowerPotBlock) Blocks.FLOWER_POT;
            BlockState state = potBlock.getEmptyPot().getFullPotsView().getOrDefault(BuiltInRegistries.BLOCK.getKey(block), () -> Blocks.AIR).get().defaultBlockState();
            if (!state.isAir()) return true;
            if (block instanceof BushBlock) return true;
            return block.equals(Blocks.TORCH) || block.equals(Blocks.SOUL_TORCH);
        }

        private void markUpdated() {
            this.setChanged();
            Objects.requireNonNull(this.getLevel()).sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }

        public void drops(){
            SimpleContainer inventory = new SimpleContainer(1);
            inventory.setItem(0, getFeatureStack());
            Containers.dropContents(Objects.requireNonNull(this.getLevel()), this.worldPosition, inventory);
            changeFeature(ItemStack.EMPTY);
            markUpdated();
        }

        public Lazy<IItemHandler> getLazyItemHandler() {
            return lazyItemHandler;
        }

        @Override
        public void onLoad() {
            super.onLoad();
            lazyItemHandler = Lazy.of(() -> itemStackHandler);
        }
        @Override
        protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
            tag.put(INVENTORY, itemStackHandler.serializeNBT(registries));
            super.saveAdditional(tag, registries);
        }
        @Override
        protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
            itemStackHandler.deserializeNBT(registries, tag.getCompound(INVENTORY));
            super.loadAdditional(tag, registries);
        }
        @Override
        public Packet<ClientGamePacketListener> getUpdatePacket() {
            return ClientboundBlockEntityDataPacket.create(this);
        }
        @Override
        public void onDataPacket(@NotNull Connection net, @NotNull ClientboundBlockEntityDataPacket pkt, HolderLookup.@NotNull Provider lookupProvider) {
            handleUpdateTag(pkt.getTag(), lookupProvider);
        }
        @Override
        public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
            return saveWithoutMetadata(registries);
        }
        @Override
        public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookupProvider) {
            super.handleUpdateTag(tag, lookupProvider);
        }
    }
}