package com.ferret.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ferret.model.FerretDetailSection

@Composable
internal fun FerretDetailContent(
    sections: List<FerretDetailSection>,
    modifier: Modifier = Modifier,
    additionalContent: LazyListScope.() -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = sections.filter { section ->
                section.items.any { item ->
                    !item.value.isNullOrBlank()
                }
            },
            key = { section ->
                section.title
            },
        ) { section ->
            FerretDetailSectionCard(
                section = section,
            )
        }

        additionalContent()
    }
}