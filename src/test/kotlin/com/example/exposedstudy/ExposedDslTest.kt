package com.example.exposedstudy

import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.util.StopWatch
import java.math.BigDecimal
import java.time.LocalDateTime

class ExposedDslTest : ExposedTestSupport() {

    @Test
//    @Transactional
    @DisplayName("kotlin exposed batch insert 성공")
    fun `payment batch insert`() {

        val amounts = (1..10000).map { it.toBigDecimal() }

        /* 배치 크기 설정
         * 한번에 처리되는 데이터 양을 줄여서 배치 작업 효율성 향상. 크기 조절해서 성능 최적화. 최적의 값 찾기
         */
        val batchSize = 1000
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
                        ignore = false, // 중복되는 행이 발생했을 때 무시하는 옵션 (true : 진행, false : 오류 발생)
                        shouldReturnGeneratedValues = false // 자동 생성된 ID 을 반환하지 않음.
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