package net.lomeli.minewell.block.tile

import com.google.common.base.Strings
import net.lomeli.minewell.core.helpers.NetworkHelper
import net.lomeli.minewell.core.util.RangeUtil
import net.lomeli.minewell.potion.ModPotions
import net.lomeli.minewell.well.Stage
import net.lomeli.minewell.well.TierRegistry
import net.lomeli.minewell.well.WellTier
import net.minecraft.init.SoundEvents
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.potion.PotionEffect
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.EnumDifficulty
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

const val MAX_DISTANCE = 20f
const val EFFECT_RANGE = MAX_DISTANCE + 5.0
const val MAX_RADIUS = 5.5f

class TileEndWell : TileEntity(), ITickable {
    private val radiusRate = 0.05f
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

            eventTier!!.updateTick(this)

            if (eventTier!!.getCurrentStage() != Stage.BOSS_CHARGING && eventTier!!.getCurrentStage() != Stage.BOSS) {
                if (radius < MAX_RADIUS) {
                    radius += radiusRate
                    if (radius > MAX_RADIUS)
                        radius = MAX_RADIUS
                }
            } else {
                if (radius > 0f)
                    radius -= radiusRate
            }

            if (timer <= 0) {
                eventTier?.clearMobs()
                if (!world.isRemote) {
                    NetworkHelper.updateClientsWithinRange(0, 0, this)
                    if (eventTier!!.isFailure()) {
                        val players = RangeUtil.getPlayersInRange(MAX_DISTANCE.toDouble(), pos, world)
                        if (players.isNotEmpty()) {
                            for (player in players)
                                player.sendMessage(TextComponentTranslation("event.minewell.failure"))
                        }
                    }
                }
                eventTier = null
            }
            this.markDirty()
        } else {
            if (radius > 0f)
                radius -= radiusRate
        }
    }

    fun giveTouchEffects() {
        val playerList = RangeUtil.getPlayersInRange(EFFECT_RANGE, pos, world)
        if (playerList.isEmpty()) return
        for (player in playerList) {
            val distance = player.getDistance(pos.x.toDouble(), pos.y - 2.0, pos.z.toDouble())
            if (distance <= MAX_RADIUS || distance > MAX_DISTANCE)
                player.addPotionEffect(PotionEffect(ModPotions.SKY_TOUCH, 100))
            else if (distance <= MAX_DISTANCE &&
                    !(player.isPotionActive(ModPotions.SKY_TOUCH) || player.isPotionActive(ModPotions.HARMONY)))
                player.addPotionEffect(PotionEffect(ModPotions.VOID_TOUCH, 100))
        }
    }

    fun setTier(tier: WellTier) {
        if (!isWellActivated() && world.difficulty != EnumDifficulty.PEACEFUL) {
            world.playSound(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), SoundEvents.ENTITY_ENDERDRAGON_AMBIENT,
                    SoundCategory.HOSTILE, 1f, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.2f + 1.0f), false)
            eventTier = tier
            setTimer(tier.getCurrentStage().getMaxTime())

            if (!world.isRemote)
                NetworkHelper.updateClientsWithinRange(eventTier!!.getCurrentKills(), eventTier!!.getKillsNeeded(), this)
        }
    }

    fun getTier(): WellTier? = eventTier

    fun isWellActivated(): Boolean = eventTier != null && timer > 0

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

    override fun getUpdateTag(): NBTTagCompound = writeToNBT(NBTTagCompound())

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        val nbt = NBTTagCompound()
        writeToNBT(nbt)
        return SPacketUpdateTileEntity(pos, 1, nbt)
    }

    override fun onDataPacket(net: NetworkManager, pkt: SPacketUpdateTileEntity) {
        readFromNBT(pkt.nbtCompound)
    }

    @SideOnly(Side.CLIENT)
    override fun getRenderBoundingBox(): AxisAlignedBB {
        if (isWellActivated()) return super.getRenderBoundingBox().grow(6.0)
        return super.getRenderBoundingBox()
    }
}