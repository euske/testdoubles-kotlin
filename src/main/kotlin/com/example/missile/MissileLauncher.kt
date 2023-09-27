package com.example.missile

class MissileLauncher {

    fun launchMissile(missile: Missile, launchCode: LaunchCode): Unit {
        if (launchCode.isExpired()) {
            missile.disable()
        } else if (launchCode.isSigned()) {
            missile.launch()
        } else {
            missile.disable()
        }
    }
}