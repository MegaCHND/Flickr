package com.example.flickr.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.core.text.HtmlCompat
import coil3.compose.AsyncImage
import com.aghajari.compose.text.AnnotatedText
import com.aghajari.compose.text.asAnnotatedString
import com.example.flickr.R
import com.example.flickr.entities.ImageData
import com.example.flickr.utils.convertDate
import com.example.flickr.utils.nullToEmpty

@Composable
fun FlickrViewScreen(
    state: ImageData?,
    modifier: Modifier = Modifier
) {
    state?.let {
        val spannedDescription = HtmlCompat.fromHtml(it.description, 0)
        val context = LocalContext.current
        val labelTitle = remember { context.getString(R.string.label_title) }
        val labelAuthor = remember { context.getString(R.string.label_author) }
        val labelDescription = remember { context.getString(R.string.label_description) }
        val labelPublishedDate = remember { context.getString(R.string.label_date) }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                it.uri,
                contentDescription = it.title,
                modifier = Modifier.fillMaxWidth()
            )
            Text(it.title,
                modifier = Modifier.semantics {
                contentDescription = labelTitle + it.title
            })
            AnnotatedText(spannedDescription.asAnnotatedString(),
                modifier = Modifier.semantics {
                    contentDescription = labelDescription + it.description
                })
            Text(it.author,
                modifier = Modifier.semantics {
                contentDescription = labelAuthor + it.author
            })
            Text(it.publishedDate.convertDate().nullToEmpty(),
                modifier = Modifier.semantics {
                contentDescription = labelPublishedDate + it.publishedDate
            })
        }
    } ?: run {
        Text("No Image Data")
    }
}
