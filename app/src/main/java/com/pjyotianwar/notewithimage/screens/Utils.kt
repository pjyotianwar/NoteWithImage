package com.pjyotianwar.notewithimage.screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.CircleCropTransformation
import com.pjyotianwar.notewithimage.models.Notes
import java.io.File

enum class LoginResult {
    Waiting, Wrong, Success, NoUser
}

enum class SignUpResult {
    Success, UserExists, Waiting
}

@Composable
fun NoteItem(
    notes: Notes,
    onEditClicked: (Notes) -> Unit,
    onDeleteClicked: (Notes) -> Unit,
    onClick: (Notes)->Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = MaterialTheme.colors.secondaryVariant.copy(0.6f),
            ).clickable {
                    onClick (notes)
            },
        elevation = 5.dp
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp))
                    .background(color = MaterialTheme.colors.primary.copy(0.6f))
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AllTitles(title = notes.noteTitle)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AllIcons(
                        imageVector = Icons.Default.Edit,
                        onIconClicked = { onEditClicked(notes) }
                    )
                    AllIcons(
                        imageVector = Icons.Default.Delete,
                        onIconClicked = { onDeleteClicked(notes) }
                    )
                }
            }
            AllDescription(desc = notes.noteDescription)

            val noteImgList = remember{
                notes.noteImages.split(";;", limit = 10).filter { it.isNotEmpty() }
            }

            Log.d("noteImgList", "$noteImgList")

            LazyRow(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                items(noteImgList){ imgUri->
                    Card(
                        modifier = Modifier
                            .height(400.dp)
                            .width(300.dp)
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {

                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imgUri.toUri())
                                .build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .height(400.dp)
                                .width(300.dp),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    }
                }
            }
        }
    }
}

fun makeToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputText(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    maxLine: Int = 1,
    onTextChange: (String) -> Unit,
    onImeAction: () -> Unit = {},
    isError: Boolean
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = text,
        onValueChange = {
            onTextChange(it)
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        ),
        maxLines = maxLine,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        }),
        modifier = modifier,
        isError = isError
    )

}

@Composable
fun AllTitles(title: String = "Title", modifier: Modifier=Modifier.padding(start = 8.dp)) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        style = MaterialTheme.typography.h6,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun AllDescription(desc: String = "Description", modifier: Modifier=Modifier.padding(start = 8.dp)) {
    Text(
        text = desc,
        maxLines = 3,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun AllIcons(
    cdesc: String = "Null",
    imageVector: ImageVector,
    onIconClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onIconClicked
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = cdesc,
            modifier = modifier
        )
    }
}

@Composable
fun imageLoad(
    imageData: Uri,
    contentScale: ContentScale= ContentScale.Crop,
    modifier: Modifier = Modifier
){
    Image(
        painter = rememberImagePainter(
            data = imageData
        ),
        contentDescription = null,
        modifier = modifier,
        alignment = Alignment.Center,
        contentScale = contentScale
    )
}