package com.pjyotianwar.notewithimage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.pjyotianwar.notewithimage.daos.NoteDao
import com.pjyotianwar.notewithimage.daos.UserDao
import com.pjyotianwar.notewithimage.models.CommonDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideUserDao(commonDatabase: CommonDatabase): UserDao =
        commonDatabase.userDao()

    @Singleton
    @Provides
    fun provideNoteDao(commonDatabase: CommonDatabase): NoteDao =
        commonDatabase.noteDao()

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("NoteImagePreferences", MODE_PRIVATE)

//    @Singleton
//    @Provides
//    fun providesEncryptedPreference(@ApplicationContext context: Context): EncryptedSharedPreferences {
//        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
//
//        val sharedPref = EncryptedSharedPreferences.create(
//            "encrypted_preferences", // fileName
//            masterKeyAlias, // masterKeyAlias
//            context, // context
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
//        )
//    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): CommonDatabase =
        Room.databaseBuilder(
            context,
            CommonDatabase::class.java,
            "common_db"
        )
            .fallbackToDestructiveMigration()
            .build()
}