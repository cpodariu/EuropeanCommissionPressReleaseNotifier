package cpodariu.europeancommissionpressreleasenotifier.network_helpers

import java.net.URL

/**
 * Created by cpodariu on 19-Mar-18.
 * For any questions please contact me at podariucatalin97@gmail.com
 */

object RequestHelper{
    final val URL_START = "http://europa.eu/rapid/search-result.htm?dateRange=1d&quickSearch=1&text="
    final val URL_END = "&format=XML&page=1"

    fun getUrlForString(s : String) : String{
        return URL_START + s.replace(" ", "+") + URL_END;
    }

    fun makeRequest(s : String) : String{
        return URL(getUrlForString(s)).readText()
    }
}