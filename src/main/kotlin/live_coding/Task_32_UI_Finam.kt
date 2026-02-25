package live_coding

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/*
Задача.
Реализовать экран с двумя состояниям: загрузки и сами данные. Запрос Security - каждые 5 секунд.
в ui это 2 плашки, 1ая c security
хедеры по краям bid                 ask
                bid x bidVolume  ask x askVolume

2ая упростим оставим только для daily это розовая полоска где красным выделен диапазон,
а current помечен маленьким ползунком снизу
 ----------!========---------
 MIN        OPEN    CLOSE   MAX
           CURRENT


Технический стек.
Kotlin Multiplatform, Kotlin Coroutines, Jetpack Compose.
*/

// —-------------------- SecurityGateway.kt ------------------

interface SecurityGateway {
    suspend fun getSecurity(id: Long): Security
}

data class Security(
    val id: Long,
    val quoteId: String,
    val bid: Double,
    val bidVolume: String,
    val ask: Double,
    val askVolume: String
)

// —--------------------------------------------------------

// —-------------------- QuoteGateway.kt ------------------

// считаем, что реализация уже есть
interface QuoteGateway {
    fun getQuoteFlow(quoteId: String): Flow<Quote>
}


data class Quote(
    val id: String,
    val daily: Map<RangeParam, Double>,
    val w52: Map<RangeParam, Double>,
)

data class SecurityQuote(
    val securityId: Long,
    val bid: Double,
    val bidVolume: String,
    val ask: Double,
    val askVolume: String,

    val quoteId: String,
    val daily: Map<RangeParam, Double>,
    val w52: Map<RangeParam, Double>,
)

enum class RangeParam {
    MIN,
    MAX,
    OPEN,
    CLOSE,
    CURRENT
}
//—---------------------------------------------------------

// —-------------------- TradeInfoViewModel.kt -------------

class TradeInfoViewModel(
    val securityId: Long,
    val sG: SecurityGateway,
    val qG: QuoteGateway,
) {
    val scope = CoroutineScope(Dispatchers.Default)
//    было дано
//    var state: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
//        private set

    val securityFlow: Flow<Security> = flow {
        while (currentCoroutineContext().isActive) {
            val security = sG.getSecurity(securityId)
            emit(security)
            delay(5_000)
        }
    }

    fun Flow<Security>.pairWithQuote(): Flow<ScreenState> {
        return this.flatMapLatest { security ->
            qG.getQuoteFlow(security.quoteId)
                .map { ScreenState.Loaded(security, it) }
        }
    }

    var state: StateFlow<ScreenState> = securityFlow.pairWithQuote()
        .debounce(20000)
        .stateIn(
            scope,
            SharingStarted.WhileSubscribed(5000),
            ScreenState.Loading
        )


    fun onInit() {
//        Я написал на собесе, криво, используй что выше написано
//        scope.launch() {
//            while(isActive) {
//                val sequrity = sG.getSecurity(securityId)
//                val flowCollector = qG.getQuoteFlow(sequrity.quoteId).onEach {
//                    println("quote: ${sequrity.quoteId}")
//                    updateState(sequrity, quote)
//                }
//                    .launchIn(scope)
//                delay(5_000)
//                flowCollector.cancel()
//            }
//        }
    }
//

    fun onDestroy() {
        scope.cancel()
    }

}

// написал я
sealed interface ScreenState {
    data object Loading : ScreenState
    data class Loaded(
        val security: Security,
        val quote: Quote
    ) : ScreenState
}

//—---------------------------------------------------------

// —-------------------- TradeInfoUi.kt -------------

// Дано
//@Composable
//fun TradeInfoUi(securityId: Long) {
//    WithViewModel(TradeInfoViewModel(securityId)) { state, viewModel ->
//    }
//}

//@Comp
//fun SecurityCard(
//    security: Security,
//    modifier = Modifier
//) {
//    Surface(modifier = modifier) {
//        Column() {
//            Row(horizontalArrangement = SpaceBetween) {
//                Text("Bid")
//                Text("Ask")
//            }
//            // bid = 10050010050015.00000 ask = 10500023902392039.090909
//            // volume = 239023902930293.02903
//            Row(horizontalArrangement = SpaceBetween) {
//                Row() {
//                    Text("$security.bid
//                            Text("x)
//                            Text("$security.bidVolume")
//                }
//                Row() {
//                    Text("$security.bid
//                            Text("x)
//                            Text("$security.bidVolume")
//                }
//            }
//
//        }
//    }
//}
//
//
//
//
//@Comp
//fun QuoteCard(
//    quote: Quote,
//    modifier = Modifier
//) {
//    Surface(modifier = modifier) {
//        Column() {
//            Row() {
//                Text("quote.bid
//                        Text("x)
//                        Text("$security.bidVolume")
//            }
//        }
//    }
//}
//
//@Comp
//fun QuoteIndicator(
//    map: Map<RangeParam, Double>,
//) {
//
//}


//—---------------------------------------------------------



