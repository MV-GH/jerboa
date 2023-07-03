package com.jerboa.ui.components.register

import android.text.style.ClickableSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jerboa.DEFAULT_LEMMY_INSTANCES
import com.jerboa.R
import com.jerboa.datatypes.types.Register
import com.jerboa.onAutofill
import com.jerboa.onAutofill2
import com.jerboa.ui.components.post.composables.CheckboxIsNsfw
import com.jerboa.ui.theme.SMALL_PADDING


@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String? = null,
    text: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        placeholder = { placeholder?.let { Text(text = it) } },
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Text,
            autoCorrect = false,
        ),
        modifier = modifier,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MyEmailTextField(
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String? = null,
    text: String,
    onValueChange: (String) -> Unit,
) {
    val t = LocalAutofillTree.current

    val autofill = LocalAutofill.current

    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = true,
        placeholder = { placeholder?.let { Text(text = it) } },
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Email,
            autoCorrect = false,
        ),
        modifier = modifier.onAutofill2(t, autofill, AutofillType.EmailAddress) {
        onValueChange(it)
 },
    )
}

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    password: String,
    onValueChange: (String) -> Unit,
) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier,
        value = password,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(text = stringResource(R.string.login_password)) },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisibility) {
                Icons.Outlined.Visibility
            } else {
                Icons.Outlined.VisibilityOff
            }

            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(imageVector = image, "")
            }
        },
    )
}


@Composable
fun MyCheckBox(checked: Boolean, onCheckedChange: ((Boolean) -> Unit) , textId: Int) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start= 0.dp, end = 0.dp, top = SMALL_PADDING, bottom = SMALL_PADDING)
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
    ) {
        Text(
            text = stringResource(textId),
            )
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    onClickRegister: (form: Register, instance: String) -> Unit = { _: Register, _: String -> },
) {
    var instance by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVerify by rememberSaveable { mutableStateOf("") }
    var nsfw by rememberSaveable { mutableStateOf(false) }
    val instanceOptions = DEFAULT_LEMMY_INSTANCES
    var expanded by remember { mutableStateOf(false) }
    var wasAutofilled by remember { mutableStateOf(false) }

    val isValid =
        instance.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()

    val form = Register(
        username = username.trim(),
        password = password.take(60),
        password_verify = passwordVerify.take(60),
        show_nsfw = nsfw,
        email = email.ifBlank { null },


        )
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(OutlinedTextFieldDefaults.MinWidth),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor(),
                    label = { Text(stringResource(R.string.login_instance)) },
                    placeholder = { Text(stringResource(R.string.login_instance_placeholder)) },
                    value = instance,
                    singleLine = true,
                    onValueChange = { instance = it },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Uri),
                )
                val filteringOptions = instanceOptions.filter { it.contains(instance, ignoreCase = true) }
                if (filteringOptions.isNotEmpty()) {
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        properties = PopupProperties(focusable = false),
                        modifier = Modifier.exposedDropdownSize(true),
                    ) {
                        filteringOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                modifier = Modifier.exposedDropdownSize(),
                                text = {
                                    Text(text = selectionOption)
                                },
                                onClick = {
                                    instance = selectionOption
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }

            MyTextField(
                modifier = Modifier
                    .background(if (wasAutofilled) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                    .onAutofill(AutofillType.NewUsername) {
                        username = it
                        wasAutofilled = true
                    },
                label = stringResource(R.string.username),
                text = username,
                onValueChange = { username = it },
            )
            // TODO: possibly optional

            MyEmailTextField(
                modifier = Modifier
                   // .background(if (wasAutofilled) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
,
                label = stringResource(R.string.email),
                text = email,
                onValueChange = { email = it },
            )
            PasswordField(
                modifier = Modifier
                    .background(if (wasAutofilled) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                    .onAutofill(AutofillType.NewPassword) {
                        password = it
                        wasAutofilled = true
                    },
                password = password,
                onValueChange = { password = it },
            )
            PasswordField(
                modifier = Modifier
                    .background(if (wasAutofilled) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                    .onAutofill(AutofillType.NewPassword) {
                        passwordVerify = it
                        wasAutofilled = true
                    },
                password = passwordVerify,
                onValueChange = { passwordVerify = it },
            )

            MyCheckBox(
                checked = nsfw,
                onCheckedChange = { nsfw = it },
                textId = R.string.register_show_nsfw
            )

            Button(
                enabled = isValid && !loading,
                onClick = { onClickRegister(form, instance) },
                modifier = Modifier.padding(top = 10.dp),
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(stringResource(R.string.register))
                }
            }
        }
    }
}

@Preview
@Composable
fun RegisterFormPreview() {
    RegisterForm()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterHeader(
    navController: NavController = rememberNavController(),
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.register),
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Icon(
                    Icons.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.login_back),
                )
            }
        },
    )
}

@Preview
@Composable
fun LoginHeaderPreview() {
    RegisterHeader()
}
