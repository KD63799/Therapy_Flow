package com.example.therapy_flow.design_system

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    hintColor: Color,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    trailingText: String? = null,
    onLeadingClick: () -> Unit = {},
    onTrailingClick: () -> Unit = {}
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = hint, color = hintColor) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier,
        // Utilisation des couleurs par défaut de Material3
        colors = TextFieldDefaults.colors(),
        leadingIcon = if (leadingIcon != null) {
            {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable { onLeadingClick() }
                )
            }
        } else null,
        trailingIcon = if (trailingIcon != null) {
            {
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable { onTrailingClick() }
                )
            }
        } else if (trailingText != null) {
            {
                Text(
                    text = trailingText,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onTrailingClick() }
                )
            }
        } else null,
    )
}
