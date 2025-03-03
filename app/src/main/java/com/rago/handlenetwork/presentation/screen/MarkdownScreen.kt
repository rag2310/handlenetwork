package com.rago.handlenetwork.presentation.screen

import androidx.compose.runtime.Composable
import com.rago.handlenetwork.presentation.composable.MarkdownRenderer

@Composable
fun MarkdownScreen() {
    MarkdownScreenContent()
}

@Composable
private fun MarkdownScreenContent() {
    val text = """
        # Ejemplo de Gráfica en Markdown

        Aquí tienes una gráfica de ejemplo que muestra las ventas de los últimos meses:

        ![Gráfica de Ventas](https://cdn-icons-png.flaticon.com/512/7857/7857073.png)

        ## Detalles de la Gráfica

        - **Enero**: 50 unidades vendidas
        - **Febrero**: 75 unidades vendidas
        - **Marzo**: 100 unidades vendidas
         """.trimIndent()

    MarkdownRenderer(markdownText = text)
}
