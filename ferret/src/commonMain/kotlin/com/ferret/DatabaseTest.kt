package com.ferret

import com.ferret.model.Transaction
import com.ferret.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import kotlin.time.Clock

object DatabaseTester {

    suspend fun run(repository: TransactionRepository) {
        val now = Clock.System.now().epochSeconds * 1000

        val id = repository.insert(
            Transaction(
                sessionId = "session-test-1",
                requestDate = now,
                protocol = "HTTPS",
                method = "GET",
                url = "https://jsonplaceholder.typicode.com/posts",
                host = "jsonplaceholder.typicode.com",
                path = "/posts",
                scheme = "https",
            )
        )
        println("Inserted → $id")
        println(repository.get(id))
        println(repository.getAll())
        println(repository.observeAll().first())
    }
}
