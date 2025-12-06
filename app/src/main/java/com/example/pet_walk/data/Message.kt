package com.example.pet_walk.data

import java.time.LocalDate
import javax.inject.Singleton

data class Message(
    val messages: List<String>,
    val date: LocalDate
)
@Singleton
interface MessageRepository{
    /* 메세지 저장 */
    suspend fun saveMessageAndDate(messages: List<String>, date: LocalDate)
    /* 메세지 불러오기 */
    suspend fun getMessageAndDate(): Message?
}
