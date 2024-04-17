package com.seatcode.seatroboticmowercontrol.infrastructure.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("com.seatcode.seatroboticmowercontrol.*")
class SeatRoboticMowerControlApplication

fun main(args: Array<String>) {
	runApplication<SeatRoboticMowerControlApplication>(*args)
}
