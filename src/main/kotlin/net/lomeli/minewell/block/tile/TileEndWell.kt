package net.lomeli.minewell.block.tile

import com.google.common.base.Strings
import net.lomeli.minewell.core.helpers.NetworkHelper
import net.lomeli.minewell.core.util.RangeUtil
import net.lomeli.minewell.potion.ModPotions
import net.lomeli.minewell.well.TierRegistry
import net.lomeli.minewell.well.WellTier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.potion.PotionEffect
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.EnumDifficulty
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

const val MAX_DISTANCE = 20f

class TileEndWell : TileEntity(), ITickable {
    private val MAX_RADIUS = 5.5f
    private val RADIUS_RATE = 0.05f
    private var eventTier: WellTier? = null
    private var tierName: String? = null
    private var tierData: NBTTagCompound? = null
    var radius = 0f
    private var timer = 0

    override fun update() {
        if (world == null) return

        if (!Strings.isNullOrEmpty(tierName) && tierData != null) {
            eventTier = TierRegistry.getTierFromName(tierName!!, tierData, this)
            eventTier!!.readFromNBT(tierData!!)
            tierData = null
            tierName = null
        }

        if (world.difficulty == EnumDifficulty.PEACEFUL) {
            eventTier = null
            timer = 0
        }
        if (isWellActivated() && timer > 0) {
            timer--
            if (world.worldTime % 20L == 0L) {
                if (!world.isRemote) {
                    if (eventTier!!.getCurrentStage().hasMiasma())
                        giveTouchEffects()
                }
            }

            eventTier?.updateTick(this)

            if (radius < MAX_RADIUS) {
                radius += RADIUS_RATE
                if (radius > MAX_RADIUS)
                    radius = MAX_RADIUS
            }
            if (timer <= 0) {
                eventTier?.clearMobs()
                eventTier = null
                if (!world.isRemote)
                    NetworkHelper.updateClientsWithinRange(0, 0, this)
            }
            this.markDirty()
        } else {
            if (radius > 0f)
                radius -= RADIUS_RATE
        }
    }

    fun giveTouchEffects() {
        val range = AxisAlignedBB(pos.x.toDouble(), pos.y - 2.0, pos.z.toDouble(), pos.x + 1.0, pos.y - 1.0, pos.z + 1.0)
                .grow(MAX_DISTANCE.toDouble())
        val playerList = world.getEntitiesWithinAABB(EntityPlayer::class.java, range)
        if (playerList.isEmpty()) return
        for (player in playerList) {
            val distance = player.getDistance(pos.x.toDouble(), pos.y - 2.0, pos.z.toDouble())
            if (distance <= MAX_RADIUS)
                player.addPotionEffect(PotionEffect(ModPotions.SKY_TOUCH, 100))
            else if (distance <= MAX_DISTANCE &&
                    !(player.isPotionActive(ModPotions.SKY_TOUCH) || player.isPotionActive(ModPotions.HARMONY)))
                player.addPotionEffect(PotionEffect(ModPotions.VOID_TOUCH, 100))
        }
    }

    fun setTier(tier: WellTier) {
        if (!isWellActivated() && world.difficulty != EnumDifficulty.PEACEFUL) {
            eventTier = tier
            setTimer(tier.getCurrentStage().getMaxTime())
        }
    }

    fun getTier(): WellTier? = eventTier

    fun isWellActivated(): Boolean = eventTier != null

    fun setTimer(timeInSec: Int) {
        timer = timeInSec * 20
    }

    fun getTimer(): Int = timer

    override fun readFromNBT(nbt: NBTTagCompound) {
        if (nbt.hasKey("timer", 3))
            timer = nbt.getInteger("timer")
        if (nbt.hasKey("radius", 5))
            radius = nbt.getFloat("radius")
        if (nbt.hasKey("end_well_tier", 8)) {
            tierName = nbt.getString("end_well_tier")
            tierData = nbt
        }
        super.readFromNBT(nbt)
    }

    override fun writeToNBT(nbt: NBTTagCompound): NBTTagCompound {
        nbt.setInteger("timer", timer)
        nbt.setFloat("radius", radius)
        eventTier?.writeToNBT(nbt)
        return super.writeToNBT(nbt)
    }

    override fun getUpdateTag(): NBTTagCompound =writeToNBT(NBTTagCompound())

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        val nbt = NBTTagCompound()
        writeToNBT(nbt)
        return SPacketUpdateTileEntity(pos, 1, nbt)
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
    }

    @SideOnly(Side.CLIENT) override fun getRenderBoundingBox(): AxisAlignedBB {
        if (isWellActivated()) return super.getRenderBoundingBox().grow(6.0)
        return super.getRenderBoundingBox()
    }
}