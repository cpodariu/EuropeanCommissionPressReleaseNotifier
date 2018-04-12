package cpodariu.europeancommissionpressreleasenotifier.View

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.Toast
import cpodariu.europeancommissionpressreleasenotifier.R
import cpodariu.europeancommissionpressreleasenotifier.model.KeyWord
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.text.Editable
import android.widget.EditText
import android.content.DialogInterface
import android.opengl.Visibility
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import cpodariu.europeancommissionpressreleasenotifier.alarm.Alarm
import cpodariu.europeancommissionpressreleasenotifier.data.db.database
import cpodariu.europeancommissionpressreleasenotifier.network_helpers.RequestHelper
import cpodariu.europeancommissionpressreleasenotifier.utils.NotificationHelper
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.db.*
import java.security.Key
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private val keyWordsArrayList = ArrayList<KeyWord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        keyWordsList.layoutManager = LinearLayoutManager(this)

        toolbar.title = "Press Release Notifications"
//        database.use { update("KeyWord", "lastID" to "MEMO/18/2601")
//                .whereSimple("_id = ?", "1")
//                .exec()  }

        keyWordsList.adapter = KeyWordsAdapter(keyWordsArrayList, this)

        updateKeyWordList()

        fab.onClick {
            alert {
                title = "New keywords:"
                lateinit var et: EditText
                positiveButton("Add") {
                    doAsync {
                        database.use { insert("KeyWord", "word" to et.text.toString(), "lastId" to "") }
                        updateKeyWordList()
                    }
                }
                customView {
                    linearLayout {
                        orientation = LinearLayout.VERTICAL
                        padding = dip(16)
                        linearLayout {
                            padding = dip(16)
                            val tv = textView()
                            tv.text = context.getString(R.string.add_alert_message)
                            tv.textSize = 16f
                        }
                        linearLayout {
                            et = editText()
                            et.height = dip(64)
                            padding = dip(0)
                            et.hint = "Add keyword here"
                            gravity = Gravity.CENTER
                        }
                    }
                }
            }.show()
        }
        Alarm().setAlarm(this);
    }

    private fun updateKeyWordList() {
        val rowParser = classParser<KeyWord>()

        database.use {
            val querryResult = select("KeyWord").parseList(rowParser)
            keyWordsArrayList.clear()
            keyWordsArrayList.addAll(querryResult)
            runOnUiThread {
                keyWordsList.adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

}
