package net.minusmc.ravenb4.utils

object ProfileUtils {
	fun isHypixelKeyValid(key: String): Boolean {
        val string2 = getTextFromURL("https://api.hypixel.net/key?key=" + key, false)
        return !string2.isEmpty() && !string2.contains("Invalid API")
    }

	fun getHypixelStats2(playerName: String, mode: DuelMode): Stats? {
		val stat = Stats(playerName, listOf(0, 0, 0, 0))
		val headers = arrayOf(
			arrayOf("User-Agent", "Mozilla/4.76 (Sk1er ModCore)")
		)
		val text = URLUtils.getTextFromURL("https://api.sk1er.club/player/" + playerName, headers, false)
		if (text.length < 50) return null

		val objectData: JsonObject
		val stats: JsonObject

		try {
			objectData = parseJson(text).asJsonObject["player"]
			stats = objectData.asJsonObject["stats"].asJsonObject["Duels"]
		} catch (e: NullPointerException) {
			return stat
		}

		val displayName = getAttrAsString(objectData, "display")
		if (!displayName.isEmpty()) {
			stat.playerName = displayName.replace("Â§", "§")
		}

		when (mode) {
			DuelMode.OVERALL -> stat.values = listOf(getAttrAsInt(stats, "wins"), getAttrAsInt(stats, "losses"), getAttrAsInt(stats, "current_winstreak"), getAttrAsInt(stats, "best_overall_winstreak"))
	     	DuelMode.BRIDGE -> stat.values = listOf(getAttrAsInt(stats, "bridge_duel_wins"), getAttrAsInt(stats, "bridge_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_bridge_duel"), getAttrAsInt(stats, "best_winstreak_mode_bridge_duel"))
	        DuelMode.UHC -> stat.values = listOf(getAttrAsInt(stats, "uhc_duel_wins"), getAttrAsInt(stats, "uhc_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_uhc_duel"), getAttrAsInt(stats, "best_winstreak_mode_uhc_duel"))
	        DuelMode.SKYWARS -> stat.values = listOf(getAttrAsInt(stats, "sw_duel_wins"), getAttrAsInt(stats, "sw_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_sw_duel"), getAttrAsInt(stats, "best_winstreak_mode_sw_duel"))
	        DuelMode.CLASSIC -> stat.values = listOf(getAttrAsInt(stats, "classic_duel_wins"), getAttrAsInt(stats, "classic_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_classic_duel"), getAttrAsInt(stats, "best_winstreak_mode_classic_duel"))
	        DuelMode.SUMO -> stat.values = listOf(getAttrAsInt(stats, "sumo_duel_wins"), getAttrAsInt(stats, "sumo_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_sumo_duel"), getAttrAsInt(stats, "best_winstreak_mode_sumo_duel"))
	        DuelMode.OP -> stat.values = listOf(getAttrAsInt(stats, "op_duel_wins"), getAttrAsInt(stats, "op_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_op_duel"), getAttrAsInt(stats, "best_winstreak_mode_op_duel"))
		}
		return stat
	}

	fun getHypixelStats(playerName: String, mode: DuelMode): List<Int> {
		val idProfile = getIdProfile(playerName)
		if (!idProfile.isEmpty()) return listOf(-1, 0, 0)

		val text = URLUtils.getTextFromURL("https://api.hypixel.net/player?key=${URLUtils.hypixelKey}&uuid=$idProfile")
		if (text.isEmpty()) return null
		if (text.equals("{\"success\":true,\"player\":null}", true)) return listOf(-1, 0, 0)
		val stats: JsonObject

		try {
			val objectData = parseJson(text).asJsonObject["player"]
			stats = objectData.asJsonObject["stats"].asJsonObject["Duels"]
		} catch (e: NullPointerException) {
			return stat
		}

		return when (mode) {
			DuelMode.OVERALL -> listOf(getAttrAsInt(stats, "wins"), getAttrAsInt(stats, "losses"), getAttrAsInt(stats, "current_winstreak"))
	     	DuelMode.BRIDGE -> listOf(getAttrAsInt(stats, "bridge_duel_wins"), getAttrAsInt(stats, "bridge_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_bridge_duel"))
	        DuelMode.UHC -> listOf(getAttrAsInt(stats, "uhc_duel_wins"), getAttrAsInt(stats, "uhc_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_uhc_duel"))
	        DuelMode.SKYWARS -> listOf(getAttrAsInt(stats, "sw_duel_wins"), getAttrAsInt(stats, "sw_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_sw_duel"))
	        DuelMode.CLASSIC -> listOf(getAttrAsInt(stats, "classic_duel_wins"), getAttrAsInt(stats, "classic_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_classic_duel"))
	        DuelMode.SUMO -> listOf(getAttrAsInt(stats, "sumo_duel_wins"), getAttrAsInt(stats, "sumo_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_sumo_duel"))
	        DuelMode.OP -> listOf(getAttrAsInt(stats, "op_duel_wins"), getAttrAsInt(stats, "op_duel_losses"), getAttrAsInt(stats, "current_winstreak_mode_op_duel"))
	        else -> null
		}
	}

	fun getHypixelBedwarsStat(uuid: String) {

	}

	fun parseJson(text: String) = JsonParser().parse(text).asJsonObject

	fun getAttrAsString(objectData: JsonObject, attr: String) {
		try {
			return objectData[attr].asString
		} catch (e: Exception) {
			return ""
		}
	}

	fun getAttrAsInt(objectData: JsonObject, attr: String) {
		try {
			return objectData[attr].asInt
		} catch (e: Exception) {
			return 0
		} catch (e: UnsupportedOperationException) {
			return 0
		}
	}

	fun getIdProfile(name: String): String {
		val text = URLUtils.getTextFromURL("https://api.mojang.com/users/profiles/minecraft/$name")
		if (text.isEmpty()) return ""
		try {
			return text.replace(" ", "").split("d\":\"")[1].split("\"")[0]
		} catch (e: ArrayIndexOutOfBoundsException) {
			return ""
		}
	}

	fun getNameProfile(id: String): String {
		val text = URLUtils.getTextFromURL("https://api.mojang.com/users/profile/$name")
		if (text.isEmpty()) return ""
		try {
			return text.replace(" ", "").split("e\":\"")[1].split("\"")[0]
		} catch (e: ArrayIndexOutOfBoundsException) {
			return ""
		}
	}
}

class Stats(val playerName: String, val values: List<Int>)

enum class DuelMode {
	OVERALL, BRIDGE, UHC, SKYWARS, CLASSIC, SUMO, OP
}