package palestra.googleassistant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mutable: MutableCollection<Int> = mutableListOf(1,2,3,4,5)
        val immutable: Collection<Int> = mutable

        mutable.add(6)

        Log.e("TAG", immutable.size.toString())
    }
}
