package live_coding


// Если пользователь кликнул один раз,
// вызывается onSingleClick после истечения времени delayMS.

// Если за время delayMS происходит второй клик,
// то вызывается onDoubleClick, а onSingleClick не срабатывает.


fun View.setDoubleClickListener(
    delayMS: Long,
    onSingleClick: () -> Unit,
    onDoubleClick: () -> Unit
) {
    var lastClickMillis: Long? = null

    val singleRunnable = Runnable {
        onSingleClick()
    }

    setOnClickListener {
        val currentClickMillis = System.currentTimeMillis()
        lastClickMillis?.let {
            if (currentClickMillis < it + delayMS) {
                removeCallbacks(singleRunnable)
                onDoubleClick()
                lastClickMillis = null
                return@setOnClickListener
            }
        }
        lastClickMillis = currentClickMillis
        postDelayed(singleRunnable, delayMS)
    }
}

class View {
    fun postDelayed(action: Runnable, delay: Long) {}
    fun removeCallbacks(runnable: Runnable) {}
    fun setOnClickListener(onClick: ()-> Unit) {}
}