package dev.catsuperberg.pexels.app.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import dev.catsuperberg.pexels.app.R
import dev.catsuperberg.pexels.app.presentation.view.model.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val searchPrompt = viewModel.searchPrompt.collectAsState()

    val focusManager = LocalFocusManager.current
    TextField(
        value = searchPrompt.value,
        onValueChange = viewModel::onSearchChange,
        placeholder = { Text(stringResource(R.string.search)) },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(R.string.search_input_field),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 20.dp, end = 4.dp, top = 17.dp, bottom = 17.dp)
                    .size(16.dp)
            )
        },
        trailingIcon = {
            if (searchPrompt.value.isNotEmpty())
                Icon(
                    painterResource(id = R.drawable.ic_clear),
                    contentDescription = stringResource(R.string.clear_search_field),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(start = 4.dp, end = 20.dp, top = 17.dp, bottom = 17.dp)
                        .size(14.dp)
                        .clickable { viewModel.onClearSearch() }
                )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                viewModel.onSearch()
                focusManager.clearFocus()
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        shape = CircleShape,
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    )
}
