package net.lomeli.minewell.block

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.well.TierOne
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Items
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.EnumDifficulty
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

class BlockEndWell : BlockBase("end_well", Material.GLASS) {
    init {
        this.setRegistryName(Minewell.MOD_ID, "end_well")
        this.blockHardness = 1.5f
        this.blockResistance = 2000f
        this.blockSoundType = SoundType.STONE
        this.setCreativeTab(CreativeTabs.DECORATIONS)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer,
                                  hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val itemUsed = playerIn.getHeldItem(hand)
        if (itemUsed.item == Items.NETHER_STAR) {
            val tile = worldIn.getTileEntity(pos)
            if (tile != null && tile is TileEndWell && !tile.isWellActivated()) {
                if (worldIn.difficulty == EnumDifficulty.PEACEFUL) {
                    if (!worldIn.isRemote)
                        playerIn.sendMessage(TextComponentTranslation("event.minewell.peaceful"))
                    return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)
                }
                tile.setTier(TierOne())

                if (!playerIn.isCreative)
                    itemUsed.shrink(1)
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ)
    }

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
            = AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8)

    @SideOnly(Side.CLIENT) override fun shouldSideBeRendered(blockState: IBlockState, blockAccess: IBlockAccess,
                                                             pos: BlockPos, side: EnumFacing): Boolean = false

    override fun isBlockNormalCube(state: IBlockState): Boolean = false

    override fun isOpaqueCube(state: IBlockState): Boolean = false

    override fun isFullCube(state: IBlockState): Boolean = false

    override fun hasTileEntity(state: IBlockState): Boolean = true

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? = TileEndWell()

    override fun getRenderType(state: IBlockState): EnumBlockRenderType = EnumBlockRenderType.INVISIBLE
}