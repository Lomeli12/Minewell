package net.lomeli.minewell.well


val STAGE_VALUES = arrayOf(Stage.STAGE_ONE_CHARGING, Stage.STAGE_ONE, Stage.STAGE_TWO_CHARGING, Stage.STAGE_TWO, Stage.STAGE_THREE_CHARGING,
        Stage.STAGE_THREE, Stage.BOSS_CHARGING, Stage.BOSS)

enum class Stage(timeSecs: Int, msg: String, hasMiasma: Boolean) {
    STAGE_ONE_CHARGING(10, "event.minewell.stage.one.charging", false),
    STAGE_ONE(120, "event.minewell.stage.one.objective", true),
    STAGE_TWO_CHARGING(10, "event.minewell.stage.two.charging", true),
    STAGE_TWO(150, "event.minewell.stage.two.objective", true),
    STAGE_THREE_CHARGING(10, "event.minewell.stage.three.charging", true),
    STAGE_THREE(150, "event.minewell.stage.three.objective", true),
    BOSS_CHARGING(10, "event.minewell.stage.boss.charging", false),
    BOSS(240, "event.minewell.stage.boss.objective", false);



    private val maxTime: Int = timeSecs
    private val message: String = msg
    private val miasma: Boolean = hasMiasma

    fun getMaxTime(): Int = maxTime
    fun getMessage(): String = message
    fun hasMiasma(): Boolean = miasma
}