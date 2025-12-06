package com.example.pet_walk.data.Repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pet_walk.data.Message
import com.example.pet_walk.data.MessageRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

val KEY_MESSAGES = stringPreferencesKey("key_message_list")
val KEY_GEN_DATE = stringPreferencesKey("key_generation_date")

@Singleton
class MessageRepositoryImpl @Inject constructor(private val dataStore: DataStore<Preferences>):MessageRepository{

    private val gson = Gson()
    override suspend fun saveMessageAndDate(messages: List<String>, date: LocalDate) {
        dataStore.edit { preferences ->
            val messageJson = gson.toJson(messages)

            preferences[KEY_MESSAGES] = messageJson
            preferences[KEY_GEN_DATE] = date.toString()
        }
    }

    override suspend fun getMessageAndDate(): Message? {

        val preferences = dataStore.data.firstOrNull()
        if(preferences==null) return null

        val messageJson = preferences[KEY_MESSAGES]
        val dateString = preferences[KEY_GEN_DATE]
        if(messageJson.isNullOrEmpty() || dateString.isNullOrEmpty()) return null

        // 객체 변환
        val type = object : TypeToken<List<String>>() {}.type
        val messages = gson.fromJson<List<String>>(messageJson, type)

        val date = LocalDate.parse(dateString)

        return Message(messages, date)
    }

}