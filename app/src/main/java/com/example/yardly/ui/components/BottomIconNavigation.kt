package com.example.yardly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@Composable
fun BottomIconNavigation(
    selectedSection: String,
    onSectionSelected: (String) -> Unit
) {
    val sections = listOf(
        "messenger" to "Messenger",
        "notification" to "Notification",
        "watchlist" to "Watchlist",
        "profile" to "Profile"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            )
            .navigationBarsPadding()
            .padding(vertical = Dimens.SpacingLarge, horizontal = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        sections.forEach { (sectionKey, sectionName) ->
            val isSelected = selectedSection == sectionKey
            Button(
                onClick = { onSectionSelected(sectionKey) },
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = Dimens.SpacingSmall)
            ) {
                Text(
                    text = sectionName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode - Home")
@Composable
fun BottomIconNavigationPreview() {
    YardlyTheme(isDarkMode = false) {
        BottomIconNavigation(
            selectedSection = "home",
            onSectionSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Light Mode - Profile Selected")
@Composable
fun BottomIconNavigationProfilePreview() {
    YardlyTheme(isDarkMode = false) {
        BottomIconNavigation(
            selectedSection = "profile",
            onSectionSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun BottomIconNavigationDarkPreview() {
    YardlyTheme(isDarkMode = true) {
        BottomIconNavigation(
            selectedSection = "watchlist",
            onSectionSelected = {}
        )
    }
}

