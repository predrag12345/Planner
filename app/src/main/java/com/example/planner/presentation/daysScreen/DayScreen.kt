package com.example.planner.presentation.daysScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planner.presentation.bottomNavigationBar.BottomNavigationBar
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DayScreen(
    navController: NavController,
    date: LocalDate,
    viewModel: DayViewModel = hiltViewModel()
) {
    val state by viewModel.viewState

    val listState = rememberLazyListState()
    val formatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") }

    LaunchedEffect(date) {
        viewModel.setEvent(DayEvent.LoadDay(date))
        listState.scrollToItem(8)
    }



    Scaffold(
        bottomBar = { BottomNavigationBar(navController, 1) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.dayTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.hours.size) { index ->
                    val hour = state.hours[index]
                    val slotHour = hour.substringBefore(":").toInt()
                    val slotStart = date.atTime(slotHour, 0)
                    val slotEnd = date.atTime(slotHour, 59)

                    val plansForThisHour = state.plans.mapNotNull { plan ->
                        try {
                            val start = LocalDateTime.parse(plan.startDate, formatter)
                            val end = LocalDateTime.parse(plan.endDate, formatter)
                            if (start <= slotEnd && end >= slotStart) {
                                Triple(plan.title, plan.description, Pair(start, end))
                            } else null
                        } catch (e: Exception) {
                            null
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(0.5.dp, Color.LightGray)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hour,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .border(0.5.dp, Color(0xFFFAF8FF))
                                .background(Color.White),
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (plansForThisHour.isNotEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    plansForThisHour.forEach { (title, desc, times) ->
                                        val (start, end) = times
                                        val durationMinutes = Duration.between(start, end).toMinutes().toInt()
                                        val heightDp = max(15, durationMinutes).dp
                                        val startMinuteOffset =
                                            if (start.toLocalDate() == date && start.hour == slotHour)
                                                start.minute else 0

                                        Spacer(modifier = Modifier.height(startMinuteOffset.dp))

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.95f)
                                                .height(heightDp)
                                                .background(
                                                    Color(0xFFB3C6FF),
                                                    shape = MaterialTheme.shapes.small
                                                )
                                                .padding(4.dp)
                                                .clickable {
                                                    navController.navigate(
                                                        "viewPlan/${title}/${desc}/${start.format(formatter)}/${end.format(formatter)}"
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (end.hour == slotHour) {
                                                Text(
                                                    text = "$title\n$desc",
                                                    fontSize = 10.sp,
                                                    color = Color.Black,
                                                    fontWeight = FontWeight.Medium,
                                                    maxLines = 5,
                                                    overflow = TextOverflow.Ellipsis
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
}
