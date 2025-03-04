package com.rago.handlenetwork.presentation.screen

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.util.CoilUtils
import com.rago.handlenetwork.presentation.composable.MarkdownRenderer
import io.noties.markwon.Markwon
import io.noties.markwon.image.coil.CoilImagesPlugin
import okhttp3.OkHttpClient

@Composable
fun MarkdownScreen() {
    MarkdownScreenContent()
}

@Composable
private fun MarkdownScreenContent() {

//    MarkdownRenderer(markdownText = text)
    MarkdownText(text)
}

@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizeBytes(50L * 1024 * 1024) // 50 MB
                .build()
        }
        .build()

    val markwon = Markwon.builder(context)
        .usePlugin(CoilImagesPlugin.create(context, imageLoader))
        .build()

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            TextView(ctx).apply {
                markwon.setMarkdown(this, markdown)
            }
        }
    )
}


val text = """# Título de Nivel 1

## Título de Nivel 2

### Título de Nivel 3

#### Título de Nivel 4

##### Título de Nivel 5

###### Título de Nivel 6

---

## Estilos de Texto

**Texto en negrita**

*Texto en cursiva*

***Texto en negrita y cursiva***

~~Texto tachado~~

> Esto es una cita

---

## Listas

### Lista Desordenada
- Elemento 1
- Elemento 2
  - Subelemento 1
  - Subelemento 2

### Lista Ordenada
1. Primer elemento
2. Segundo elemento
   1. Subelemento 1
   2. Subelemento 2

---

## Enlaces e Imágenes

[Enlace a Google](https://www.google.com)

![Imagen de ejemplo](https://cdn-icons-png.flaticon.com/512/7857/7857073.png)

---

""".trimIndent()