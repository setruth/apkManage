package com.setruth.apkmanage.utils





class SnowflakeId{
    private val epoch = 1609459200000L // 2021-01-01 00:00:00
    private val workerId: Long = 1
    private var sequence = 0L
    private var lastTimestamp = -1L

    fun generateId(datacenterId: Long): String {
        var timestamp = System.currentTimeMillis()
        synchronized(this) {
            if (timestamp < lastTimestamp) {
                throw RuntimeException("系统时钟无效")
            }

            if (timestamp == lastTimestamp) {
                // 当前毫秒内的序列号已经用完
                sequence = (sequence + 1) and 4095L
                if (sequence == 0L) {
                    // 当前毫秒内的序列号已经用完，等待下一毫秒
                    timestamp = waitNextMillis(lastTimestamp)
                }
            } else {
                // 新的毫秒开始，序列号重置为0
                sequence = 0L
            }
            lastTimestamp = timestamp
        }

        // 生成雪花ID
        return (((timestamp - epoch) shl 22) or (datacenterId shl 17) or (workerId shl 12) or sequence).toString()
    }

    private fun waitNextMillis(lastTimestamp: Long): Long {
        var timestamp = System.currentTimeMillis()
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis()
        }
        return timestamp
    }
}