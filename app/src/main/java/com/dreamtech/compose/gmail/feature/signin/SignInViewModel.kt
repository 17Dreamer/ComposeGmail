package com.dreamtech.compose.gmail.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamtech.compose.gmail.core.domain.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) : ViewModel() {


    fun signIn(email:String, password: String, onSuccess:()->Unit){
        viewModelScope.launch {
            if(signInUseCase(email, password)){
                onSuccess()
            }

        }
    }
}