package com.firstproject.androiddemofx.provider.trafficstats

import android.content.Context
import com.firstproject.androiddemofx.provider.base.BaseDbHelper

internal class TrafficStatsDbHelper(context: Context) : BaseDbHelper(context) {

    override fun getTableName() = TrafficStatsContract.TABLE_NAME

    override fun buildCreateTableSQL() = """
        CREATE TABLE ${TrafficStatsContract.TABLE_NAME} (
            ${TrafficStatsContract.TrafficStatsColumns._id} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${TrafficStatsContract.TrafficStatsColumns.cellId} INTEGER NOT NULL,
            ${TrafficStatsContract.TrafficStatsColumns.rsrp} REAL NOT NULL,
            ${TrafficStatsContract.TrafficStatsColumns.techType} TEXT NOT NULL,
            ${TrafficStatsContract.TrafficStatsColumns.bandNumber} TEXT,
            ${TrafficStatsContract.TrafficStatsColumns.timestamp} INTEGER NOT NULL,
            ${TrafficStatsContract.TrafficStatsColumns.transactionID} INTEGER,
            ${TrafficStatsContract.TrafficStatsColumns.DNSType} TEXT,
            ${TrafficStatsContract.TrafficStatsColumns.IPAddress} TEXT,
            ${TrafficStatsContract.TrafficStatsColumns.responseTime} INTEGER,
            ${TrafficStatsContract.TrafficStatsColumns.responseCode} INTEGER,
            ${TrafficStatsContract.TrafficStatsColumns.networkDetails} TEXT,
            ${TrafficStatsContract.TrafficStatsColumns.consumed} INTEGER
        )
    """.trimIndent()
}