package com.example.planner.presentation.bottomNavigationBar

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planner.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavigationBar(
    navController: NavController,
    selectedItem: Int,
    viewModel: BottomNavigationBarViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White)
    ) {
        val items = listOf(
            BottomNavigationItem("dashboard", R.drawable.taboneselected, R.drawable.tabone),
            BottomNavigationItem("days", R.drawable.tabtwoselected, R.drawable.tabtwo),
            BottomNavigationItem("plan", R.drawable.tabthreeselected, R.drawable.tabthree),
            BottomNavigationItem("settings", R.drawable.tabfourselected, R.drawable.tabfour)
        )

        var selectedItemIndex by rememberSaveable { mutableStateOf(selectedItem) }

        LaunchedEffect(key1 = viewModel.effect) {
            viewModel.effect.collect { effect ->
                if (effect is BottomNavigationBarEffect.NavigateToNextScreen) {
                    navController.navigate(effect.itemName)
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize()
        ) {
            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        selectedItemIndex = index
                        viewModel.loadNextScreen(item.title)
                    },
                    item = item
                )
            }
        }
    }
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Int,
    val unselectedIcon: Int
)
