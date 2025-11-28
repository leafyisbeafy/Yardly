package com.example.yardly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                )
                .padding(horizontal = Dimens.ScreenPaddingHorizontal, vertical = Dimens.SpacingLarge),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { /* TODO: Handle notifications */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = Dimens.SpacingLarge)
            ) {
                Text(
                    text = "Channel",
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun TopBarPreview() {
    YardlyTheme(isDarkMode = false) {
        TopBar()
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun TopBarDarkPreview() {
    YardlyTheme(isDarkMode = true) {
        TopBar()
    }
}

