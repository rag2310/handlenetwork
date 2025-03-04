package com.rago.handlenetwork.presentation.composable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.compose.rememberConstraintsSizeResolver
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.size.Size
import com.rago.handlenetwork.data.utils.markdown.MarkdownElement
import com.rago.handlenetwork.data.utils.markdown.MarkdownElement.*
import com.rago.handlenetwork.data.utils.markdown.MarkdownParser

@Composable
fun MarkdownRenderer(markdownText: String) {
    val elements = MarkdownParser.parse(markdownText)
LazyColumn(modifier = Modifier.padding(16.dp)) {
        elements.forEach { element ->
            when (element) {
                is Header -> item { Header(element.level, element.text) }
                is Bold -> item { BoldText(element.text) }
                is Italic -> item { ItalicText(element.text) }
                is BoldItalic -> item { BoldItalicText(element.text) }
                is Strikethrough -> item { StrikethroughText(element.text) }
                is BlockQuote -> item { BlockQuoteText(element.text) }
                is Link -> item { LinkText(element.text, element.url) }
                is Image -> item { ImageElement(element.altText, element.url) }
                is UnorderedList -> item { UnorderedListElement(element.items) }
                is OrderedList -> item { OrderedListElement(element.items) }
                is ListItem -> item { ListItemElement(element.elements) }
                is Paragraph -> item { ParagraphText(element.elements) }
                is Text -> item { InlineText(element.text) }
            }
        }
    }
}

@Composable
fun Header(level: Int, text: String) {
    val fontSize = when (level) {
        1 -> 30.sp
        2 -> 24.sp
        3 -> 18.sp
        4 -> 16.sp
        5 -> 14.sp
        6 -> 12.sp
        else -> 10.sp
    }
    Text(text = text, fontSize = fontSize, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun BoldText(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun ItalicText(text: String) {
    Text(text = text, fontStyle = FontStyle.Italic, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun BoldItalicText(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun StrikethroughText(text: String) {
    Text(text = text, textDecoration = TextDecoration.LineThrough, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun BlockQuoteText(text: String) {
    Text(text = text, color = Color.Gray, modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp))
}

@Composable
fun LinkText(text: String, url: String) {
    val uriHandler = LocalUriHandler.current
    Text(text = text, color = Color.Blue, modifier = Modifier
        .padding(vertical = 4.dp)
        .clickable { uriHandler.openUri(url) })
}

@Composable
fun ImageElement(altText: String, url: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build(),
    )
    val state by painter.state.collectAsState()
    when (state) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
            CircularProgressIndicator()
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = altText,
            )
        }

        is AsyncImagePainter.State.Error -> {
            Log.i("TAG", "ImageElement: ${(state as AsyncImagePainter.State.Error).result.throwable.message}")
        }
    }
}

@Composable
fun UnorderedListElement(items: List<MarkdownElement.ListItem>) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        items.forEach { item ->
            Row {
                Text(text = "•", modifier = Modifier.padding(end = 8.dp))
                InlineMarkdownText(item.elements)
            }
        }
    }
}

@Composable
fun OrderedListElement(items: List<MarkdownElement.ListItem>) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        items.forEachIndexed { index, item ->
            Row {
                Text(text = "${index + 1}.", modifier = Modifier.padding(end = 8.dp))
                InlineMarkdownText(item.elements)
            }
        }
    }
}

@Composable
fun ListItemElement(elements: List<MarkdownElement>) {
    InlineMarkdownText(elements)
}

@Composable
fun ParagraphText(elements: List<MarkdownElement>) {
    InlineMarkdownText(elements)
}

