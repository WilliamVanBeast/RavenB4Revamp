package net.minusmc.ravenb4.module.modules.others;

import net.minecraft.util.ChatComponentText
import net.minusmc.ravenb4.module.Module
import net.minusmc.ravenb4.module.ModuleCategory
import net.minusmc.ravenb4.setting.impl.DescriptionSetting


class FakeChat: Module("FakeChat", ModuleCategory.other){

    private val message: String = "&eThis is a fake chat message.";

    init {
        this.addSetting(DescriptionSetting("Desc", "Command: fakechat [msg]"))
    }

    override fun onEnable() {
        if (message.contains("\\n")) {
            val split: List<String> = message.split("\\\\n")
            for (s in split) {
                this.sendMessage(s)
            }
        } else {
            this.sendMessage(message)
        }

        disable()
    }

    private fun sendMessage(txt: String) {
        mc.thePlayer.addChatMessage(ChatComponentText(txt.replace("&", "§")))
    }

}