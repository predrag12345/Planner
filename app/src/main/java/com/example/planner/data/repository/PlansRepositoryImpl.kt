package com.example.planner.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PlansRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlansRepository {

    override suspend fun addPlan(userId: String, plan: Plan): Result<String> {
        return try {
            val docRef = firestore.collection("users")
                .document(userId)
                .collection("plans")
                .add(
                    mapOf(
                        "title" to plan.title,
                        "description" to plan.description,
                        "startDate" to plan.startDate,
                        "endDate" to plan.endDate,
                        "notificationEnabled" to plan.notificationEnabled
                    )
                )
                .await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getPlansForUser(userId: String): Flow<List<Plan>> = callbackFlow {
        val listener = firestore.collection("users")
            .document(userId)
            .collection("plans")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val plans = snapshot?.documents?.map { doc ->
                    Plan(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        startDate = doc.getString("startDate") ?: "",
                        endDate = doc.getString("endDate") ?: "",
                        notificationEnabled = doc.getBoolean("notificationEnabled") ?: false
                    )
                } ?: emptyList()

                trySend(plans)
            }

        awaitClose { listener.remove() }
    }
}

data class Plan(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val notificationEnabled: Boolean = false
)
