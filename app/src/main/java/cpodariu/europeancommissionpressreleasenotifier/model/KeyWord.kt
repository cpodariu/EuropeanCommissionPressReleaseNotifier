package cpodariu.europeancommissionpressreleasenotifier.model

/**
 * Created by cpodariu on 17-Mar-18.
 * For any questions please contact me at podariucatalin97@gmail.com
 */

data class KeyWord(val id : Long, val word: String, val lastId : String) {
    companion object {
        fun parse(xml: String): String {
            val articles = xml.split("</PressRelease><PressRelease>")
            return articles.size.toString();
        }
    }
}