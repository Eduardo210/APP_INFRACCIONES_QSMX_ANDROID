package mx.qsistemas.infracciones.utils

import org.json.JSONException
import org.json.JSONObject

class Ticket {
    companion object {

        fun getPrintObject(text: String, size: String, position: String, bold: String): JSONObject {
            val json = JSONObject()
            try {
                json.put("content-type", "txt")
                json.put("content", text)
                json.put("size", size)
                json.put("position", position)
                json.put("offset", "0")
                json.put("bold", bold)
                json.put("italic", "0")
                json.put("height", "-1")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return json
        }

        fun getPrintBarCode(text: String): JSONObject {
            val json = JSONObject()
            try {
                json.put("content-type", "one-dimension")
                json.put("content", text)
                json.put("size", "3")
                json.put("position", "center")
                json.put("offset", "0")
                json.put("bold", "0")
                json.put("italic", "0")
                json.put("height", "2")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return json
        }
    }
}