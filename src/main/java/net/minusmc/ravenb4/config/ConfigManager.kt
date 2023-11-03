package net.minusmc.ravenb4.config

import net.minusmc.keystrokes.KeyStroke
import net.minusmc.ravenb4.utils.MinecraftInstance
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class ConfigManager: MinecraftInstance() {
    private val keyStrokesPath = mc.mcDataDir.path + File.separator + "keystrokes"
    private val modPath = keyStrokesPath + "mod"

    fun saveToConfigFile() {
        try {
            val file = File(keyStrokesPath, "config.txt")
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            val fileWriter = FileWriter(file, false)
            fileWriter.write("api: \n") // later
            fileWriter.write("\n[ profiles ]\n\n")
        } catch (_: Exception) {}
    }

    fun loadKeyStorkes() {
        try {
            val file = File(modPath, "config")
            if (!file.exists()) return

            val bufferedReader = BufferedReader(FileReader(file))
            var n = 0
            for (line in bufferedReader.readLines()) {
//                when (n++) {
//                    1 -> KeyStroke.
//                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}