package main.kotlin.dmitriy.molchanov.ui.add

import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.JPanel

object BoxLayoutUtils {

    /** Выравнивание компонентов по оси X для группы компонентов */
    fun setGroupAlignmentX(components: Array<JComponent>, alignment: Float) =
            components.forEach { it.alignmentX = alignment }

    /** Создание панели с вертикальным расположением */
    fun createVerticalPanel() = JPanel().apply {
        maximumSize = Dimension(600, 300)
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
    }

    /** Создание панели с горизонтальным расположением */
    fun createHorizontalPanel() = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.X_AXIS)
    }
}