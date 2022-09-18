package id.xxx.module.data.model

import android.content.Context
import org.json.JSONArray
import java.io.BufferedReader

data class CountryCode(
    val name: String,
    val dial_code: String,
    val code: String,
    val flag: String
) {
    companion object {

        fun from(context: Context?): List<CountryCode> {
            val result = mutableListOf<CountryCode>()
            if (context != null) {
                val `in` = context.assets.open("country_codes.json")
                val jsonArrayString = `in`.bufferedReader().use(BufferedReader::readText)
                val jsonArray = JSONArray(jsonArrayString)
                val jsonArrayLen = (jsonArray.length() - 1)
                for (index in 0..jsonArrayLen) {
                    val jsonObject = jsonArray.getJSONObject(index)
                    val data = CountryCode(
                        name = jsonObject.getString("name"),
                        dial_code = jsonObject.getString("dial_code"),
                        code = jsonObject.getString("code"),
                        flag = jsonObject.getString("flag")
                    )
                    result.add(data)
                }
            }
            return result.toList()
        }
    }
}