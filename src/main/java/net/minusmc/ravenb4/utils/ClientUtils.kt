package net.minusmc.ravenb4.utils

import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.StringUtils

object ClientUtils: MinecraftInstance() {
	fun reformat(txt: String) = txt.replace("&", "§")

	fun cleanString(s: String) = StringUtils.stripControlCodes(s).toCharArray().map{it.code}.filter{ it in 21..126 }.map{it.toChar()}.toString()

	fun isHypixel(): Boolean {
		if (!PlayerUtils.isPlayerInGame) return false
		return try {
			!mc.isSingleplayer && mc.currentServerData.serverIP.equals("hypixel.net", true)
		} catch (t: Throwable) {
			t.printStackTrace()
			false
		}
	}

	fun getPlayersFromScoreboard(): ArrayList<String> {
		mc.theWorld ?: return arrayListOf()

		val scoreboard = mc.theWorld.scoreboard ?: return arrayListOf()
		val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return arrayListOf()

		val scores = scoreboard.getSortedScores(objective).filter {it != null && it.playerName != null && !it.playerName.startsWith("#")}.take(15) // 10 lines in java = 1 line in kotlin
		val players = scores.map {
			val team = scoreboard.getPlayersTeam(it.playerName)
			ScorePlayerTeam.formatPlayerName(team, it.playerName)
		}
		return ArrayList(players)
	}
}