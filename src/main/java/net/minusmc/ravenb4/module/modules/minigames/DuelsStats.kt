package net.minusmc.ravenb4.module.modules.minigames

import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.utils.DuelMode
import net.minusmc.ravenb4.utils.ProfileUtils
import net.minusmc.ravenb4.setting.impl.ComboSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting

class DuelsStats: Module("DuelsStats", ModuleCategory.minigames) {
    private val modeValue = ComboSetting("Mode", DuelMode.values().toTypedArray(), "OVERALL")
    private val sendOnJoin = TickSetting("Send on join", false)
    private val threatLevelValue = TickSetting("Thread level", true)

    var nickName = ""

    private val atomicBool = AtomicBoolean()

    init {
    	addSetting(modeValue)
    	addSetting(sendOnJoin)
    	addSetting(threatLevelValue)
    }

    @SubscribeEvent
    fun onMessageRecieved(event: ClientChatReceivedEvent) {
        if (event.type != 2 && PlayerUtils.isPlayerInGame()) {
            if (atomicBool.compareAndSet(true, false)) {
                PlayerUtils.sendMessageToSelf("")
            }
        }
    }

    // trash function?
    // fun checkScoreboard(packet: S3EPacketTeams) {
    //     if (packet.name.equals("§7§k", true) && !packet.players.isEmpty()) {

    //     }
    // }

    fun sendMessage(playerName: String) {
        if (sendOnJoin.get())
            PlayerUtils.sendMessageToSelf("&7Opponent found: &b$playerName")
        RavenB4.executor.execute(() -> {
            val stat = ProfileUtils.getHypixelStats2(playerName, DuelMode.valueOf(modeValue.get()))
            if (stat == null) {
                PlayerUtils.sendMessageToSelf("&b $playerName &7is nicked!")
            } else {
                val values = stat.values
                val winPerLose = if (values[0] == 0) values[1].toDouble() else MahtUtils.round(values[0].toDouble() / values[1], 2)
                if (modeValue.get().equals("OVERALL", true)) {
                    PlayerUtils.sendMessageToSelf("&7Mode: &b ${e.name()}")
                }
                PlayerUtils.sendMessageToSelf("&r $stat.playerName")
                PlayerUtils.sendMessageToSelf("&7W/L: &b$winPerLose &7(&b${values[0]} &7W &b${values[1]} &7L)")
                PlayerUtils.sendMessageToSelf("&7WS: &b${values[2]} &7BWS: &b${values[3]}")
                if (d.d()) {
                    PlayerUtils.sendMessageToSelf("&7Threat: &r" + af.d(nArray[0], nArray[1], d, nArray[2], nArray[3]))
                }
            }
        })
    }

    // wtf check mode?
    fun checkMode(value1: Int, value2: Int, winPerLose: Double, value3: Int, value4: Int): String {
        var value5 = 0
        if (value1 + value2 <= 1) {
            value5 += 2
        }
        if (value3 >= 30) {
            value5 += 6
        } else if (value3 >= 15) {
            value5 += 4
        } else if (value3 >= 8) {
            value5 += 3
        } else if (value3 >= 4) {
            value5 += 2
        }

        if (value4 >= 400) {
            value5 += 5
        }
        if (value4 >= 200) {
            value5 += 4
        } else if (value4 >= 100) {
            value5 += 3
        } else if (value4 >= 50) {
            value5 += 2
        }

        if (winPerLose >= 20.0) {
            value5 += 8
        } else if (winPerLose >= 10.0) {
            value5 += 5
        } else if (winPerLose >= 5.0) {
            value5 += 4
        } else if (winPerLose >= 3.0) {
            value5 += 2
        } else if (winPerLose >= 0.8) {
            ++value5
        }

        if (value1 >= 20000) {
            value5 += 4
        } else if (value1 >= 10000) {
            value5 += 3
        } else if (value1 >= 5000) {
            value5 += 2
        } else if (value1 >= 1000) {
            ++value5
        }
        if (value2 == 0) {
            value5 = if (value1 == 0) value5 += 3 else value5 += 4
        } else if (value2 <= 10 && winPerLose >= 4.0) {
            value5 += 2
        }

        return if (n5 == 0) "§7§k"
        else if (n5 <= 3) "Threat level"
        else if (n5 <= 6) "Send on join"
        else if (n5 <= 10) "Mode"
        else "Duels Stats"
    }

}