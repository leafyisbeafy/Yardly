package com.example.yardly.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yardly.ui.theme.Category
import com.example.yardly.ui.theme.Dimens
import com.example.yardly.ui.theme.YardlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionNavigation(
    selectedSection: String,
    onSectionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = Category.all

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.SpacingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens.SpacingSmall),
        contentPadding = PaddingValues(horizontal = Dimens.ScreenPaddingHorizontal)
    ) {
        items(categories) { category ->
            val isSelected = selectedSection == category.id

            val containerColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
                label = "containerColor"
            )

            val contentColor by animateColorAsState(
                targetValue = if (isSelected) category.onColor else MaterialTheme.colorScheme.onBackground,
                label = "contentColor"
            )

            val borderColor by animateColorAsState(
                targetValue = if (isSelected) Color.Transparent else category.color,
                label = "borderColor"
            )

            Box(
                modifier = Modifier
                    .height(40.dp)
                    .border(
                        width = if (isSelected) 0.dp else 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(50)
                    )
                    .background(
                        color = containerColor,
                        shape = RoundedCornerShape(50)
                    )
                    .clip(RoundedCornerShape(50))
                    .clickable { onSectionSelected(category.id) }
                    .padding(horizontal = Dimens.SpacingXLarge),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.label,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun SectionNavigationPreview() {
    YardlyTheme(isDarkMode = false) {
        SectionNavigation(
            selectedSection = "home-default",
            onSectionSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun SectionNavigationDarkPreview() {
    YardlyTheme(isDarkMode = true) {
        SectionNavigation(
            selectedSection = "textbook",
            onSectionSelected = {}
        )
    }
}

