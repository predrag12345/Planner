package com.example.planner.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planner.presentation.bottomNavigationBar.BottomNavigationBar
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.viewState

    LaunchedEffect(Unit) {
        viewModel.setEvent(DashboardEvent.LoadCalendar)
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DashboardEffect.NavigateToDayDetail -> {
                    navController.navigate("days/${effect.date}")
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, 0)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 8.dp, vertical = 16.dp)
        ) {
            Text(
                text = state.monthTitle,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )

            val daysOfWeek = listOf("M", "T", "W", "T", "F", "S", "S")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            val weeks = state.daysInMonth.chunked(7)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                weeks.forEach { week ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val fullWeek =
                            if (week.size < 7) week + List(7 - week.size) { null } else week
                        fullWeek.forEach { date ->
                            if (date == null) {
                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(2.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .padding(2.dp)
                                        .border(
                                            1.dp,
                                            Color.Gray,
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .background(
                                            if (date == state.today) Color(0xFF4A7DDF) else Color(0xFFFBFBFB),
                                            shape = MaterialTheme.shapes.medium
                                        )
                                        .clickable {
                                            viewModel.setEvent(DashboardEvent.DayClicked(date))
                                        },
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Text(
                                            text = date.dayOfMonth.toString(),
                                            color = if (date == state.today) Color.White else Color.Black,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Medium
                                        )

                                        val dayPlans = state.plans.filter { plan ->
                                            val startDate =
                                                LocalDate.parse(plan.startDate.substring(0, 10))
                                            startDate == date
                                        }

                                        dayPlans.forEach { plan ->
                                            Text(
                                                text = plan.title,
                                                fontSize = 10.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = Color.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
