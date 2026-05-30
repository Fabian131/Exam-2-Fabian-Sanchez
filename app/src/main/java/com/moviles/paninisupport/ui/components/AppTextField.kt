package com.moviles.paninisupport.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import com.moviles.paninisupport.ui.theme.AppBorder
import com.moviles.paninisupport.ui.theme.AppPrimary
import com.moviles.paninisupport.ui.theme.AppSecondaryText

@Composable
fun AppTextField(
    value: String,
    label: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                readOnly = readOnly,
                singleLine = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = AppSecondaryText
                    )
                },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppBorder,
                    unfocusedBorderColor = AppBorder,
                    focusedContainerColor = AppPrimary.copy(alpha = 0f),
                    unfocusedContainerColor = AppPrimary.copy(alpha = 0f),
                    cursorColor = AppPrimary,
                    focusedTextColor = AppPrimary,
                    unfocusedTextColor = AppPrimary
                )
            )
            if (onClick != null) {
                Spacer(
                    Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onClick
                        )
                )
            }
        }
    }
}