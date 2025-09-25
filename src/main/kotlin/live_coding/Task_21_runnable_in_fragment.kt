package live_coding

/**
 * Найти ошибки
 */
//class SearchFragment : Fragment() {
//
//    private val handlerThread = HandlerThread("SearchHandlerThread")
//        .apply { start() }
//    private val handler = Handler(handlerThread.looper)
//
//    private val searchText: EditText = requireView().findViewById(R.id.search_text) // viewBinding, либо nullable!!!!!!!!!
//    private val searchResultView: TextView = requireView().findViewById(R.id.search_result)
//    private val searchButton: Button = requireView().findViewById(R.id.search_button)
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View = inflater.inflate(R.layout.fragment_search, container, true) // false а не true!!!!!!!!
//      У фрагмента сам FragmentManager сам добавляет view в контейнер.!!!!!!!!
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val runnable = Runnable {
//            val searchString = searchText.text.toString() // runOnUiThread!!!!!!!!!
//            val searchResult = executeSearchRequest(searchString)
//            searchResultView.text = searchResult // runOnUiThread!!!!!!!!!
//        }
//        searchButton.setOnClickListener {
//            handler.post(runnable)
//        }
//    }

//    не хватает отмены, при повороте, может и при паузе,!!!!!!!!!!!
//    зануления вьюшек, отключения потока!!!!!!!!!!
//
//    private fun executeSearchRequest(searchString: String): String {
//        // Долгая операция, возможно поход в сеть
//        return "Result for: $searchString"
//    }
//}