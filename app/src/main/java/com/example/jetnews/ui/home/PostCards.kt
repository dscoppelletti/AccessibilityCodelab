/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetnews.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetnews.R
import com.example.jetnews.data.posts.post1
import com.example.jetnews.data.posts.post3
import com.example.jetnews.model.Post
import com.example.jetnews.ui.theme.JetnewsTheme

@Composable
fun PostCardHistory(post: Post, navigateToArticle: (String) -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    /* BEGIN-5 - Custom actions */
    val showFewerLabel = stringResource(R.string.cd_show_fewer)
    /* END-5 */
    Row(
        Modifier.clickable(
            /* BEGIN-4 - Click labels */
            // Clickable elements in your app by default don't provide any
            // information on what clicking that element will do.Therefore,
            // accessibility services like TalkBack will use a very generic
            // default description.
            // To provide the best experience for users with accessibility
            // needs, we can provide a specific description that explains what
            // will happen when the user clicks this element.
            onClickLabel = stringResource(R.string.action_read_article)
            /* END-4 */
        ) { navigateToArticle(post.id) }
            /* BEGIN-5.2 - Custom actions */
            // However, by removing the semantics of the IconButton, there is
            // now no way to execute the action anymore. We can add the action
            // to the list item instead by adding a custom action in the
            // semantics modifier.
            // Now we can use the custom action popup in TalkBack to apply the
            // action. This becomes more and more relevant as the number of
            // actions inside a list item increases.
            .semantics {
                customActions = listOf(
                    CustomAccessibilityAction(
                        label = showFewerLabel,
                        // action returns boolean to indicate success
                        action = { openDialog = true; true }
                    )
                )
            }
            /* END-5.2 */
    ) {
        Image(
            painter = painterResource(post.imageThumbId),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .size(40.dp, 40.dp)
                .clip(MaterialTheme.shapes.small)
        )
        Column(
            Modifier
                .weight(1f)
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            Text(post.title, style = MaterialTheme.typography.subtitle1)
            Row(Modifier.padding(top = 4.dp)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    val textStyle = MaterialTheme.typography.body2
                    Text(
                        text = post.metadata.author.name,
                        style = textStyle
                    )
                    Text(
                        text = " - ${post.metadata.readTimeMinutes} min read",
                        style = textStyle
                    )
                }
            }
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            /* BEGIN-3.2 - Touch target size */
            // In our use case, there is an easier way to make sure the touch
            // target is at least 48dp. We can make use of the Material
            // component IconButton that will handle this for us.
//            Icon(
//                imageVector = Icons.Default.Close,
//                contentDescription = stringResource(R.string.cd_show_fewer),
//                modifier = Modifier
//                    .clickable { openDialog = true }
//                    /* BEGIN-3.1 - Touch target size */
//                    // Any on-screen element that someone can click, touch, or
//                    // otherwise interact with should be large enough for
//                    // reliable interaction. You should make sure these elements
//                    // have a width and height of at least 48dp.
//                    // If these controls are sized dynamically, or resize based
//                    // on the size of their content, consider using the sizeIn
//                    // modifier to set a lower bound on their dimensions.
//                    // Some Material components set these sizes for you. For
//                    // example, the Button composable has its MinHeight set to
//                    // 36dp, and uses 8dp vertical padding. This adds up to the
//                    // required 48dp height.
//                    //
//                    // The order of modifier functions is significant. Since
//                    // each function makes changes to the Modifier returned by
//                    // the previous function, the sequence affects the final
//                    // result. In this case, we apply the padding before setting
//                    // the size, but after applying the clickable modifier. This
//                    // way the padding will be added to the size, and the whole
//                    // element will be clickable.
//                    .padding(12.dp)
//                    /* END-3.1 */
//                    .size(24.dp)
//            )
            IconButton(
                /* BEGIN-5.1 - Custom actions */
                // By default, both the Row and the IconButton composable are
                // clickable and as a result will be focused by TalkBack. This
                // happens for each item in our list, which means a lot of
                // swiping while navigating the list. We rather want the action
                // related to the IconButton to be included as a custom action
                // on the list item. We can tell Accessibility Services not to
                // interact with this Icon by using the clearAndSetSemantics
                // modifier.
                modifier = Modifier.clearAndSetSemantics { },
                /* END-5.1 */
                onClick = { openDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cd_show_fewer)
                )
            }
            /* END-3.2 */
        }
    }
    if (openDialog) {
        AlertDialog(
            modifier = Modifier.padding(20.dp),
            onDismissRequest = { openDialog = false },
            title = {
                Text(
                    text = stringResource(id = R.string.fewer_stories),
                    style = MaterialTheme.typography.h6
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.fewer_stories_content),
                    style = MaterialTheme.typography.body1
                )
            },
            confirmButton = {
                Text(
                    text = stringResource(id = R.string.agree),
                    style = MaterialTheme.typography.button,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(15.dp)
                        .clickable { openDialog = false }
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostCardPopular(
    post: Post,
    navigateToArticle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    /* BEGIN-4 - Click labels */
    val readArticleLabel = stringResource(id = R.string.action_read_article)
    /* END-4 */
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.size(280.dp, 240.dp)
            /* BEGIN-4 - Click labels */
            // This composable uses the Card composable internally, which does
            // not allow you to directly set the click label. Instead, you can
            // use the semantics modifier to set the click label:
            .semantics { onClick(label = readArticleLabel, action = null) },
            /* END-4 */
        onClick = { navigateToArticle(post.id) }
    ) {
        Column {

            Image(
                painter = painterResource(post.imageId),
                contentDescription = null, // decorative
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = post.title,
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = post.metadata.author.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )

                Text(
                    text = stringResource(
                        id = R.string.home_post_min_read,
                        formatArgs = arrayOf(
                            post.metadata.date,
                            post.metadata.readTimeMinutes
                        )
                    ),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Preview("Regular colors")
@Preview("Dark colors", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPostCardPopular() {
    JetnewsTheme {
        Surface {
            PostCardPopular(post1, {})
        }
    }
}

@Preview("Post History card")
@Composable
fun HistoryPostPreview() {
    JetnewsTheme {
        Surface {
            PostCardHistory(post3) {}
        }
    }
}
