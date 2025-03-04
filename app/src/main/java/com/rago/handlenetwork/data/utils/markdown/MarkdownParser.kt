package com.rago.handlenetwork.data.utils.markdown

sealed class MarkdownElement {
    data class Header(val level: Int, val text: String) : MarkdownElement()
    data class Bold(val text: String) : MarkdownElement()
    data class Italic(val text: String) : MarkdownElement()
    data class BoldItalic(val text: String) : MarkdownElement()
    data class Strikethrough(val text: String) : MarkdownElement()
    data class BlockQuote(val text: String) : MarkdownElement()
    data class Text(val text: String) : MarkdownElement()
    data class Link(val text: String, val url: String) : MarkdownElement()
    data class Image(val altText: String, val url: String) : MarkdownElement()
    data class UnorderedList(val items: List<ListItem>) : MarkdownElement()
    data class OrderedList(val items: List<ListItem>) : MarkdownElement()
    data class ListItem(val elements: List<MarkdownElement>) : MarkdownElement()
    data class Paragraph(val elements: List<MarkdownElement>) : MarkdownElement()
}

object MarkdownParser {
    fun parse(markdown: String): List<MarkdownElement> {
        val lines = markdown.lines()
        val elements = mutableListOf<MarkdownElement>()

        var currentListItems = mutableListOf<MarkdownElement.ListItem>()
        var isOrderedList = false

        for (line in lines) {
            when {
                line.startsWith("# ") -> elements.add(MarkdownElement.Header(1, line.removePrefix("# ").trim()))
                line.startsWith("## ") -> elements.add(MarkdownElement.Header(2, line.removePrefix("## ").trim()))
                line.startsWith("### ") -> elements.add(MarkdownElement.Header(3, line.removePrefix("### ").trim()))
                line.startsWith("#### ") -> elements.add(MarkdownElement.Header(4, line.removePrefix("#### ").trim()))
                line.startsWith("##### ") -> elements.add(MarkdownElement.Header(5, line.removePrefix("##### ").trim()))
                line.startsWith("###### ") -> elements.add(MarkdownElement.Header(6, line.removePrefix("###### ").trim()))
                line.startsWith("- ") -> {
                    currentListItems.add(MarkdownElement.ListItem(parseInlineElements(line.removePrefix("- ").trim())))
                    isOrderedList = false
                }
                line.matches(Regex("^\\d+\\. .*")) -> {
                    currentListItems.add(MarkdownElement.ListItem(parseInlineElements(line.substringAfter(". ").trim())))
                    isOrderedList = true
                }
                line.startsWith("> ") -> elements.add(MarkdownElement.BlockQuote(line.removePrefix("> ").trim()))
                line.contains("![") && line.contains("](") && line.contains(")") -> {
                    val altText = line.substringAfter("![").substringBefore("]")
                    val url = line.substringAfter("](").substringBefore(")")
                    elements.add(MarkdownElement.Image(altText, url))
                }
                line.contains("[") && line.contains("](") && line.contains(")") -> {
                    val text = line.substringAfter("[").substringBefore("]")
                    val url = line.substringAfter("](").substringBefore(")")
                    elements.add(MarkdownElement.Link(text, url))
                }
                line.isNotBlank() -> elements.add(MarkdownElement.Paragraph(parseInlineElements(line.trim())))
                else -> {
                    if (currentListItems.isNotEmpty()) {
                        if (isOrderedList) {
                            elements.add(MarkdownElement.OrderedList(currentListItems))
                        } else {
                            elements.add(MarkdownElement.UnorderedList(currentListItems))
                        }
                        currentListItems = mutableListOf()
                    }
                }
            }
        }

        if (currentListItems.isNotEmpty()) {
            if (isOrderedList) {
                elements.add(MarkdownElement.OrderedList(currentListItems))
            } else {
                elements.add(MarkdownElement.UnorderedList(currentListItems))
            }
        }

        return elements
    }

    private fun parseInlineElements(text: String): List<MarkdownElement> {
        val elements = mutableListOf<MarkdownElement>()
        var remainingText = text

        while (remainingText.isNotEmpty()) {
            val boldItalicStart = remainingText.indexOf("***")
            val boldStart = remainingText.indexOf("**")
            val italicStart = remainingText.indexOf("*")
            val strikethroughStart = remainingText.indexOf("~~")

            val nextSpecial = listOf(boldItalicStart, boldStart, italicStart, strikethroughStart)
                .filter { it != -1 }
                .minOrNull() ?: -1

            if (nextSpecial == -1) {
                elements.add(MarkdownElement.Text(remainingText))
                break
            }

            if (nextSpecial > 0) {
                elements.add(MarkdownElement.Text(remainingText.substring(0, nextSpecial)))
            }

            when (nextSpecial) {
                boldItalicStart -> {
                    val boldItalicEnd = remainingText.indexOf("***", boldItalicStart + 3)
                    if (boldItalicEnd != -1) {
                        elements.add(MarkdownElement.BoldItalic(remainingText.substring(boldItalicStart + 3, boldItalicEnd)))
                        remainingText = remainingText.substring(boldItalicEnd + 3)
                    } else {
                        elements.add(MarkdownElement.Text(remainingText))
                        break
                    }
                }
                boldStart -> {
                    val boldEnd = remainingText.indexOf("**", boldStart + 2)
                    if (boldEnd != -1) {
                        elements.add(MarkdownElement.Bold(remainingText.substring(boldStart + 2, boldEnd)))
                        remainingText = remainingText.substring(boldEnd + 2)
                    } else {
                        elements.add(MarkdownElement.Text(remainingText))
                        break
                    }
                }
                italicStart -> {
                    val italicEnd = remainingText.indexOf("*", italicStart + 1)
                    if (italicEnd != -1) {
                        elements.add(MarkdownElement.Italic(remainingText.substring(italicStart + 1, italicEnd)))
                        remainingText = remainingText.substring(italicEnd + 1)
                    } else {
                        elements.add(MarkdownElement.Text(remainingText))
                        break
                    }
                }
                strikethroughStart -> {
                    val strikethroughEnd = remainingText.indexOf("~~", strikethroughStart + 2)
                    if (strikethroughEnd != -1) {
                        elements.add(MarkdownElement.Strikethrough(remainingText.substring(strikethroughStart + 2, strikethroughEnd)))
                        remainingText = remainingText.substring(strikethroughEnd + 2)
                    } else {
                        elements.add(MarkdownElement.Text(remainingText))
                        break
                    }
                }
            }
        }

        return elements
    }
}

