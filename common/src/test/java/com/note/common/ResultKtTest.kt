package com.note.common

import app.cash.turbine.test
import com.note.common.result.Result
import com.note.common.result.asResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ResultKtTest {

    @Test
    fun Result_success_only() = runTest {
        flow {
            emit(1)
            emit(2)
        }.asResult().test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(1), awaitItem())
            assertEquals(Result.Success(2), awaitItem())

            awaitComplete()
        }
    }

    @Test
    fun Result_catches_errors() = runTest {
        flow {
            emit(1)
            throw Exception("Test Error")
        }.asResult().test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(1), awaitItem())

            when (val errorResult = awaitItem()) {
                is Result.Error -> assertEquals(
                    "Test Error",
                    errorResult.exception.message,
                )

                Result.Loading,
                is Result.Success,
                -> throw IllegalStateException(
                    "The flow should have emitted an Error Result",
                )
            }

            awaitComplete()
        }
    }
}
