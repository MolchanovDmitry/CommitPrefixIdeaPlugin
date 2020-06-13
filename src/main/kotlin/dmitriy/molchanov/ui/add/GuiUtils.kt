package main.kotlin.dmitriy.molchanov.ui.add

import javax.swing.JComponent
import javax.swing.JTextField


object GuiUtils {

    /**
     * Определение компонентам размера самого большого (по ширине) компонента в группе
     * Метод придания группе компонентов одинаковых размеров (минимальных,
     * предпочтительных и максимальных).
     * @param components список компонентов
     */
    fun makeSameSize(components: Array<JComponent>) {
        // Массив компонентов
        val array = components
                .map { it.preferredSize.width }
                .toIntArray()
        // Получение максимального размера
        val maxSizePos = maximumElementPosition(array)
        val maxSize = components[maxSizePos].preferredSize
        // Установка компонентам одинаковых размеров
        components.forEach {
            it.preferredSize = maxSize
            it.minimumSize = maxSize
            it.maximumSize = maxSize
        }
    }

    /** Корректировка размера текстового поля JTextField */
    fun fixTextFieldSize(field: JTextField) {
        val size = field.preferredSize
        //	чтобы текстовое поле по-прежнему могло увеличивать свой размер в длину
        size.width = field.maximumSize.width
        //	Определение максимального размера текстового поля
        field.maximumSize = size
    }

    /** Метод определения позиции максимального элемента массива */
    private fun maximumElementPosition(array: IntArray): Int {
        var maxPos = 0
        for (i in 1 until array.size) {
            if (array[i] > array[maxPos]) maxPos = i
        }
        return maxPos
    }
}