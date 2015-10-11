package net.minecraft.block;

import com.riagenic.HAXE;
import com.riagenic.MossyClient;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import static com.riagenic.MossyClient.*;

public class BlockSoulSand extends Block
{
    private static final String __OBFID = "CL_00000310";

    public BlockSoulSand()
    {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        float f = 0.125F;
        return new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)((float)(pos.getY() + 1) - f), (double)(pos.getZ() + 1));
    }

    // TODO : HAXED - onEntityCollidedWithBlock
    /*================================ HAXE ================================================*/
    @HAXE(Side.CLIENT)
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if(Mossy.getMods().IsNoSlowdownEnabled) {
            return;
        }
        entityIn.motionX *= 0.4D;
        entityIn.motionZ *= 0.4D;

    }
    /*================================ HAXE ================================================*/
}