package com.yite.senaoapp.extension

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

/**
 * 使元件可點擊，但移除ripple (漣漪效果)。
 *
 * @param enabled 是否啟用點擊，`false` 時點擊無效。
 * @param onClickLabel 點擊動作的語義化/無障礙標籤。
 * @param role UI 元件類型，供無障礙服務使用。
 * @param onClick 使用者點擊元件時的回呼函式。
 */
fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier =
    this.then(
        Modifier.clickable(
            interactionSource = null,
            indication = null,
            enabled = enabled,
            onClickLabel = onClickLabel,
            role = role,
            onClick = onClick,
        ),
    )
