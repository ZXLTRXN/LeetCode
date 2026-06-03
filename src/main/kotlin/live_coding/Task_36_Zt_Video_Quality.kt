package live_coding

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.collections.ArrayDeque
import kotlin.collections.reduce

// Есть видеозвонок. Раз в секунду приходит метрика сети.
// Нужно определить уровень качества видео: LOW, MEDIUM, HIGH.

/**

 * Один сэмпл состояния сети за текущий момент времени.
 *
 * Предполагается, что такие сэмплы приходят периодически (например, раз в секунду)
 * и используются для принятия решения о качестве видео.
 */
public data class NetworkSample(
    /**
     * Оценка пропускной способности канала (Kbps).
     *
     * Пример:
     * - 600-1000 Kbps: обычно хватает только для низкого качества
     * - 1200-2500 Kbps: диапазон для среднего качества
     * - 2500+ Kbps: потенциально достаточно для высокого качества
     */
    val bandwidthKbps: Int,

    )

/**

 * Дискретные уровни качества видео, которые может выбрать адаптер.
 *
 * Обычно уровни мапятся на профили кодека/разрешения, например:
 * - LOW -> 360p / сниженный bitrate
 * - MEDIUM -> 720p / сбалансированный режим
 * - HIGH -> 1080p / высокий bitrate
 */
public enum class VideoQuality {
    /**
     * Низкое качество.
     *
     * Используется при плохом канале:
     * высокий packet loss, высокий RTT или недостаточный bandwidth.
     */
    LOW,

    /**
     * Среднее качество.
     *
     * Базовый "рабочий" режим для типичных сетевых условий.
     */
    MEDIUM,

    /**
     * Высокое качество.
     *
     * Включается только при стабильно хороших сетевых метриках,
     * чтобы избежать "флаппинга" (частых переключений вверх-вниз).
     */
    HIGH
}

class Task_36_Zt_Video_Quality {

    /**

     * На основе потока сетевых сэмплов возвращает поток решений по качеству видео.
     *
     * Что ожидается от реализации:
     * 1) Анализировать входные метрики в реальном времени (Flow).
     * 2) Использовать сглаживание (например, окно последних N сэмплов),
     * чтобы не реагировать на единичные шумовые всплески.
     * 3) Эмитить решение на каждый входной сэмпл.
     * 4) Применять гистерезис:
     * - понижать качество быстро при ухудшении;
     * - повышать осторожно, только после стабилизации. 2
     *
     * Ограничения:
     * - Нельзя менять сигнатуру метода
     * - Никаких блокирующих вызовов (Thread.sleep и т.п.).
     * - Решение должно корректно работать в корутинном контексте и уважать отмену.
     */
    public fun decideQuality(samples: Flow<NetworkSample>): Flow<VideoQuality> {
        return flow {
            val samplesBandwidthQueue = ArrayDeque<Int>(3)
            var currentVideoQuality: VideoQuality? = null
            var dropCounter = 0
            val dropThreshold = 2

            samples.collect { sample ->
                while (samplesBandwidthQueue.size > 2) {
                    samplesBandwidthQueue.removeFirst()
                }
                samplesBandwidthQueue.addLast(sample.bandwidthKbps)

                val average = samplesBandwidthQueue.getAverage()

                val newVideoQuality = average.mapBandwidthToVideoQuality()
                currentVideoQuality?.let { cvq ->
                    when {
                        newVideoQuality.ordinal > cvq.ordinal && dropCounter < dropThreshold -> {
                            dropCounter++
                            emit(cvq)
                        }

                        else -> {
                            dropCounter = 0
                            currentVideoQuality = newVideoQuality
                            emit(newVideoQuality)
                        }
                    }

                    return@collect
                }

                // if currentVideoQuality == null
                currentVideoQuality = newVideoQuality
                emit(newVideoQuality)
            }
        }
    }

    fun ArrayDeque<Int>.getAverage(): Int {
        if (this.isEmpty()) return 0

        val sumOfBandwidth = this.reduce { acc, v ->
            acc + v
        }
        return sumOfBandwidth / this.size
    }

    fun Int.mapBandwidthToVideoQuality(): VideoQuality {
        return when { // недописано ну и ок
            this > 2500 -> VideoQuality.HIGH
            else -> VideoQuality.LOW
        }
    }
}