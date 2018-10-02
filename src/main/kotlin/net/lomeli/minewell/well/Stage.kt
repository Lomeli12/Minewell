package net.lomeli.minewell.well

enum class Stage(timeSecs: Int, msg: String) {
    STAGE_ONE_CHARGING(10, "event.minewell.stage.one.charging"),
    STAGE_ONE(120, "event.minewell.stage.one.objective"),
    STAGE_TWO_CHARGING(10, "event.minewell.stage.two.charging"),
    STAGE_TWO(150, "event.minewell.stage.two.objective"),
    STAGE_THREE_CHARGING(10, "event.minewell.stage.three.charging"),
    STAGE_THREE(150, "event.minewell.stage.three.objective"),
    BOSS_CHARGING(10, "event.minewell.stage.boss.charging"),
    BOSS(240, "event.minewell.stage.boss.objective");

    private val maxTime: Int = timeSecs
    private val message: String = msg

    fun getMaxTime(): Int = maxTime
    fun getMessage(): String = message
}