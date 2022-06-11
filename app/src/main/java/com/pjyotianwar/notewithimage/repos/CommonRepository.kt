package com.pjyotianwar.notewithimage.repos

import com.pjyotianwar.notewithimage.daos.NoteDao
import com.pjyotianwar.notewithimage.daos.UserDao
import com.pjyotianwar.notewithimage.models.Notes
import com.pjyotianwar.notewithimage.models.Users
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommonRepository @Inject constructor(
    private val userDao: UserDao,
    private val noteDao: NoteDao
) {
    fun getAllNotes(user: String): Flow<List<Notes>> = noteDao.getAllNotes(user)

//    fun getAllUsers(): Flow<List<Users>> = userDao.getAllUsers()

    fun getUserByMail(mail: String): Users? = userDao.getUserWithMail(mail)

    fun getUserByMobile(mobile: String): Users? = userDao.getUserWithMobile(mobile)

    suspend fun addUser(users: Users) = userDao.addUser(users)

    suspend fun addNote(notes: Notes) = noteDao.addNote(notes)

//    suspend fun updateUser(users: Users) = userDao.updateUser(users)

    suspend fun updateNote(notes: Notes) = noteDao.updateNote(notes)

//    suspend fun deleteUser(users: Users) = userDao.deleteUser(users)

    suspend fun deleteNote(notes: Notes) = noteDao.deleteNote(notes)
}
