package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate
{
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private final int field_150068_a;
    private static final String __OBFID = "CL_00000334";

    protected BlockPressurePlateWeighted(String p_i45436_1_, Material p_i45436_2_, int p_i45436_3_)
    {
        super(p_i45436_2_);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
        this.field_150068_a = p_i45436_3_;
    }

    protected int computeRedstoneStrength(World worldIn, BlockPos pos)
    {
        int i = Math.min(worldIn.getEntitiesWithinAABB(Entity.class, this.getSensitiveAABB(pos)).size(), this.field_150068_a);

        if (i > 0)
        {
            float f = (float)Math.min(this.field_150068_a, i) / (float)this.field_150068_a;
            return MathHelper.ceiling_float_int(f * 15.0F);
        }
        else
        {
            return 0;
        }
    }

    protected int getRedstoneStrength(IBlockState p_176576_1_)
    {
        return ((Integer)p_176576_1_.getValue(POWER)).intValue();
    }

    protected IBlockState setRedstoneStrength(IBlockState p_176575_1_, int p_176575_2_)
    {
        return p_176575_1_.withProperty(POWER, Integer.valueOf(p_176575_2_));
    }

    public int tickRate(World worldIn)
    {
        return 10;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {POWER});
    }
}