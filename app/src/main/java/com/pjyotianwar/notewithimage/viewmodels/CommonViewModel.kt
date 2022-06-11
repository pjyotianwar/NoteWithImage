package com.pjyotianwar.notewithimage.viewmodels

import android.content.SharedPreferences
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pjyotianwar.notewithimage.encrypt
import com.pjyotianwar.notewithimage.models.Notes
import com.pjyotianwar.notewithimage.models.Users
import com.pjyotianwar.notewithimage.repos.CommonRepository
import com.pjyotianwar.notewithimage.screens.LoginResult
import com.pjyotianwar.notewithimage.screens.SignUpResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.regex.Pattern
import javax.inject.Inject


@HiltViewModel
class CommonViewModel @Inject constructor(
    private val repository: CommonRepository,
    private val prefs: SharedPreferences
) : ViewModel() {

    private val _noteList = MutableStateFlow<List<Notes>>(emptyList())
    val noteList = _noteList.asStateFlow()
    var currentUserName = mutableStateOf("")
    var currentUserMail = mutableStateOf("")
    var editNote = mutableStateOf(false)
    var noteDetail = mutableStateOf(false)

    var noteTitle = mutableStateOf("")
    var noteDescription = mutableStateOf("")
    var noteTitleErrorState = mutableStateOf(false)
    var noteDescriptionErrorState = mutableStateOf(false)
    var noteImage = mutableStateListOf<String>()
    var noteId = mutableStateOf(-1)

    //SignUp variables
    var semailErrorState = mutableStateOf(false)
    var spasswordErrorState = mutableStateOf(false)
    var snameErrorState = mutableStateOf(false)
    var sphoneErrorState = mutableStateOf(false)
    var semail = mutableStateOf("")
    var spassword = mutableStateOf("")
    var sphone = mutableStateOf("")
    var sname = mutableStateOf("")
    val signUpResult = mutableStateOf(SignUpResult.Waiting)

    //Login variables
    val loginWithMail = mutableStateOf(true)
    var luserNameErrorState = mutableStateOf(false)
    var lpasswordErrorState = mutableStateOf(false)
    var lpassword = mutableStateOf("")
    var luserName = mutableStateOf("")
    val loginResult = mutableStateOf(LoginResult.Waiting)

    fun resetLoginResult(){
        loginResult.value = LoginResult.Waiting
    }

    fun resetSignUpResult(){
        signUpResult.value = SignUpResult.Waiting
    }

    fun isUserLoggedIn(): Boolean {
        val Islogin = prefs.getBoolean("Islogin", false)
        return Islogin
    }

    fun isMailValid(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isMobileValid(phone: String): Boolean {
//        return !TextUtils.isEmpty(umobile.value) && Patterns.PHONE.matcher(umobile.value).matches()
        val REG = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}\$"
        var PATTERN: Pattern = Pattern.compile(REG)
        return PATTERN.matcher(phone).find()
    }

    fun isNameValid(name: String): Boolean {
        return name.length in 4..25 && name.matches(Regex("[a-zA-Z]+"))
    }

    fun isPasswordValid(password: String, name: String): Boolean {
        val strongPass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$"
//        val ptn = "^[a-z](?=.*[0-9]){2,}(?=.*[A-Z]){2,}(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$"
//        val pt = "^[a-z]+(?=(.*[0-9]).{2,})(?=(.*[A-Z]).{2,})(?=(.*[!*)(?,.@#$%~^&+=]).{1,}).{8,15}$"

//        val ptrn =  "^[a-z](?=.*[~'!@#$%^&*-_=+/.,])(?=(.*[A-Z]){2,})(?=(.*[0-9]){2,}).{8,15}\$"
//        val pattern = Pattern.compile(ptrn)
//        pattern.matcher(password).find()
        return !password.contains(name, ignoreCase = true) &&
                password.matches(Regex(strongPass))
    }

    fun clearSignUp() {
        sphone.value = ""
        sname.value = ""
        semail.value = ""
        spassword.value = ""
        signUpResult.value = SignUpResult.Waiting
    }

    fun clearLogin() {
        luserName.value = ""
        lpassword.value = ""
        loginResult.value = LoginResult.Waiting
    }

    fun validateLogin(): Boolean {
        luserNameErrorState.value = luserName.value.isEmpty()
        lpasswordErrorState.value = lpassword.value.isEmpty()
        return luserName.value.isNotEmpty() && lpassword.value.length in 8..15
    }

    fun validateSignUp(): Boolean {
        sphoneErrorState.value = !isMobileValid(sphone.value)
        semailErrorState.value = !isMailValid(semail.value)
        snameErrorState.value = !isNameValid(sname.value)
        spasswordErrorState.value = !isPasswordValid(spassword.value, sname.value)

        return !sphoneErrorState.value && !semailErrorState.value && !snameErrorState.value && !spasswordErrorState.value
    }

    fun getUserDetails(){
        currentUserMail.value = prefs.getString("userMail", "").toString()
        currentUserName.value = prefs.getString("userName", "").toString()
        Log.d("User mail", "${currentUserMail.value}")
        Log.d("User name", "${currentUserName.value}")
    }

    fun signUpUser() {

        var uexists: Boolean? = null

        if (!validateSignUp()) {
            Log.d("SIGNUP", "Invalid entries")
            return
        }

        CoroutineScope(Dispatchers.IO + Job()).launch {
            val ue = repository.getUserByMail(semail.value)
            val up = repository.getUserByMobile(sphone.value)
            if (ue != null || up != null) {
                uexists = true
            }
        }.invokeOnCompletion {
            signUpResult.value = SignUpResult.UserExists
            if (uexists != true) {

                val pass = encrypt(spassword.value).toString()
//                Log.d("signUpUser encrypted password", "$pass")
                viewModelScope.launch {
                    repository.addUser(
                        Users(
                            userName = sname.value,
                            userMobile = sphone.value,
                            userEmail = semail.value,
                            userPassword = pass
                        )
                    )
                }.invokeOnCompletion {
                    Log.d("SIGNUP", "User does not Exist")
                    prefs.edit().putBoolean("Islogin", true).commit()
                    prefs.edit().apply {
                        putString("userMail", semail.value)
                        putString("userName", sname.value)
                    }.apply()

                    currentUserMail.value = semail.value
                    currentUserName.value = sname.value
                    Log.d("User mail", "${currentUserMail.value}")
                    Log.d("User name", "${currentUserName.value}")

                    clearSignUp()
                    clearLogin()
                    signUpResult.value = SignUpResult.Success
                }
            }
        }

    }

    fun login(){

        var uexists: Boolean? = null
        var uvalid: Boolean? = null

        if (!validateLogin())
            return

        var usr: Users?=null

        CoroutineScope(Dispatchers.IO+ Job()).launch {
            if (loginWithMail.value)
                usr = repository.getUserByMail(luserName.value)
            else
                usr = repository.getUserByMobile(luserName.value)
            uexists = usr != null
            if (usr!=null){
                uvalid = usr?.userPassword.equals(encrypt(lpassword.value))
            }
            if (uvalid==true){
                prefs.edit().apply {
                    putString("userName", usr?.userName)
                    putString("userMail", usr?.userEmail)
                }.apply()

                currentUserMail.value = usr?.userEmail.toString()
                currentUserName.value = usr?.userName.toString()
                Log.d("User mail", "${currentUserMail.value}")
                Log.d("User name", "${currentUserName.value}")

                prefs.edit().putBoolean("Islogin", true).commit()
            }
        }.invokeOnCompletion {
            Log.d("login uexists uvalid", "$uexists $uvalid")

            if (uexists==true){
                if (uvalid==true){

                    currentUserMail.value = usr?.userEmail.toString()
                    currentUserName.value = usr?.userName.toString()
                    Log.d("User mail", "${currentUserMail.value}")
                    Log.d("User name", "${currentUserName.value}")

                    clearLogin()
                    clearSignUp()
                    loginResult.value=LoginResult.Success
                }
                else{
                    loginResult.value=LoginResult.Wrong
                }
            }
            else{
                loginResult.value=LoginResult.NoUser
            }
        }

    }

    fun logout(): Boolean {
        prefs.edit().putBoolean("Islogin", false).commit()
        prefs.edit().apply {
            putString("userName", "")
            putString("userMail", "")
        }.apply()

        currentUserName.value = ""
        currentUserMail.value = ""

        return true
    }

    fun resetNoteValues() {
        noteId.value = -1
        noteImage.clear()
        noteDescription.value = ""
        noteTitle.value = ""
        editNote.value = false
    }

    fun setNoteValues(notes: Notes) {
        editNote.value = true
        noteDescription.value = notes.noteDescription
        noteTitle.value = notes.noteTitle
        notes.noteImages.split(";;", limit = 10).filter { it.isNotEmpty() }.forEach {
            noteImage.add(it)
        }
    }

    fun addImageUri(path: String?): Boolean{
        return if (noteImage.size>=10 || noteImage.contains(path))
            false
        else{
            if (path!=null) {
                noteImage.add(path)
                true
            } else
                false
        }
    }

    fun removeImageUri(path: String?){
        noteImage.remove(path)
    }

    fun getImageString(): String{
        var img = ""
        noteImage.forEach {
            img+=it+";;"
        }
        return img
    }

//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getAllNotes(user.value).distinctUntilChanged().collect { listOfNotes ->
//                _noteList.value = listOfNotes
//                if (listOfNotes.isNullOrEmpty())
//                    Log.d("Empty Notes", ": Empty list")
//            }
//        }
//    }

    fun getNotes() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNotes(currentUserMail.value).distinctUntilChanged().collect { listOfNotes ->
            _noteList.value = listOfNotes
            if (listOfNotes.isNullOrEmpty())
                Log.d("Get Notes", ": Empty list")
            Log.d("Get Notes", "${currentUserMail.value}")
        }
    }

    fun validateNote(): Boolean{
        noteTitleErrorState.value = !validateTitle(noteTitle.value)
        noteDescriptionErrorState.value = !validateDescription(noteDescription.value)

        Log.d("validateNote", "${noteTitleErrorState.value} ${noteDescriptionErrorState.value}")

        if (!noteDescriptionErrorState.value && !noteTitleErrorState.value){
            if (noteDetail.value) {
                Log.d("validateNote", "update")
                updateNote()
            }
            else {
                Log.d("validateNote", "add")
                addNote()
            }
            return true
        }
        return false
    }

    fun addNote() = viewModelScope.launch {
        repository.addNote(
            Notes(
                noteTitle = noteTitle.value,
                noteDescription = noteDescription.value,
                noteImages = getImageString(),
                user = currentUserMail.value
            )
        )
    }

    fun updateNote() = viewModelScope.launch {
        repository.updateNote(
            Notes(
                noteId = noteId.value,
                noteTitle = noteTitle.value,
                noteDescription = noteDescription.value,
                noteImages = getImageString(),
                user = currentUserMail.value
            )
        )
        editNote.value = false
    }

    fun removeNote(notes: Notes) = viewModelScope.launch {
        repository.deleteNote(notes)
    }

    fun validateTitle(title: String): Boolean{
        Log.d("validateTitle", "${title.length} ${title.length in 5..100}")
        return title.length in 5..100
    }

    fun validateDescription(desc: String): Boolean{
        Log.d("validateDesc", "${desc.length} ${desc.length in 100..1000}")
        return desc.length in 100..1000
    }
}