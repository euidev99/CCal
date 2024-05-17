package com.capstone.ccal.common

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * 베이스로 혹시나 레포지토리가 다른 내용을 쓸 수도 있으니,
 * 서비스의 분기 / api, db 를 분기하고자 BaseRepository 로 작업
 * 한 콜렉션을 가져오는 레포지토리
 */
open class BaseRepository<T: Any>(
    private val collectionName: String,
    private val documentClass: Class<T>) {

    private val db = FirebaseFirestore.getInstance()
    protected val collectionReference: CollectionReference = db.collection(collectionName)

    suspend fun addDocument(documentData: T) : RepoResult<Boolean> = withContext(Dispatchers.IO) {
        try {
            //응답 처리 보류..
            collectionReference.add(documentData).await()
            RepoResult.Success(true)
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

    suspend fun addDocumentWithId(id: String, documentData: T) : RepoResult<Boolean> = withContext(Dispatchers.IO) {
        try {
            //응답 처리 보류..
//            collectionReference.add(documentData).await()
            collectionReference.document(id).set(documentData).await()

            RepoResult.Success(true)
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

    suspend fun getAllDocuments(): RepoResult<List<T>> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = db.collection(collectionName).get().await()
            val dataList = querySnapshot.toObjects(documentClass)
            RepoResult.Success(dataList)
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

    suspend fun getDocumentById(documentId: String): RepoResult<T> = withContext(Dispatchers.IO) {
        try {
            val documentSnapshot = collectionReference.document(documentId).get().await()
            if (documentSnapshot.exists()) {
                val documentData = documentSnapshot.toObject(documentClass)
                if (documentData != null) {
                    RepoResult.Success(documentData)
                } else {
                    RepoResult.Error(Exception("Document not found"))
                }
            } else {
                RepoResult.Error(Exception("Document not found"))
            }
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

//    suspend fun getSecondCollectionById(firstDocumentId: String, secondCollectionId: String): RepoResult<T> = withContext(Dispatchers.IO) {
//        try {
//            // 첫 번째 컬렉션의 문서 참조 가져오기
//            val firstDocumentRef = collectionReference.document(firstDocumentId)
//
//            // 두 번째 컬렉션 참조 가져오기
//            val secondCollectionRef = firstDocumentRef.collection("secondCollection")
//
//            // 특정 문서 가져오기 (예: 두 번째 컬렉션의 문서 ID가 "secondDocumentId"인 경우)
//            val documentSnapshot = secondCollectionRef.document(secondDocumentId).get().await()
//
//            if (documentSnapshot.exists()) {
//                val documentData = documentSnapshot.toObject(documentClass)
//                if (documentData != null) {
//                    RepoResult.Success(documentData)
//                } else {
//                    RepoResult.Error(Exception("Document not found"))
//                }
//            } else {
//                RepoResult.Error(Exception("Document not found"))
//            }
//        } catch (e: Exception) {
//            RepoResult.Error(e)
//        }
//    }

    suspend fun addDocumentToSecondCollection(firstDocumentId: String, subCollectionName: String,  data: T): RepoResult<String> = withContext(Dispatchers.IO) {
        try {
            val firstDocumentRef = collectionReference.document(firstDocumentId)
            val secondCollectionRef = firstDocumentRef.collection(subCollectionName)

            // 새로운 문서 추가
            val newDocumentRef = secondCollectionRef.add(data).await()

            RepoResult.Success(newDocumentRef.id)
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }

    suspend fun getAllDocumentsFromSecondCollection(firstDocumentId: String, subCollectionName: String ): RepoResult<List<T>> = withContext(Dispatchers.IO) {
        try {
            val firstDocumentRef = collectionReference.document(firstDocumentId)
            val secondCollectionRef = firstDocumentRef.collection(subCollectionName)

            val documentsSnapshot = secondCollectionRef.get().await()

            if (!documentsSnapshot.isEmpty) {
                val documentList = mutableListOf<T>()
                for (document in documentsSnapshot.documents) {
                    val documentData = document.toObject(documentClass)
                    documentData?.let {
                        documentList.add(it)
                    }
                }
                RepoResult.Success(documentList)
            } else {
                RepoResult.Error(Exception("No documents found in the second collection"))
            }
        } catch (e: Exception) {
            RepoResult.Error(e)
        }
    }


    suspend fun updateDocument(documentId: String, updates: Map<String, Any>): RepoResult<Unit> = withContext(Dispatchers.IO) {
        try {
            collectionReference.document(documentId).update(updates).await()
            RepoResult.Success(Unit)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error updating document: $documentId, ${e.message}", e))
        }
    }

    suspend fun deleteDocument(documentId: String): RepoResult<Unit> = withContext(Dispatchers.IO) {
        try {
            collectionReference.document(documentId).delete().await()
            RepoResult.Success(Unit)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error deleting document: $documentId, ${e.message}", e))
        }
    }

    suspend fun deleteDocumentByField(fieldName: String, value: Any): RepoResult<Unit> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = collectionReference.whereEqualTo(fieldName, value).get().await()
            for (document in querySnapshot.documents) {
                // 각 문서를 삭제
                document.reference.delete().await()
            }
            RepoResult.Success(Unit)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error deleting documents by field: $fieldName, ${e.message}", e))
        }
    }


    suspend fun getDocumentsByField(fieldName: String, value: Any): RepoResult<List<T>> = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = collectionReference.whereEqualTo(fieldName, value).get().await()
            val dataList = querySnapshot.toObjects(documentClass)
            RepoResult.Success(dataList)
        } catch (e: Exception) {
            RepoResult.Error(Exception("Error fetching documents by field: $fieldName, ${e.message}", e))
        }
    }

}