/*sealed class MarkdownElement {
    data class Header(val level: Int, val text: String) : MarkdownElement()
    data class Bold(val text: String) : MarkdownElement()
    data class Text(val text: String) : MarkdownElement()
    data class Link(val text: String, val url: String) : MarkdownElement()
    data class Image(val altText: String, val url: String) : MarkdownElement()
    data class ListItem(val text: List<MarkdownElement>) : MarkdownElement()
    data class Paragraph(val text: List<MarkdownElement>) : MarkdownElement()
}

object MarkdownParser {
    fun parse(markdown: String): List<MarkdownElement> {
        val lines = markdown.lines()
        val elements = mutableListOf<MarkdownElement>()

        for (line in lines) {
            when {
                line.startsWith("# ") -> elements.add(MarkdownElement.Header(1, line.removePrefix("# ").trim()))
                line.startsWith("## ") -> elements.add(MarkdownElement.Header(2, line.removePrefix("## ").trim()))
                line.startsWith("### ") -> elements.add(MarkdownElement.Header(3, line.removePrefix("### ").trim()))
                line.startsWith("- ") -> elements.add(MarkdownElement.ListItem(parseInlineElements(line.removePrefix("- ").trim())))
                line.contains("![") && line.contains("](") && line.contains(")") -> {
                    val altText = line.substringAfter("![").substringBefore("]")
                    val url = line.substringAfter("](").substringBefore(")")
                    elements.add(MarkdownElement.Image(altText, url))
                }
                line.contains("[") && line.contains("](") && line.contains(")") -> {
                    val text = line.substringAfter("[").substringBefore("]")
                    val url = line.substringAfter("](").substringBefore(")")
                    elements.add(MarkdownElement.Link(text, url))
                }
                line.isNotBlank() -> elements.add(MarkdownElement.Paragraph(parseInlineElements(line.trim())))
            }
        }

        return elements
    }

    private fun parseInlineElements(text: String): List<MarkdownElement> {
        val elements = mutableListOf<MarkdownElement>()
        var remainingText = text

        while (remainingText.isNotEmpty()) {
            val boldStart = remainingText.indexOf("**")
            if (boldStart == -1) {
                elements.add(MarkdownElement.Text(remainingText))
                break
            }

            val boldEnd = remainingText.indexOf("**", boldStart + 2)
            if (boldEnd == -1) {
                elements.add(MarkdownElement.Text(remainingText))
                break
            }

            if (boldStart > 0) {
                elements.add(MarkdownElement.Text(remainingText.substring(0, boldStart)))
            }
            elements.add(MarkdownElement.Bold(remainingText.substring(boldStart + 2, boldEnd)))
            remainingText = remainingText.substring(boldEnd + 2)
        }

        return elements
    }
}*/

/*
sealed class MarkdownElement {
    data class Header(val level: Int, val text: String) : MarkdownElement()
    data class Bold(val text: String) : MarkdownElement()
    data class Link(val text: String, val url: String) : MarkdownElement()
    data class Image(val altText: String, val url: String) : MarkdownElement()
    data class ListItem(val text: String) : MarkdownElement()
    data class Paragraph(val text: String) : MarkdownElement()
}

object MarkdownParser {
    fun parse(markdown: String): List<MarkdownElement> {
        val lines = markdown.lines()
        val elements = mutableListOf<MarkdownElement>()

        for (line in lines) {
            when {
                line.startsWith("# ") -> elements.add(MarkdownElement.Header(1, line.removePrefix("# ").trim()))
                line.startsWith("## ") -> elements.add(MarkdownElement.Header(2, line.removePrefix("## ").trim()))
                line.startsWith("### ") -> elements.add(MarkdownElement.Header(3, line.removePrefix("### ").trim()))
                line.startsWith("**") && line.endsWith("**") -> elements.add(MarkdownElement.Bold(line.removeSurrounding("**").trim()))
                line.startsWith("- ") -> elements.add(MarkdownElement.ListItem(line.removePrefix("- ").trim()))
                line.contains("![") && line.contains("](") && line.contains(")") -> {
                    val altText = line.substringAfter("![").substringBefore("]")
                    val url = line.substringAfter("](").substringBefore(")")
                    elements.add(MarkdownElement.Image(altText, url))
                }
                line.contains("[") && line.contains("](") && line.contains(")") -> {
                    val text = line.substringAfter("[").substringBefore("]")
                    val url = line.substringAfter("](").substringBefore(")")
                    elements.add(MarkdownElement.Link(text, url))
                }
                line.isNotBlank() -> elements.add(MarkdownElement.Paragraph(line.trim()))
            }
        }

        return elements
    }
}*/
