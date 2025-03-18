package com.cedica.cedica.data.repository.local;

import com.cedica.cedica.core.guestData.GuestData
import com.cedica.cedica.core.guestData.isGuestUser
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.User
import com.cedica.cedica.data.user.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LocalUserRepository(
    private val dao: UserDao,
    private val guestData: GuestData,
): UserRepository {

    override suspend fun existsByFullName(firstName: String, lastName: String): Boolean {
        return dao.existsByFullName(firstName, lastName)
    }

    override suspend fun existByUsername(username: String): Boolean {
        return dao.existByUsername(username)
    }

    override suspend fun insert(entity: User): Long {
        return dao.insert(entity)
    }

    override suspend fun insert(entities: List<User>) {
        dao.insert(entities)
    }

    override suspend fun update(entity: User) {
        if (isGuestUser(entity))
            guestData.setGlobalConfiguration(entity.personalConfiguration)
        else
            dao.update(entity)
    }

    override suspend fun update(entities: List<User>) {
        dao.update(entities)
    }

    override suspend fun delete(entity: User) {
        dao.delete(entity)
    }

    override suspend fun delete(entities: List<User>) {
        dao.delete(entities)
    }

    override fun getAll(): Flow<List<User>> {
        return dao.getAllUsers()
    }

    override suspend fun getByID(id: Long): User {
        return if (isGuestUser(id))
            guestData.getGuestUser().first()
        else
            dao.getByID(id)
    }
}
