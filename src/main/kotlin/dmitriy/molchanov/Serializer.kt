package main.kotlin.dmitriy.molchanov

import org.apache.commons.codec.binary.Base64
import java.io.*

object Serializer {
    fun <T>serialize(dataList: ArrayList<T>): String? = try {
        ByteArrayOutputStream().use { bo ->
            ObjectOutputStream(bo).use { so ->
                so.writeObject(dataList)
                so.flush()
                Base64.encodeBase64String(bo.toByteArray())
            }
        }
    } catch (e: IOException) {
        null
    }

    fun <T> deserialize(dataStr: String): ArrayList<T>? {
        val b = Base64.decodeBase64(dataStr)
        val bi = ByteArrayInputStream(b)
        return try {
            val si = ObjectInputStream(bi)
            ArrayList::class.java.cast(si.readObject()) as ArrayList<T>
        } catch (e: IOException) {
            null
        } catch (e: ClassNotFoundException) {
            null
        }
    }
}