package mx.qsistemas.infracciones.db_web.managers

import android.annotation.SuppressLint
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import mx.qsistemas.infracciones.Application
import mx.qsistemas.infracciones.utils.Validator


@SuppressLint("StaticFieldLeak")
object CatalogsFirebaseManager {
    suspend fun getValue(reference: String, typeCollection: String): String {
        val value: String
        val document: Task<DocumentSnapshot>? = if (Validator.isNetworkEnable(Application.getContext())) {
            Application.firestore?.collection(typeCollection)?.document(reference)?.get(Source.SERVER)

        } else {
            Application.firestore?.collection(typeCollection)?.document(reference)?.get(Source.CACHE)
        }
        value = document?.await()?.get("value").toString()
        return value
    }
}