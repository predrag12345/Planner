package com.example.planner.presentation.weeksScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planner.presentation.bottomNavigationBar.BottomNavigationBar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.max

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeekScreen(
    navController: NavController,
    viewModel: WeekViewModel = hiltViewModel()
) {
    val state by viewModel.viewState
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.setEvent(WeekEvent.LoadWeek)
        coroutineScope.launch { listState.scrollToItem(8) }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, 1) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 8.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.width(60.dp).fillMaxHeight())
                state.daysOfWeek.forEachIndexed { index, day ->
                    val isToday = state.datesOfWeek.getOrNull(index) == LocalDate.now()
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(if (isToday) Color(0xFF4A7DDF) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = day,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = if (isToday) Color.White else Color.Black
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.width(60.dp).fillMaxHeight())
                state.datesOfWeek.forEachIndexed { index, date ->
                    val isToday = date == LocalDate.now()
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(if (isToday) Color(0xFF4A7DDF) else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = if (isToday) Color.White else Color.Black
                        )
                    }
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.hours.size) { index ->
                    val hour = state.hours[index] // npr. "14:00"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .border(0.5.dp, Color.LightGray),
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


                        repeat(7) { dayIndex ->
                            val date = state.datesOfWeek.getOrNull(dayIndex)

                            // Plan koji počinje TAČNO u ovom satu (da ne crtamo 100 puta isti)
                            val planForThisHour = state.plans.firstOrNull { plan ->
                                try {
                                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                    val start = LocalDateTime.parse(plan.startDate, formatter)
                                    val planDate = start.toLocalDate()
                                    val planHour = start.hour

                                    planDate == date && planHour == hour.substringBefore(":").toInt()
                                } catch (e: Exception) {
                                    false
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .border(0.5.dp, Color(0xFFFAF8FF))
                                    .background(Color.White),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                if (planForThisHour != null) {

                                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                        val start = LocalDateTime.parse(planForThisHour.startDate, formatter)
                                        val end = LocalDateTime.parse(planForThisHour.endDate, formatter)
                                        val durationHours = max(1, java.time.Duration.between(start, end).toHours().toInt())

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.95f)
                                                .height(durationHours * 60.dp) // blok raste po trajanju
                                                .background(Color(0xFFB3C6FF), shape = MaterialTheme.shapes.small)
                                                .padding(4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${planForThisHour.title}\n${planForThisHour.description}",
                                                fontSize = 8.sp,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 4,
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
