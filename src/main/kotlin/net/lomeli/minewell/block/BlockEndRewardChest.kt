package net.lomeli.minewell.block

import net.lomeli.minewell.Minewell
import net.lomeli.minewell.block.tile.TileEndRewardChest
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

class BlockEndRewardChest: BlockBase("end_reward_chest", Material.IRON) {

    private val validRotationAxes = arrayOf(EnumFacing.UP, EnumFacing.DOWN)

    init {
        this.blockHardness = 1.5f
        this.blockResistance = 2000f
        this.setBlockUnbreakable()
        this.blockSoundType = SoundType.METAL
    }

    override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, player: EntityPlayer,
                                  hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        val tile = world.getTileEntity(pos)
        if (tile is TileEndRewardChest) {
            if (!player.isSneaking) {
                val rewards = tile.getPlayerInventory(player)
                if (rewards != null) {
                    player.openGui(Minewell.instance, -1, world, pos.x, pos.y, pos.z)
                    tile.openChest()
                    return true
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)
    }

    override fun rotateBlock(world: World, pos: BlockPos, axis: EnumFacing): Boolean {
        if (world.isRemote) return false
        if (axis == EnumFacing.UP || axis == EnumFacing.DOWN) {
            val tile = world.getTileEntity(pos)
            if (tile is TileEndRewardChest) {

            }
            return true
        }
        return false
    }

    override fun getValidRotations(world: World, pos: BlockPos): Array<EnumFacing>? = validRotationAxes

    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB =
            AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375)

    override fun isOpaqueCube(state: IBlockState): Boolean = false

    override fun isFullCube(state: IBlockState): Boolean = false

    override fun getRenderType(state: IBlockState): EnumBlockRenderType = EnumBlockRenderType.INVISIBLE

    override fun hasTileEntity(state: IBlockState): Boolean = true

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? = TileEndRewardChest()

    override fun eventReceived(state: IBlockState, world: World, pos: BlockPos, id: Int, param: Int): Boolean {
        super.eventReceived(state, world, pos, id, param)
        val tileentity = world.getTileEntity(pos)
        return tileentity?.receiveClientEvent(id, param) ?: false
    }
}