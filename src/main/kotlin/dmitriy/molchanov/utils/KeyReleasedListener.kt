package main.kotlin.dmitriy.molchanov.utils

import java.awt.event.KeyEvent
import java.awt.event.KeyListener

interface KeyReleasedListener: KeyListener {
    override fun keyTyped(var1: KeyEvent?) = Unit

    override fun keyPressed(var1: KeyEvent?) = Unit

    override fun keyReleased(var1: KeyEvent?)
}