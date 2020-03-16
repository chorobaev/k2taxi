package io.jachoteam.taxiappclient.models

import io.jachoteam.taxiappclient.models.local.VersionDifference
import java.util.*
import javax.inject.Inject

class VersionChecker @Inject constructor() {

    fun isTimeToCheckVersion(lastCheckedTimestamp: Long): Boolean {
        return lastCheckedTimestamp == 0L ||
            Date().time - lastCheckedTimestamp >= VERSION_CHECK_DURATION_IN_SEC * 1000
    }

    fun getDifferenceOfVersions(oldVersion: String, newVersion: String): VersionDifference {
        if (oldVersion.isNotInValidFormat() || newVersion.isNotInValidFormat())
            return VersionDifference.NONE

        val oldMajor = oldVersion
            .substringBefore('.')
            .removePrefix("v")
            .toInt()
        val oldSufficient = oldVersion
            .substringAfter('.')
            .substringBefore('.')
            .toInt()
        val oldMinor = oldVersion
            .substringAfterLast('.')
            .toInt()

        val newMajor = newVersion
            .substringBefore('.')
            .removePrefix("v")
            .toInt()
        val newSufficient = newVersion
            .substringAfter('.')
            .substringBefore('.')
            .toInt()
        val newMinor = newVersion
            .substringAfterLast('.')
            .toInt()

        return when {
            oldMajor < newMajor -> VersionDifference.MAJOR
            oldSufficient < newSufficient -> VersionDifference.SUFFICIENT
            oldMinor < newMinor -> VersionDifference.MINOR
            else -> VersionDifference.NONE
        }
    }


    private fun String.isNotInValidFormat(): Boolean {
        return !this.matches("^v?\\d+\\.\\d+\\.\\d+$".toRegex())
    }

    companion object {
        private const val VERSION_CHECK_DURATION_IN_SEC = 0
    }
}