package live_coding

/**
 * Найти ошибки
 */
//class MyActivity : Activity() {
//    private val context = applicationContext // не будет стилизации!!!!!!!!!!
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val rootView = LinearLayout(context).apply {
//            orientation = LinearLayout.VERTICAL
//        }
//        setContentView(rootView)
//
//        val button = Button(context).apply {
//            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
//        }
//        rootView.addView(button)
//
//        val imageView = ImageView(context).apply {
//            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        }
//        rootView.addView(imageView)
//
//        button.setOnClickListener { // унести отсюда!!!!!!!!
//            // релевантность АПИ?!!!!!!!!!!!
//            val downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//            val files = downloads.listFiles()
//            files.forEach { file ->
//                if ((file as File).name.endsWith(".jpg")) {
//                    // поток не запустили вообще (.start()),!!!!!!!!!!!
//                    // утечка при повороте, лучше lifecycleScope.launch c withContext!!!!!!!!!!!
//                    Thread {
//                        val image = BitmapFactory.decodeFile(file.path) // считывание целиком -> OutOfMemory возможен, Glide юзай!!!!!!!!
//                        imageView.setImageBitmap(image) // надо делать на главном потоке!!!!!!!!!
//                    }
//                    return@forEach // == continue!!!!!!!!
//                }
//
//                throw RuntimeException("No images in downloads folder!!") // упадет на первой не картинке!!!!!!!!
//            }
//        }
//    }
//}