package com.example.missile

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UnsignedLaunchCode : LaunchCode {
	override fun isSigned(): Boolean {
		return false
	}
	override fun isExpired(): Boolean {
		return false
	}
}

class SignedLaunchCode : LaunchCode {
	override fun isSigned(): Boolean {
		return true
	}
	override fun isExpired(): Boolean {
		return false
	}
}

class ExpiredLaunchCode : LaunchCode {
	override fun isSigned(): Boolean {
		return true
	}
	override fun isExpired(): Boolean {
		return true
	}
}

class DummyMissile : Missile {
	override fun launch() {
		throw Exception("KABOOM!")
	}

	override fun disable() {
	}
}

open class SpyMissile : Missile {
	var launch_isCalled = false
	var disable_isCalled = false

	override fun launch() {
		this.launch_isCalled = true
	}

	override fun disable() {
		this.disable_isCalled = true
	}
}

class MockMissile : SpyMissile() {
	fun verifyDisabled() {
		assertTrue(this.disable_isCalled)
		assertFalse(this.launch_isCalled)
	}
}

class MissileLauncherTests {

	@Test
	fun testDoNothingIfLaunchCodeIsNotSigned() {
		// launchCodeのisSigned()が偽ならば、なにもしない。

		// prepare
		val launcher = MissileLauncher()
		val dummyMissile = DummyMissile()
		val unsignedLaunchCode = UnsignedLaunchCode()
		// action
		launcher.launchMissile(dummyMissile, unsignedLaunchCode)
		// check
	}

	@Test
	fun testCallLaunchIfLaunchCodeIsSigned() {
		// launchCodeのisSigned()が真の場合、missileのlaunch()を呼ぶ。

		// prepare
		val launcher = MissileLauncher()
		val spyMissile = SpyMissile()
		val signedLaunchCode = SignedLaunchCode()
		// action
		launcher.launchMissile(spyMissile, signedLaunchCode)
		// check
		assertTrue(spyMissile.launch_isCalled)
	}

	@Test
	fun testDoNotCallLaunchIfLaunchCodeIsExpired() {
		// launchCodeのisExpired()が真の場合、isSignedの値にかかわらず、missileのlaunch()を呼ばない。

		// prepare
		val launcher = MissileLauncher()
		val spyMissile = SpyMissile()
		val expiredLaunchCode = ExpiredLaunchCode()
		// action
		launcher.launchMissile(spyMissile, expiredLaunchCode)
		// check
		assertFalse(spyMissile.launch_isCalled)
	}

	@Test
	fun testCallDisableIfLaunchCodeIsExpired() {
		// launchCodeのisExpired()が真の場合、isSignedの値にかかわらず、missileのdisable()を呼んでlaunch()を呼ばない。

		// prepare
		val launcher = MissileLauncher()
		val spyMissile = SpyMissile()
		val expiredLaunchCode = ExpiredLaunchCode()
		// action
		launcher.launchMissile(spyMissile, expiredLaunchCode)
		// check
		assertTrue(spyMissile.disable_isCalled)
		assertFalse(spyMissile.launch_isCalled)
	}

	@Test
	fun testCallDisableIfLaunchCodeIsNotSigned() {
		// launchCodeのisSigned()が偽の場合、missileのdisable()を呼んでlaunch()を呼ばない。

		// prepare
		val launcher = MissileLauncher()
		val mockMissile = MockMissile()
		val unsignedLaunchCode = UnsignedLaunchCode()
		// action
		launcher.launchMissile(mockMissile, unsignedLaunchCode)
		// check
		mockMissile.verifyDisabled()
	}
}
