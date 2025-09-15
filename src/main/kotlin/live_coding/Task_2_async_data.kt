@file:Suppress("unused")
package live_coding

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

/**
 * Постановка.
 * Два независимых запроса: fetchUser() и fetchUserOrders(). Выполнить параллельно.
 * Если любая из них завершается ошибкой — вся корутина отменяется.
 * Сделал по сложнее, если ошибка в первой - отмена, во второй - частичный успех
 * (для каноничного случая хватит 1 try)
 * Состояние описывается через sealed interface с тремя состояниями: Loading, Success, Failure.

 */
suspend fun getData(
    id: Int
): AggregateState = withContext(Dispatchers.IO) {
    val sc = CoroutineScope(Job()) // изолируем скоуп для наших 2 корутин
    val userDataDef = sc.async { // а ошибка тут пусть ложит скоуп
        println("async1 on ${Thread.currentThread().name}")
        val users = fetchUser(id = id)
        println("async1 result ready")
        users
    }

    val userOrdersDef = sc.async(Job()) { // чтобы этот второстепенный не ложил скоуп
        println("async2 on ${Thread.currentThread().name}")
        val orders = fetchUserOrdersError(id = id)
        println("async2 result ready")
        orders
    }

    println("await on ${Thread.currentThread().name}")
    val myUser = try {
        userDataDef.await()
    } catch(ex: CancellationException) {
        // залогировать ex
        println("user canceled")
        throw ex
    } catch(ex: RuntimeException) {
        println("user ex $ex")
        return@withContext AggregateState.Failure(ex)
    }

    val orders = try {
        userOrdersDef.await()
    } catch(ex: CancellationException) {
        println("orders canceled")
        throw ex
    } catch(ex: RuntimeException) {
        println("orders ex $ex")
        listOf()
    }
    return@withContext AggregateState.Success(myUser, orders)

}

fun main() {
    runBlocking {
        val _stateFlow = MutableStateFlow<AggregateState>(AggregateState.Loading)

        val viewScope = CoroutineScope(Job())


        println("blocking on ${Thread.currentThread().name}")

        println("before getData")
        // val scope = CoroutineScope(Job() + Dispatchers.IO)
        // scope.getData(1) //////
        // suspend-функция по умолчанию исполняется в том контексте, где её вызвали.
        // async / launch внутри уже используют контекст от scope с IO,
        // а весь остальной код функции остаётся в main.
        // поэтому чтобы перенести на IO надо withContext использовать

        // getData(1) //////
        // если оставить так,
        // то это просто переключит поток, но не будет задействован асинхронный подход
        // для полной асинхронности в данном примере нужен launch

        launch {
            try {
                _stateFlow.value = getData(1)
            } catch (ex: RuntimeException) {
                println("getData wrapper ex $ex")
                throw ex
            }
        }


        println("before collect")
        viewScope.launch { // чтобы collect не ушел в бесконечное ожидание,
            // а скоуп отдельный чтобы можно было закенселить без красноты в логе
            _stateFlow.collect { // равносильно launchIn
                println("state $it")
            }
        }
        println("after collect")
        delay(5000)
        println("after delay")
        viewScope.cancel()


    }
}

data class MyUser(
    val id: String,
    val name: String
)

data class Order(
    val id: String,
    val userId: String,
    val orderNumber: Int
)

sealed interface AggregateState {
    data object Loading : AggregateState
    data class Success(val user: MyUser, val orders: List<Order>) : AggregateState
    data class Failure(val error: Throwable) : AggregateState
}

suspend fun fetchUser(id: Int): MyUser {
    delay(2000)
    return MyUser(
        id = "1",
        name = "Andrew"
    )
}

suspend fun fetchUserOrders(id: Int): List<Order> {
    try {
        delay(4000)
    } catch(ex: CancellationException) {
        println("fetchUserOrders catched cancelation")
        throw ex
    }

    return listOf(
        Order(
            id = "order1",
            userId = "user1",
            orderNumber = 1,
        ),
        Order(
            id = "order2",
            userId = "user2",
            orderNumber = 2,
        )
    )
}

suspend fun fetchUserError(id: Int): MyUser {
    delay(2000)
    throw RuntimeException("User error")
}

suspend fun fetchUserOrdersError(id: Int): List<Order> {
    try {
        delay(4000)
    } catch(ex: CancellationException) {
        println("fetchUserOrdersError catched cancelation")
        throw ex
    }

    throw RuntimeException("Order error")
}