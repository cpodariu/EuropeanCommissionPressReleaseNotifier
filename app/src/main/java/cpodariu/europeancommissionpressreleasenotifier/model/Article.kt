package cpodariu.europeancommissionpressreleasenotifier.model

import android.util.Log
import cpodariu.europeancommissionpressreleasenotifier.network_helpers.RequestHelper
import org.apache.commons.lang3.StringUtils

class Article(xmlString: String){

    var url : String = StringUtils.substringBetween(xmlString, "<url>", "</url>")
    val articleDescription : String = StringUtils.substringBetween(xmlString, "<description>", "</description>")
    val id : String = StringUtils.substringBetween(xmlString, "<id>", "</id>")
    val title : String = StringUtils.substringBetween(xmlString, "<title>", "</title>")

    companion object {
        fun getAllArticles(key: String): ArrayList<Article> {
            val xmlResponse = RequestHelper.makeRequest(key)
            val articleList: ArrayList<Article> = ArrayList()
            for (i in xmlResponse.split("</PressRelease><PressRelease>")) {
                articleList.add(Article(i))
            }
            return articleList
        }
    }

}