package main.kotlin.dmitriy.molchanov.ui.main

import java.awt.event.MouseEvent
import java.awt.event.MouseListener

interface MouseClickListener: MouseListener {

    override fun mouseReleased(p0: MouseEvent?) = Unit

    override fun mouseEntered(p0: MouseEvent?)  = Unit

    override fun mouseClicked(p0: MouseEvent?) = Unit

    override fun mouseExited(p0: MouseEvent?)  = Unit

    override fun mousePressed(p0: MouseEvent?)
}