@Composable
fun InlineMarkdownText(elements: List<MarkdownElement>) {
    val annotatedString = buildAnnotatedString {
        elements.forEach { element ->
            when (element) {
                is MarkdownElement.Bold -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(element.text)
                    }
                }
                is MarkdownElement.Italic -> {
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(element.text)
                    }
                }
                is MarkdownElement.BoldItalic -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                        append(element.text)
                    }
                }
                is MarkdownElement.Strikethrough -> {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                        append(element.text)
                    }
                }
                is MarkdownElement.Text -> {
                    append(element.text)
                }
                is MarkdownElement.Link -> {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append(element.text)
                    }
                }
                else -> {
                    append(element.toString())
                }
            }
        }
    }

    Text(text = annotatedString, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun InlineText(text: String) {
    Text(text = text, modifier = Modifier.padding(vertical = 4.dp))
}

/*@Composable
fun MarkdownRenderer(markdownText: String) {
    val elements = MarkdownParser.parse(markdownText)
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        elements.forEach { element ->
            when (element) {
                is Header -> item {
                    Header(element.level, element.text)
                }

                is Bold -> item {
                    BoldText(element.text)
                }

                is Link -> item {
                    LinkText(element.text, element.url)
                }

                is Image -> item {
                    ImageElement(element.altText, element.url)
                }

                is Paragraph -> item {
                    ParagraphText(element.text)
                }

                is ListItem -> item {
                    ListItemElement(element.text)
                }
            }
        }
    }
}

@Composable
fun Header(level: Int, text: String) {
    val fontSize = when (level) {
        1 -> 30.sp
        2 -> 24.sp
        3 -> 18.sp
        else -> 16.sp
    }
    Text(
        text = text,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun BoldText(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun LinkText(text: String, url: String) {
    val uriHandler = LocalUriHandler.current
    Text(text = text, color = Color.Blue, modifier = Modifier
        .padding(vertical = 4.dp)
        .clickable { uriHandler.openUri(url) })
}

@Composable
fun ImageElement(altText: String, url: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build(),
    )
    val state by painter.state.collectAsState()
    when (state) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
            CircularProgressIndicator()
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = altText,
            )
        }

        is AsyncImagePainter.State.Error -> {
            Log.i("TAG", "ImageElement: ${(state as AsyncImagePainter.State.Error).result.throwable.message}")
        }
    }

}

@Composable
fun ParagraphText(text: String) {
    Text(text = text, modifier = Modifier.padding(vertical = 4.dp))
}


@Composable
fun ListItemElement(text: String) {
    Log.i("TAG", "ListItemElement: $text")
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(text = "•", modifier = Modifier.padding(end = 8.dp))
        Text(text = text)
    }
}*/

/*@Composable
fun MarkdownRenderer(markdownText: String) {
    val elements = MarkdownParser.parse(markdownText)
    Column(modifier = Modifier.padding(16.dp)) {
        elements.forEach { element ->
            when (element) {
                is Header -> Header(element.level, element.text)
                is Bold -> BoldText(element.text)
                is Link -> LinkText(element.text, element.url)
                is Image -> ImageElement(element.altText, element.url)
                is ListItem -> ListItemElement(element.text)
                is Paragraph -> ParagraphText(element.text)
                is Text -> Text(element.text)
            }
        }
    }
}

@Composable
fun Header(level: Int, text: String) {
    val fontSize = when (level) {
        1 -> 30.sp
        2 -> 24.sp
        3 -> 18.sp
        else -> 16.sp
    }
    Text(text = text, fontSize = fontSize, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun BoldText(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
}

@Composable
fun LinkText(text: String, url: String) {
    val uriHandler = LocalUriHandler.current
    Text(text = text, color = Color.Blue, modifier = Modifier
        .padding(vertical = 4.dp)
        .clickable { uriHandler.openUri(url) })
}

@Composable
fun ImageElement(altText: String, url: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(url)
            .crossfade(true)
            .size(Size.ORIGINAL)
            .build(),
    )
    val state by painter.state.collectAsState()
    when (state) {
        is AsyncImagePainter.State.Empty,
        is AsyncImagePainter.State.Loading -> {
            CircularProgressIndicator()
        }

        is AsyncImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = altText,
            )
        }

        is AsyncImagePainter.State.Error -> {
            Log.i("TAG", "ImageElement: ${(state as AsyncImagePainter.State.Error).result.throwable.message}")
        }
    }
}

@Composable
fun ListItemElement(elements: List<MarkdownElement>) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(text = "•", modifier = Modifier.padding(end = 8.dp))
        InlineMarkdownText(elements)
    }
}

@Composable
fun ParagraphText(elements: List<MarkdownElement>) {
    InlineMarkdownText(elements)
}

@Composable
fun InlineMarkdownText(elements: List<MarkdownElement>) {
    val annotatedString = buildAnnotatedString {
        elements.forEach { element ->
            when (element) {
                is MarkdownElement.Bold -> {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(element.text)
                    }
                }
                is MarkdownElement.Text -> {
                    append(element.text)
                }
                is MarkdownElement.Link -> {
                    withStyle(style = SpanStyle(color = Color.Blue)) {
                        append(element.text)
                    }
                }
                else -> {
                    append(element.toString())
                }
            }
        }
    }

    Text(text = annotatedString, modifier = Modifier.padding(vertical = 4.dp))
}*/

