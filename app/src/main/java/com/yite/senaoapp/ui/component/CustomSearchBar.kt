package com.yite.senaoapp.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yite.senaoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    CustomTextField(
        modifier = modifier,
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(24.dp),
                painter = painterResource(
                    id = R.drawable.icon_search,
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        },
        placeholderText = stringResource(R.string.input_keyword),
        value = value,
        textStyle = MaterialTheme.typography.bodyLarge,
        onValueChange = onValueChange
    )
}