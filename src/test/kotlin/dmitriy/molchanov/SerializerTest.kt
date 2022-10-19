package dmitriy.molchanov

import dmitriy.molchanov.data.Serializer
import dmitriy.molchanov.model.Rule
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
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
            isUpperCase = true
        )
        val rule2 = Rule(
            gitRepo = "aa",
            regexPrefix = "bb",
            checkString = "cc",
            startWith = "[",
            endWith = "]",
            isUpperCase = false
        )
        val list = arrayListOf(rule, rule2)
        val ser = Serializer.serialize(list)
        val des = Serializer.deserialize<ArrayList<String>>(ser!!) as ArrayList<Rule>

        assertEquals(rule.gitRepo, des[0].gitRepo)
        assertEquals(rule.checkString, des[0].checkString)
        assertEquals(rule.regexPrefix, des[0].regexPrefix)
        assertEquals(rule.startWith, des[0].startWith)
        assertEquals(rule.endWith, des[0].endWith)
        assertEquals(rule.isUpperCase, des[0].isUpperCase)
        assertEquals(rule2.gitRepo, des[1].gitRepo)
        assertEquals(rule2.checkString, des[1].checkString)
        assertEquals(rule2.regexPrefix, des[1].regexPrefix)
        assertEquals(rule2.endWith, des[1].endWith)
        assertEquals(rule2.isUpperCase, des[1].isUpperCase)
        assertEquals(rule.startWith, des[1].startWith)
    }
}
