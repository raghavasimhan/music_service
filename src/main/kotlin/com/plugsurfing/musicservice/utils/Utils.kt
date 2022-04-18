package com.plugsurfing.musicservice.utils

import org.json.JSONObject

class Utils {
    companion object {
        fun parseWikiDataResponse(response : String, id : String) : String {
            var jsonObject = JSONObject(response)
            val keys = listOf("entities", id, "sitelinks", "enwiki")
            keys.forEach { jsonObject = jsonObject.getJSONObject(it) }
            return jsonObject.getString("url")
        }
    }
}