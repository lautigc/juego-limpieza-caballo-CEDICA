package com.cedica.cedica.data.repository.local

import com.cedica.cedica.data.repository.interfaces.TherapistRepository
import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.TherapistDao
import com.cedica.cedica.data.user.UserTherapist
import kotlinx.coroutines.flow.Flow

class LocalTherapistRepository(private val dao: TherapistDao): TherapistRepository {
    override suspend fun insert(entity: Therapist): Long {
        return dao.insert(entity)
    }

    override suspend fun insert(entities: List<Therapist>) {
        dao.insert(entities)
    }

    override suspend fun update(entity: Therapist) {
        dao.update(entity)
    }

    override suspend fun update(entities: List<Therapist>) {
        dao.update(entities)
    }

    override suspend fun delete(entity: Therapist) {
        dao.delete(entity)
    }

    override suspend fun delete(entities: List<Therapist>) {
        dao.delete(entities)
    }

    override fun getAll(): Flow<List<Therapist>> {
        return dao.getAll()
    }

    override suspend fun getByID(id: Int): Therapist {
        return dao.getByID(id)
    }

    override fun getAllUserTherapist(): Flow<List<UserTherapist>> {
        return dao.getAllUserTherapist()
    }
}