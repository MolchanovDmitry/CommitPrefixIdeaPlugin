package dmitriy.molchanov

import dmitriy.molchanov.data.Serializer
import dmitriy.molchanov.model.Rule
import org.junit.Assert.*
import org.junit.Test

class SerializerTest {

    @Test
    fun serialize() {
        val list = ArrayList<String>()
        list.add("1111")
        list.add("2222")
        val ser = Serializer.serialize(list)
        val des = Serializer.deserialize<ArrayList<String>>(ser!!) as ArrayList<String>

        assertArrayEquals(list.toTypedArray(), des.toTypedArray())
    }

    @Test
    fun serializeRule() {
        val rule = Rule(
            gitRepo = "11",
            regexPrefix = "22",
            checkString = "33",
            startWith = "[",
            endWith = "]",
            register = ""
        )
        val rule2 = Rule(
            gitRepo = "aa",
            regexPrefix = "bb",
            checkString = "cc",
            startWith = "[",
            endWith = "]",
            register = ""
        )
        val list = arrayListOf(rule, rule2)
        val ser = Serializer.serialize(list)
        val des = Serializer.deserialize<ArrayList<String>>(ser!!) as ArrayList<Rule>

        assertEquals(rule.gitRepo, des[0].gitRepo)
        assertEquals(rule.checkString, des[0].checkString)
        assertEquals(rule.regexPrefix, des[0].regexPrefix)
        assertEquals(rule2.gitRepo, des[1].gitRepo)
        assertEquals(rule2.checkString, des[1].checkString)
        assertEquals(rule2.regexPrefix, des[1].regexPrefix)

    }
}