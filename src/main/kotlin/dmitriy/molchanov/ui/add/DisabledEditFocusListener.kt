package dmitriy.molchanov.ui.add

import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.lang.ref.WeakReference
import javax.swing.JTextArea

/**
 * Слушатель фокуса, не позволяющий редактировать [JTextArea]
 */
class DisabledEditFocusListener(textArea: JTextArea): FocusListener {

    private val weakTextArea = WeakReference(textArea)

    override fun focusGained(e: FocusEvent) {
        weakTextArea.get()?.isEditable = false
    }

    override fun focusLost(e: FocusEvent?) {
        weakTextArea.get()?.isEditable = true
    }
}