package net.lomeli.minewell.well


val STAGE_VALUES = arrayOf(Stage.STAGE_ONE_CHARGING, Stage.STAGE_ONE, Stage.STAGE_TWO_CHARGING, Stage.STAGE_TWO, Stage.STAGE_THREE_CHARGING,
        Stage.STAGE_THREE, Stage.BOSS_CHARGING, Stage.BOSS)

enum class Stage(timeSecs: Int, msg: String, hasMiasma: Boolean, displayBar: Boolean, hasSphere: Boolean) {
    STAGE_ONE_CHARGING(10, "event.minewell.stage.one.charging", false, false, false),
    STAGE_ONE(120, "event.minewell.stage.one.objective", true, true, false),
    STAGE_TWO_CHARGING(10, "event.minewell.stage.two.charging", true, false, false),
    STAGE_TWO(150, "event.minewell.stage.two.objective", true, true, false),
    STAGE_THREE_CHARGING(10, "event.minewell.stage.three.charging", true, false, false),
    STAGE_THREE(150, "event.minewell.stage.three.objective", true, true, false),
    BOSS_CHARGING(10, "event.minewell.stage.boss.charging", false, false, false),
    BOSS(240, "event.minewell.stage.boss.objective", false, false, false);



    private val maxTime: Int = timeSecs
    private val message: String = msg
    private val miasma: Boolean = hasMiasma
    private val displayBar: Boolean = displayBar
    private val hasSphere: Boolean = hasSphere

    fun getMaxTime(): Int = maxTime
    fun getMessage(): String = message
    fun hasMiasma(): Boolean = miasma
    fun displayHUDBar(): Boolean = displayBar
    fun hasSafeSphere(): Boolean = hasSphere
}