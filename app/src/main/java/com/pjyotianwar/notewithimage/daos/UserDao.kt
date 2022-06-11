package com.pjyotianwar.notewithimage.daos

import androidx.room.*
import com.pjyotianwar.notewithimage.models.Users
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("Select * from users_table")
    fun getAllUsers(): Flow<List<Users>>

    @Query("Select * from users_table where user_email = :mail")
    fun getUserWithMail(mail: String): Users?

    @Query("Select * from users_table where user_mobile = :mobile")
    fun getUserWithMobile(mobile: String): Users?

    @Insert(entity = Users::class)
    suspend fun addUser(users: Users)

    @Update(entity = Users::class)
    suspend fun updateUser(users: Users)

    @Delete(entity = Users::class)
    suspend fun deleteUser(users: Users)
}
