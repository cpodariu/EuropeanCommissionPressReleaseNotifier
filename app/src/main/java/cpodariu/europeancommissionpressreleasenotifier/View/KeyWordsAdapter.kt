package cpodariu.europeancommissionpressreleasenotifier.View

import android.content.Context
import android.provider.SyncStateContract.Helpers.update
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.swipe.SwipeLayout
import cpodariu.europeancommissionpressreleasenotifier.R
import cpodariu.europeancommissionpressreleasenotifier.data.db.database
import cpodariu.europeancommissionpressreleasenotifier.model.KeyWord
import kotlinx.android.synthetic.main.key_words_item_view.view.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.update
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.security.Key

/**
 * Created by cpodariu on 17-Mar-18.
 * For any questions please contact me at podariucatalin97@gmail.com
 */

class KeyWordsAdapter(val keyWordsList: ArrayList<KeyWord>, val ctx: Context) : RecyclerView.Adapter<KeyWordsAdapter.KeyWordsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyWordsAdapter.KeyWordsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.key_words_item_view, parent, false)
        return KeyWordsAdapter.KeyWordsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return keyWordsList.size;
    }

    override fun onBindViewHolder(holder: KeyWordsViewHolder, position: Int) {
        holder.bindKeyWord(keyWordsList[position], ctx, fun(k : KeyWord){keyWordsList.remove(k); notifyDataSetChanged()} )
    }


    class KeyWordsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindKeyWord(keyWord: KeyWord, ctx: Context, deleteElem: (KeyWord) -> Unit) {
            itemView.key_words_text_view.text = keyWord.word
            itemView.swipe_layout.addDrag(SwipeLayout.DragEdge.Right, itemView.bottom_wrapper);
            itemView.delete_button.onClick {
                ctx.database.use { execSQL("DELETE FROM KeyWord WHERE _id is " + keyWord.id) }
                deleteElem(keyWord)
            }
        }
    }
}