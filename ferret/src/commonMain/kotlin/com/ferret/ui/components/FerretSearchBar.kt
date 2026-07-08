package com.ferret.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Filter1
import androidx.compose.material.icons.filled.Filter4
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.focus.FocusRequester
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun FerretSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    enabled: Boolean = true,
    onFilterClick: (() -> Unit)? = null,
    filterActive: Boolean = false,
    onClear: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    focusRequester: FocusRequester? = null,
    height: Dp = 44.dp,
    shape: Shape = RoundedCornerShape(50),
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .height(height)
                .background(containerColor, shape)
                .padding(horizontal = 12.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp),
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            ) {
                if (query.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = placeholderColor,
                    )
                }
                var fieldModifier = Modifier.fillMaxWidth()
                if (focusRequester != null) {
                    fieldModifier = fieldModifier.focusRequester(focusRequester)
                }
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    enabled = enabled,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = contentColor),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(contentColor),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions.takeIf { it != KeyboardActions.Default }
                        ?: KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    interactionSource = remember { MutableInteractionSource() },
                    modifier = fieldModifier,
                )
            }
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = { (onClear ?: { onQueryChange("") }).invoke() },
                    modifier = Modifier.size(28.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear search",
                        tint = iconColor,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }

        if (onFilterClick != null) {
            Box(modifier = Modifier.size(8.dp))
            Box {
                IconButton(onClick = onFilterClick) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = "Filters",
                        tint = iconColor,
                    )
                }
                if (filterActive) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 6.dp, end = 6.dp)
                            .size(6.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape),
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun FerretSearchBarEmptyPreview() {
    MaterialTheme {
        Surface {
            var query by remember { mutableStateOf("") }
            FerretSearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = "Search HTTP requests...",
                onFilterClick = {},
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun FerretSearchBarActiveFilterPreview() {
    MaterialTheme {
        Surface {
            var query by remember { mutableStateOf("auth/login") }
            FerretSearchBar(
                query = query,
                onQueryChange = { query = it },
                placeholder = "Search HTTP requests...",
                onFilterClick = {},
                filterActive = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun FerretSearchBarNoFilterPreview() {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                var query by remember { mutableStateOf("") }
                FerretSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = "Search WebSocket connections...",
                )
            }
        }
    }
}