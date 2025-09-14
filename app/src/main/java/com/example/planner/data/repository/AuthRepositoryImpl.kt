package com.example.planner.data.repository

import com.example.planner.data.localDataSource.LocalDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth,
                                             private val firestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun userIsLogged(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun performLogin(email: String, password: String): String {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    continuation.resume(it.user?.uid ?: "")
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun performRegister(firstname: String, lastname: String, email: String, password: String): String {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: ""
                    val userData = mapOf(
                        "firstname" to firstname,
                        "lastname" to lastname,
                        "email" to email
                    )
                    firestore.collection("users").document(uid)
                        .set(userData)
                        .addOnSuccessListener {
                            continuation.resume(uid)
                        }
                        .addOnFailureListener {
                            continuation.resumeWithException(it)
                        }
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun saveUserData(token: String) {
    }

    override suspend fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$")
        return emailRegex.matches(email)
    }

    override suspend fun areFieldsEmpty(firstname: String, lastname: String, email: String, password: String): Boolean {
        return (email.isEmpty() || password.isEmpty() || firstname.isEmpty() || lastname.isEmpty())
    }

    override suspend fun logout() {
         FirebaseAuth.getInstance().signOut()
    }

}