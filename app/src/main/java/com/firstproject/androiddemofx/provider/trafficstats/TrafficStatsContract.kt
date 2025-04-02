
package com.firstproject.androiddemofx.provider.trafficstats

import android.net.Uri
import android.util.Log
import com.firstproject.androiddemofx.provider.DatabaseInfo.VERSION

class TrafficStatsContract {
    companion object {

        private const val TAG: String = "TrafficStatsContract"
        private const val SCHEME = "content://"

        /**
         * Old AUTHORITY
         * TODO("Remove from TRD when Old Client version supporting TRD 2.8 are not more in production")
         *
         * private const val AUTHORITY: String = "com.tmobile.oem.echolocate.system.provider"
         */

        /**
         * New AUTHORITY according to android guidelines.
         * A list of one or more URI authorities that identify data offered by the content provider. List multiple
         * authorities by separating their names with a semicolon. To avoid conflicts, use a Java-style naming
         * convention for authority names, such as "com.tmobile.oem.echolocate.system.provider.trafficstats.TrafficStatsContentProvider".
         * Typically, it's the name of the ContentProvider subclass that implements the provider.
         */
        private const val AUTHORITY: String =
            "com.firstproject.androiddemofx.provider.trafficstats.TrafficStatsContentProvider"
        
        const val TABLE_NAME = "trafficstats"
        private const val TABLE = TABLE_NAME
        
        val CONTENT_URI: Uri = Uri.parse("$SCHEME$AUTHORITY/$TABLE")

        const val SINGLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.item/vnd." + AUTHORITY + "_" + TABLE + "_" + VERSION
        const val MULTIPLE_RECORD_MIME_TYPE =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + "_" + TABLE + "_" + VERSION

        init {
            Log.d(TAG, "SCHEME = $SCHEME")
            Log.d(TAG, "AUTHORITY = $AUTHORITY")
            Log.d(TAG, "TABLE = $TABLE")
            Log.d(TAG, "VERSION = $VERSION")
        }

    }

    interface TrafficStatsColumns {
        companion object {
            /**
             * This is Unique ID in the Table
             * Unique ID in the table is for each event data from the table.
             * If there is a buffer limitation, device can use pages and flush remove oldest one
             * NFA review the criteria of flushing condition from device free from logic of diagnositcs delete rows
             */
            const val _id = "_id"

            /**
             * Current cellId to which device is connected
             *
             */
            const val cellId = "cellId"

            /**
             * RSRP level on this carrier in dBm
             *  Applicable to LTE and NR carriers
             *  If the carrier is a NR carrier, use SS-RSRP value for this field.
             *  Example: -90
             */
            const val rsrp = "rsrp"

            /**
             * Current DNS Resolver NetworkType
             * Refer to private String techType (GID-513376)
             * Example: NR, LTE, 3G, 2G
             */
            const val techType = "techType"

            /**
             * Current DNS Resolver bandNumber
             * Refer to private String techType (GID-513335)
             * Example: NR, LTE, 3G, 2G
             */
            const val bandNumber = "bandNumber"

            /**
             * Timestamp in UNIX epoch time down to milliseconds
             * Example: 1543320466
             */
            const val timestamp = "timestamp"

            /**
             * each transaction should have uniqueID to differentiate transaction
             *  field for the DNS protocol is only 16 bits in length, so this value can range from 0 through 65535
             *  38597
             */
            const val transactionID = "transactionID"

            /**
             * Type : String
             * A, AAAA
             * Refer to return value is 1 (IPv4) or 28 (IPv6) https://developer.android.com/reference/android/net/DnsResolver#TYPE_A
             */
            const val DNSType = "DNSType"

            /**
             * IP address of the origin of request
             * @return
             * Example 1:
             * ```
             * {
             *    "src": {
             *       "ip": "983c2cc0dcc05f2b68c4287040cfc73"
             *     },
             *     "dst": {
             *       "ip": "983c2cc0dcc05f2b68c4287040cfc74"
             *     }
             *  }
             *  If device does not support port number and report only FQDN destination IP addresses,
             *  then this is the example
             *  Example 2:
             *  {
             *   "ip": [
             *     "983c2cc0dcc05f2b68c4287040cfc79",
             *     "983c2cc0dcc05f2b68c4287040cfc7a",
             *     "983c2cc0dcc05f2b68c4287040cfc7b"
             *   ]
             *   }
             * ```
             */
            const val IPAddress = "IPAddress"

            /**
             * Time difference between request & response
             * Type : Long
             *  Example: response time 0.46, 0, -1, -2
             *  0 means when DNS response is not received, return value shall be 0 and responseCode value in the below show timeout code 408 or 504 accordingly.
             *  -1 means device does not support this
             *  -2 means device has failed to get the value from SW
             */
            const val responseTime = "responseTime"

            /**
             * This is transactionInformation such as Information
             * Type : Int
             * This is used for DNS response only .because DNS response has response code.
             * DNS response codes indicate either success or failure for the DNS query.
             * Example: 2xx ,3xx, 4xx, 5xx.i.e. A successful HTTP response with a 2xx status code (see Section 6.3 of [RFC7231])
             *
             */
            const val responseCode = "responseCode"

            /**
             * The BSSID (basic service set identification) is the Mac-Address of the
             * router/access-point is a unique identifier of the Wi-Fi network in the following
             * format "00:00:00:00:00:00", which is used from the machine to address it.
             * Type : String
             */
            const val networkDetails = "networkDetails"

            /**
             * This would be used by T-Mobile to Sync the data to local
             * default is zero
             * Type : Int
             */
            const val consumed = "consumed"
        }
    }
}