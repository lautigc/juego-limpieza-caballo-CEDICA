package com.cedica.cedica.data.repository.interfaces

import kotlinx.coroutines.flow.Flow


interface BaseRepository<T> {

    suspend fun insert(entity: T): Long

    suspend fun insert(entities: List<T>)

    suspend fun update(entity: T)

    suspend fun update(entities: List<T>)

    suspend fun delete(entity: T)

    suspend fun delete(entities: List<T>)

    fun getAll(): Flow<List<T>>

    suspend fun getByID(id: Long): T
}