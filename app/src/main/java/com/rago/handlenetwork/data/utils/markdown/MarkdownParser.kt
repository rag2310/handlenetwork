package com.rago.handlenetwork.data.utils.markdown

sealed class MarkdownElement {
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
}

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
