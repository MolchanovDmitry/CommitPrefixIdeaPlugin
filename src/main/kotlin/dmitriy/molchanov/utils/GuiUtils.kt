package main.kotlin.dmitriy.molchanov.utils

import java.awt.event.KeyListener
import javax.swing.*


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

    /** Метод определения позиции максимального элемента массива */
    private fun maximumElementPosition(array: IntArray): Int {
        var maxPos = 0
        for (i in 1 until array.size) {
            if (array[i] > array[maxPos]) maxPos = i
        }
        return maxPos
    }

    /** Заголовок и ввод git репозитория */
    fun getViewGroup(label: JLabel, textField: JTextField, keyListener: KeyListener): JPanel {
        textField.addKeyListener(keyListener)
        val group = BoxLayoutUtils.createHorizontalPanel()
        group.add(label)
        group.add(Box.createHorizontalStrut(10))
        group.add(textField)
        return group
    }
}