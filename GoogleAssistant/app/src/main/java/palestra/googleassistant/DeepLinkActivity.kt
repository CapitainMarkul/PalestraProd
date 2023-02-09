package palestra.googleassistant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_deeplink.*


class DeepLinkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deeplink)

        processDeepLink()
    }

    private fun processDeepLink() {
        val url = intent.dataString
        full_url.text = "\n Full url : $url.\n\n"
        txt_json.text = "Json : ${converterUrl(url)}"
    }

    private fun converterUrl(url: String): String {
        return url.substringAfterLast('/')

//        val listIndex = mutableListOf<Int>()
//        val stringBuilder = StringBuilder(newUrl)
//        stringBuilder.forEachIndexed { index, c ->
//            if (c == '[' || c == ',' || c == '{') {
//                listIndex.add(index + 1)
//            }
//        }
//
//        listIndex.asReversed().forEach {
//            stringBuilder.insert(it, "\n")
//        }
//        return "Json:" + stringBuilder.toString()
    }
}