
package com.dungeonderps.resourcefulbees.block;

import com.dungeonderps.resourcefulbees.RegistryHandler;
import com.dungeonderps.resourcefulbees.lib.BeeConst;
import com.dungeonderps.resourcefulbees.tileentity.HoneycombBlockEntity;
import com.dungeonderps.resourcefulbees.utils.Color;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class HoneycombBlock extends Block {

    public String blockColor;
    //public String beeType;

    public HoneycombBlock() {
        super(Block.Properties.from(Blocks.HONEYCOMB_BLOCK));
    }

    public void setBlockColor(String blockColor){
        this.blockColor = blockColor;
    }

    public void setBeeType(String beeType) {
        //this.beeType = beeType;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new HoneycombBlockEntity();}

    @Override    // TODO FIX THIS TO USE TE - don't pull data from block instance!!
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        ItemStack honeyCombBlockItemStack = new ItemStack(RegistryHandler.HONEYCOMB_BLOCK_ITEM.get());
        final CompoundNBT honeyCombItemStackTag = honeyCombBlockItemStack.getOrCreateChildTag(BeeConst.NBT_ROOT);
        //honeyCombItemStackTag.putString(BeeConst.NBT_BEE_TYPE, this.beeType);
        honeyCombItemStackTag.putString(BeeConst.NBT_COLOR, this.blockColor);

        return honeyCombBlockItemStack;
    }

    public static int getBlockColor(BlockState state, @Nullable IBlockReader world, @Nullable BlockPos pos, int tintIndex){
        if (world != null && pos != null){
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof HoneycombBlockEntity) {
                return  ((HoneycombBlockEntity) tile).getColor();
            }
        }

        return BeeConst.DEFAULT_COLOR;
    }



    public static int getItemColor(ItemStack stack, int tintIndex){
        CompoundNBT honeycombNBT = stack.getChildTag(BeeConst.NBT_ROOT);
        return (honeycombNBT != null && honeycombNBT.contains(BeeConst.NBT_COLOR) && !honeycombNBT.getString(BeeConst.NBT_COLOR).isEmpty())
                ? Color.parseInt(honeycombNBT.getString(BeeConst.NBT_COLOR)) : BeeConst.DEFAULT_COLOR;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile instanceof HoneycombBlockEntity) {
            HoneycombBlockEntity honeycombBlockEntity = (HoneycombBlockEntity) tile;
            honeycombBlockEntity.loadFromNBT(stack.getOrCreateChildTag(BeeConst.NBT_ROOT));

            setBeeType(stack.getChildTag("ResourcefulBees").getString("BeeType"));
        }

        //this.beeType = stack.getChildTag(BeeConst.NBT_ROOT).getString(BeeConst.NBT_BEE_TYPE);
        this.blockColor = stack.getChildTag(BeeConst.NBT_ROOT).getString(BeeConst.NBT_COLOR);
    }
}
