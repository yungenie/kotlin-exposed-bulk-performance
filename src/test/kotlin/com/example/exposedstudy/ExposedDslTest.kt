package com.example.exposedstudy

import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import org.springframework.util.StopWatch
import java.math.BigDecimal
import java.time.LocalDateTime

class ExposedDslTest : ExposedTestSupport() {

    @Test
//    @Transactional
    fun `payment batch insert`() {

        val amounts = (1..100000).map { it.toBigDecimal() }
        val batchSize = 1000 // 배치 크기 설정

        performBatchInsertPayment(amounts, batchSize)
    }


    // 데이터베이스 배치 작업을 수행하는 함수
    private fun performBatchInsertPayment(amounts: List<BigDecimal>, batchSize: Int) {
        val stopWatch = StopWatch()
        stopWatch.start()

        // 리스트를 배치 크기에 맞게 분할하여 작업을 수행
        amounts.chunked(batchSize) { chunk ->
            transaction {
                Payments.batchInsert(
                        chunk,
                        ignore = false,
                        shouldReturnGeneratedValues = false
                ) { amount ->
                    this[Payments.orderId] = amount.toLong()
                    this[Payments.amount] = amount
                    this[Payments.createdAt] = LocalDateTime.now()
                    this[Payments.updatedAt] = LocalDateTime.now()
                }
            }
        }

        stopWatch.stop()
        println("Elapsed Time(s) : " + stopWatch.totalTimeSeconds)
        println("Elapsed Time(ms) : " + stopWatch.totalTimeMillis)
    }


}