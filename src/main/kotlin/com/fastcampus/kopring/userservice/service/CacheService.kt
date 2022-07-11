package com.fastcampus.kopring.userservice.service

import com.fastcampus.kopring.userservice.domain.entity.User
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class CacheService {

    private val localCache = ConcurrentHashMap<String, User>()

    fun put(key: String, value: User) {
        localCache[key] = value
    }

    fun get(key: String) = localCache[key]

    fun evict(token: String) {
        localCache.remove(token)
    }
}