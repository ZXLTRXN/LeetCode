package live_coding

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.CopyOnWriteArrayList

// У нас есть кошелек, мы можем его пополнять, выводить с него деньги, либо переводить на другой
// кошелек. Баланс может уходить в минус.
// 1. Что будет выведено на консоль
// 2. Отрефачить код

class WBWallet(initialBalance: Int, val owner: String) {
    var balance: Int = initialBalance
    val history: MutableList<Transaction> = mutableListOf()

    fun deposit(amount: Int) {
        balance += amount
        history += Transaction("+$amount")
    }

    fun withdraw(amount: Int) {
        balance -= amount
        history -= Transaction("-$amount")
    }

    fun transferTo(otherWallet: WBWallet, amount: Int) {
        balance -= amount
        history += Transaction("-$amount")
        otherWallet.balance += amount
        otherWallet.history += Transaction("+$amount")
    }
}

class WBWalletFixed(initialBalance: Int, val owner: String) {
    @Volatile
    var balance: Int = initialBalance
        private set

    val history: MutableList<Transaction> = CopyOnWriteArrayList()

    val mutex = Mutex()

    suspend fun deposit(amount: Int) {
        mutex.withLock {
            unsafeDeposit(amount)
        }
    }

    /**
     * for internal use only
     */
    fun unsafeDeposit(amount: Int) {
        balance += amount
        history += Transaction("+$amount")
    }

    suspend fun withdraw(amount: Int) {
        mutex.withLock {
            unsafeWithdraw(amount)
        }
    }

    /**
     * for internal use only
     */
    fun unsafeWithdraw(amount: Int) {
        balance -= amount
        history += Transaction("-$amount")
    }

    // 2 одновременных трансфера друг другу вызовут дедлок, поэтому делаем так
    suspend fun transferTo(otherWallet: WBWalletFixed, amount: Int) {
        val (firstWallet, secondWallet) =
            if (this.owner > otherWallet.owner)
                this@WBWalletFixed to otherWallet
            else otherWallet to this@WBWalletFixed

        withContext(NonCancellable) {
            firstWallet.mutex.withLock {
                secondWallet.mutex.withLock {
                    unsafeWithdraw(amount)
                    otherWallet.unsafeDeposit(amount)
                }
            }
        }
    }
}

suspend fun main() {
    val scope = CoroutineScope(Dispatchers.Default)
    val wallet1 = WBWalletFixed(10000, "User")
    val wallet2 = WBWalletFixed(10000, "User2")
    val jobs = mutableListOf<Job>()
//    jobs += scope.launch { // неровное число выдаст - это не ошибка
//        for (i in 0..1000) {
//            wallet1.deposit(100)
//        }
//    }
//    jobs += scope.launch {
//        for (i in 0..1000) {
//            wallet2.withdraw(100)
//        }
//    }
    jobs += scope.launch {
        for (i in 0..1000) {
            wallet1.transferTo(wallet2, 100)
        }
    }
    jobs += scope.launch {
        for (i in 0..1000) {
            wallet2.transferTo(wallet1, 100)
        }
    }
    jobs.joinAll()
    scope.cancel()
    println("wallet1 balance: ${wallet1.balance}")
    println("wallet2 balance: ${wallet2.balance}")
}

class Transaction(val string: String)
