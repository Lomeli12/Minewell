package net.lomeli.minewell.block

import net.lomeli.minewell.block.tile.TileEndWell
import net.lomeli.minewell.item.ItemLightCharge
import net.lomeli.minewell.item.ModItems
import net.lomeli.minewell.well.WellTier
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
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
        this.blockHardness = 1.5f
        this.blockResistance = 2000f
        this.blockSoundType = SoundType.STONE
    }

    override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer,
                                  hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val itemUsed = player.getHeldItem(hand)
        if (itemUsed.item is ItemLightCharge) {
            val tile = world.getTileEntity(pos)
            if (tile is TileEndWell && !tile.isWellActivated() && isValidShape(world, pos)) {
                if (world.difficulty == EnumDifficulty.PEACEFUL) {
                    if (!world.isRemote)
                        player.sendMessage(TextComponentTranslation("event.minewell.peaceful"))
                    return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)
                }
                val tier = getTierFromCharge(itemUsed)
                tier.initTier(tile)
                tile.setTier(tier)

                if (!player.isCreative)
                    itemUsed.shrink(1)
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)
    }

    private fun isValidShape(world: World, pos: BlockPos): Boolean{
        for (y in 1..2) {
            if (!world.isAirBlock(BlockPos(pos.x, pos.y - y, pos.z)))
                return false
        }

        for (x in -1..1)
            for (z in -1..1) {

            }

        return true
    }

    override fun breakBlock(world: World, pos: BlockPos, state: IBlockState) {
        val tile = world.getTileEntity(pos)
        if (tile is TileEndWell && tile.isWellActivated()) {
            val tier = tile.getTier()!!
            tier.clearMobs()
        }
        super.breakBlock(world, pos, state)
    }

    private fun getTierFromCharge(stack: ItemStack): WellTier = ModItems.LIGHT_CHARGE.getTierFromStack(stack)

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB = AxisAlignedBB(0.2, 0.0, 0.2, 0.8, 1.0, 0.8)

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(blockState: IBlockState, blockAccess: IBlockAccess,
                                      pos: BlockPos, side: EnumFacing): Boolean = false

    override fun isBlockNormalCube(state: IBlockState): Boolean = false

    override fun isOpaqueCube(state: IBlockState): Boolean = false

    override fun isFullCube(state: IBlockState): Boolean = false

    override fun hasTileEntity(state: IBlockState): Boolean = true

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? = TileEndWell()

    override fun getRenderType(state: IBlockState): EnumBlockRenderType = EnumBlockRenderType.INVISIBLE
}