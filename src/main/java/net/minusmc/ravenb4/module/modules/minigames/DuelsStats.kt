package net.minusmc.ravenb4.module.modules.minigames

import net.minusmc.ravenb4.RavenB4
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.utils.DuelMode
import net.minusmc.ravenb4.setting.impl.ComboSetting
import net.minusmc.ravenb4.setting.impl.SliderSetting
import net.minusmc.ravenb4.setting.impl.TickSetting

class DuelsStats: Module("DuelsStats", ModuleCategory.minigames) {
    private val modeValue = ComboSetting("Mode", DuelMode.values(), "OVERALL")
    private val sendOnJoin = TickSetting("Send on join", false)
    private val threatLevelValue = TickSetting("Thread level", true)

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

    fun sendMessage(message: String) {
        if (sendOnJoin.get())
            PlayerUtils.sendMessageToSelf("&7Opponent found: &b$message")
        RavenB4.executor.execute(() -> {

        })
    }

}