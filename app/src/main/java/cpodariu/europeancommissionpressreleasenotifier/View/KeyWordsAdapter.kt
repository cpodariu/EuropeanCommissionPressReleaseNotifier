package cpodariu.europeancommissionpressreleasenotifier.View

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cpodariu.europeancommissionpressreleasenotifier.R
import cpodariu.europeancommissionpressreleasenotifier.model.KeyWord
import kotlinx.android.synthetic.main.key_words_item_view.view.*

/**
 * Created by cpodariu on 17-Mar-18.
 * For any questions please contact me at podariucatalin97@gmail.com
 */

class KeyWordsAdapter(val keyWordsList: ArrayList<KeyWord>) : RecyclerView.Adapter<KeyWordsAdapter.KeyWordsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeyWordsAdapter.KeyWordsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.key_words_item_view, parent, false)
        return KeyWordsAdapter.KeyWordsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return keyWordsList.size;
    }

    override fun onBindViewHolder(holder: KeyWordsViewHolder, position: Int) {
        holder.bindKeyWord(keyWordsList[position])
    }


    class KeyWordsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindKeyWord(keyWord : KeyWord){
            with(keyWord){
                itemView.key_words_text_view.text = keyWord.word
            }
        }
    }
}