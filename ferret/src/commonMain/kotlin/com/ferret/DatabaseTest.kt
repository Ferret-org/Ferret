package com.ferret

import com.ferret.model.Body
import com.ferret.model.Header
import com.ferret.model.HttpMethod
import com.ferret.model.Transaction
import com.ferret.model.TransactionProtocol
import com.ferret.model.TransactionState
import com.ferret.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import kotlin.time.Clock

object DatabaseTester {

    suspend fun run(
        repository: TransactionRepository
    ) {

//        insert
        val id = repository.insert(
            Transaction(
                sessionId = "session-1",
                protocol = TransactionProtocol.HTTP,
                state = TransactionState.STARTED,
                url = "https://jsonplaceholder.typicode.com/posts",
                method = HttpMethod.GET,
                requestHeaders = listOf(
                    Header("Authorization", "Bearer token"),
                    Header("Accept", "application/json")
                ),
                responseHeaders = emptyList(),
                requestBody = null,
                responseBody = null,
                statusCode = null,
                startTimestamp = Clock.System.now().epochSeconds,
                endTimestamp = null,
                durationMs = null,
                isSecure = true,
                errorMessage = null
            )
        )

        println("Inserted -> $id")


//        Get
        val transaction = repository.get(id)

        println(transaction)

//        update

        repository.update(
            transaction!!.copy(
                state = TransactionState.COMPLETED,
                statusCode = 200,
                endTimestamp = Clock.System.now().epochSeconds,
                durationMs = 125,
                responseHeaders = listOf(
                    Header("Content-Type", "application/json")
                ),
                responseBody = Body(
                    contentType = "application/json",
                    content = """{"success":true}""",
                    sizeInBytes = 16
                )
            )
        )

        println(repository.get(id))

//      getAll

        repository.getAll().forEach {
            println(it)
        }

//      observing all
        println(repository.observeAll().first())


//        delete older

//        repository.deleteOlderThan(
//            Clock.System.now().epochSeconds - 1000
//        )

        println(repository.getAll())


//        clearing

//        repository.clear()

        println(repository.getAll())

    }
}