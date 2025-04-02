package com.firstproject.androiddemofx.provider.trafficstats

import com.firstproject.androiddemofx.provider.ProviderInfo

class TrafficStatsContentProviderInfo: ProviderInfo {
    override val authorities: List<String> = listOf(
        "com.firstproject.androiddemofx.provider",
        "com.firstproject.androiddemofx.provider.trafficstats.TrafficStatsContentProvider"
    )
    override val directBootAware: Boolean = true
    override val enabled: Boolean = true
    override val exported: Boolean = true
    override val grantUriPermissions: Boolean = true
    override val initOrder: Int = 99
    override val label: String = "Echolocate System TrafficStats Provider"
    override val multiprocess: Boolean = true
    override val name: String = "TrafficStatsContentProvider"
    override val permission: String = ""
    override val process: String = ":trafficStatsProvider"
    override val readPermission: String = "com.firstproject.androiddemofx.provider.trafficstats.TrafficStatsContentProvider.READ_TRAFFIC_STATS"
    override val syncable: Boolean = true
    override val writePermission: String = "com.firstproject.androiddemofx.provider.trafficstats.TrafficStatsContentProvider.WRITE_TRAFFIC_STATS"
    override val protectionLevel: String = "signatureOrSystem"
}