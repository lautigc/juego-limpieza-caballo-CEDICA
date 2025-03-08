package com.cedica.cedica.data.repository.local

import com.cedica.cedica.data.repository.interfaces.PatientRepository
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.PatientDao
import com.cedica.cedica.data.user.UserPatient
import kotlinx.coroutines.flow.Flow

class LocalPatientRepository(private val dao: PatientDao): PatientRepository {
    override suspend fun insert(entity: Patient) {
        dao.insert(entity)
    }

    override suspend fun insert(entities: List<Patient>) {
        dao.insert(entities)
    }

    override suspend fun update(entity: Patient) {
        dao.update(entity)
    }

    override suspend fun update(entities: List<Patient>) {
        dao.update(entities)
    }

    override suspend fun delete(entity: Patient) {
        dao.delete(entity)
    }

    override suspend fun delete(entities: List<Patient>) {
        dao.delete(entities)
    }

    override fun getAll(): Flow<List<Patient>> {
        return dao.getAll()
    }

    override suspend fun getByID(id: Long): Patient {
        return dao.getByID(id)
    }

    override fun getAllUserPatient(): Flow<List<UserPatient>> {
        return dao.getAllUserPatient()
    }
}