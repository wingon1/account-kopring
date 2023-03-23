package com.sori.account.common.generator

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable
import java.net.NetworkInterface
import java.security.SecureRandom
import java.time.Instant
import java.util.*
import kotlin.math.pow

/**
 * Distributed Sequence Generator.
 * Inspired by <a href="https://github.com/twitter/snowflake/tree/snowflake-2010">Twitter snowflake</a>
 *
 * This class should be used as a Singleton.
 * Make sure that you create and reuse a Single instance of SequenceGenerator per node in your distributed system cluster.
 *
 * @see <a href="https://www.callicoder.com/distributed-unique-id-sequence-number-generator/">article</a> for the reference
 */
class SequenceGenerator : IdentifierGenerator {
    private var nodeId: Int

    private var lastTimestamp: Long

    private var sequence: Long

    companion object {
        private const val UNUSED_BITS: Int = 1 // Sign bit, Unused (always set to 0)
        private const val EPOCH_BITS: Int = 41

        private const val NODE_ID_BITS: Int = 10
        private const val SEQUENCE_BITS: Int = 12

        private val maxNodeId: Int = (2.0.pow(NODE_ID_BITS) - 1.0).toInt()

        private val maxSequence: Int = (2.0.pow(SEQUENCE_BITS) - 1.0).toInt()

        private const val CUSTOM_EPOCH: Long = 1673838000000L

        private fun timestamp(): Long {
            return Instant.now().toEpochMilli() - CUSTOM_EPOCH
        }
    }

    /**
     * Let the class generates a node id
     */
    constructor() {
        this.lastTimestamp = -1L
        this.nodeId = createNodeId()
        this.sequence = 0
    }

    /**
     * create instance with a node id
     */
    constructor(nodeId: Int) : this() {
        this.lastTimestamp = -1L
        if (nodeId < 0 || nodeId > maxNodeId) {
            throw IllegalArgumentException(String.format("Node ID must be between %d and %d", 0, maxNodeId))
        }
        this.nodeId = nodeId
        this.sequence = 0
    }

    @Synchronized
    fun nextId(): Long {
        var currentTimestamp: Long = timestamp()

        if (currentTimestamp < lastTimestamp) {
            throw IllegalStateException("Invalid System Clock!")
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1).and(maxSequence.toLong())
            if (sequence == 0L) {
                // Sequence Exhausted, wait till next millisecond.
                currentTimestamp = waitNextMillis(currentTimestamp)
            }
        } else {
            sequence = 0
        }

        lastTimestamp = currentTimestamp

        return currentTimestamp.shl(NODE_ID_BITS + SEQUENCE_BITS)
            .or(nodeId.shl(SEQUENCE_BITS).toLong())
            .or(sequence)
    }

    /**
     * Block and wait till next millisecond
     */
    private fun waitNextMillis(currentTimestamp: Long): Long {
        var nextTimestamp: Long = currentTimestamp
        while (nextTimestamp == lastTimestamp) {
            nextTimestamp = timestamp()
        }
        return nextTimestamp
    }

    /**
     * Create new node id based on hardware address of the network interface
     */
    private fun createNodeId(): Int {
        var nodeId: Int
        try {
            val sb: StringBuilder = StringBuilder()
            val networkInterfaces: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val mac: ByteArray? = networkInterface.hardwareAddress
                if (mac != null) {
                    for (b: Byte in mac) {
                        sb.append(String.format("%02X", b))
                    }
                }
            }
            nodeId = sb.toString().hashCode()
        } catch (ex: Exception) {
            nodeId = (SecureRandom.getInstanceStrong().nextInt())
        }
        nodeId = nodeId.and(maxNodeId)
        return nodeId
    }

    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Serializable {
        return this.nextId()
    }
}
