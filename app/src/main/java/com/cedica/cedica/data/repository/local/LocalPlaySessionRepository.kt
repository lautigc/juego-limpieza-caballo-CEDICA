package com.cedica.cedica.data.repository.local;

import com.cedica.cedica.data.repository.interfaces.PlaySessionRepository
import com.cedica.cedica.data.user.PlaySession
import com.cedica.cedica.data.user.PlaySessionDao
import kotlinx.coroutines.flow.Flow

class LocalPlaySessionRepository(private val dao: PlaySessionDao): PlaySessionRepository {

    override suspend fun insert(entity: PlaySession): Long {
        return dao.insert(entity)
    }

    override suspend fun insert(entities: List<PlaySession>) {
        dao.insert(entities)
    }

    override suspend fun update(entity: PlaySession) {
        dao.update(entity)
    }

    override suspend fun update(entities: List<PlaySession>) {
        dao.update(entities)
    }

    override suspend fun delete(entity: PlaySession) {
        dao.delete(entity)
    }

    override suspend fun delete(entities: List<PlaySession>) {
        dao.delete(entities)
    }

    override fun getAll(): Flow<List<PlaySession>> {
        return dao.getAllPlaySessions()
    }

    override suspend fun getByID(id: Long): PlaySession {
        return dao.getByID(id)
    }
}
