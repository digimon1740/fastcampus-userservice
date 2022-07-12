package com.fastcampus.kopring.userservice.service

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class CacheService<T> {

    private val localCache = ConcurrentHashMap<String, T>()

    suspend fun awaitPut(key: String, value: T) {
        localCache[key] = value
    }

    suspend fun awaitGetOrPut(key: String, supplier: suspend () -> T): T {
        val cached = if (localCache[key] == null) {
            localCache[key] = supplier()
            localCache[key]
        } else {
            localCache[key]
        }
        checkNotNull(cached)
        return cached
    }

    suspend fun awaitEvict(token: String) {
        localCache.remove(token)
    }
}