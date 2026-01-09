package com.example.myfirebase.repositori

import com.example.myfirebase.modeldata.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)
    suspend fun getSatuSiswa(id: String): Siswa?
    suspend fun editSatuSiswa(id: String, siswa: Siswa)
    suspend fun hapusSatuSiswa(id: String)
}

class FirebaseRepositorySiswa : RepositorySiswa {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {
            collection.get().await().documents.map { doc ->
                Siswa(
                    id = doc.id,
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun postDataSiswa(siswa: Siswa) {
        val docRef = if (siswa.id.isEmpty()) collection.document() else collection.document(siswa.id)
        val data = hashMapOf(
            "id" to docRef.id,
            "nama" to siswa.nama,
            "alamat" to siswa.alamat,
            "telpon" to siswa.telpon
        )
        docRef.set(data).await()
    }

    override suspend fun getSatuSiswa(id: String): Siswa? {
        return try {
            val query = collection.whereEqualTo("id", id).get().await()
            query.documents.firstOrNull()?.let { doc ->
                Siswa(
                    id = doc.id,
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            println("Gagal baca data siswa : ${e.message}")
            null
        }
    }

    override suspend fun editSatuSiswa(id: String, siswa: Siswa) {
        val docQuery = collection.whereEqualTo("id", id).get().await()
        val docId = docQuery.documents.firstOrNull()?.id ?: return
        collection.document(docId).set(
            mapOf(
                "id" to siswa.id,
                "nama" to siswa.nama,
                "alamat" to siswa.alamat,
                "telpon" to siswa.telpon
            )
        ).await()
    }

    override suspend fun hapusSatuSiswa(id: String) {
        val docQuery = collection.whereEqualTo("id", id).get().await()
        val docId = docQuery.documents.firstOrNull()?.id ?: return
        collection.document(docId).delete().await()
    }
}