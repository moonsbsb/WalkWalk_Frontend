package com.example.pet_walk.data.Repository

import com.example.pet_walk.data.remote.api.AttendenceApi
import com.example.pet_walk.data.remote.model.AttendenceResponse
import retrofit2.Response
import javax.inject.Inject

class AttendenceRepository @Inject constructor(private val api: AttendenceApi) {
    /* 출석 확인 */
    suspend fun getAttendence(token: String, month: String): Response<AttendenceResponse>{
        return api.getAttendence(token, month)
    }
}