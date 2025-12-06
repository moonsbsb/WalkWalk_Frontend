package com.example.pet_walk.ui.screen.attendance

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.pet_walk.data.Repository.AttendenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pet_walk.api.model.Attendence
import com.example.pet_walk.data.HiddenMessage
import com.example.pet_walk.data.MessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Collections.emptyList
import kotlin.random.Random

@HiltViewModel
class AttendenceViewModel @Inject constructor(
    private val repository: AttendenceRepository, private val messageRepository: MessageRepository
): ViewModel() {

    /* 출석 체크 */
    private val _attendence = MutableStateFlow<Attendence>(Attendence(0,0f,0))
    var attendence: StateFlow<Attendence> = _attendence
    fun getAttendence(token: String, month: String){
        viewModelScope.launch {
            try {
                val response = repository.getAttendence(token, month)
                val result = response.body()?.result
                if(response.isSuccessful && result!=null){
                    _attendence.value = result
                    Log.d("출석체크", "요청 성공\n걸은 거리:${result.distanceSum}\n걸은 시간:${result.minSum}\n횟수:${result.count}")
                }else{
                    Log.d("출석체크", "요청 실패: ${response.code()} - ${response.message()}")
                }
            }catch (e: Exception){ Log.e("출석체크", "에러: ${e.message}") }
        }
    }

    /* 랜덤 메세지 */
    private val _message = MutableStateFlow<List<String>>(emptyList())
    var message: StateFlow<List<String>> = _message

    init { getMessage() }

    private fun getMessage(){
        viewModelScope.launch {
            val today = LocalDate.now()
            val saveDate = messageRepository.getMessageAndDate()

            val updateMessage = saveDate == null || saveDate.date.month != today.month

            if(updateMessage){
                val newMessage = randomMessage()
                messageRepository.saveMessageAndDate(newMessage, today)
                _message.value = newMessage
            }else{
                _message.value = saveDate!!.messages
            }
        }
    }
    private fun randomMessage(): MutableList<String>{
        val messages = HiddenMessage().msg
        val uniqueMessage = messages
            .shuffled(Random)
            .take(4)
            .toMutableList()

        return uniqueMessage
    }

}