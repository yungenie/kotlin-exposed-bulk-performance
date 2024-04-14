package com.example.exposedstudy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ExposedStudyApplication

fun main(args: Array<String>) {
    runApplication<ExposedStudyApplication>(*args)
}