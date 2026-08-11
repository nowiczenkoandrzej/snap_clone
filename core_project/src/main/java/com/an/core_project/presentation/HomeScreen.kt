package com.an.core_project.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.core_project.R
import com.an.core_project.presentation.components.ProjectCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewmodel: HomeViewmodel,
    onLoadProject: () -> Unit,
    navigateToSettingsScreen: () -> Unit
) {


    val projects = viewmodel
        .savedProjects
        .collectAsState()
        .value

    LaunchedEffect(Unit) {
        viewmodel.events.collect { event ->
            when(event) {
                HomeScreenEvent.LoadProject -> onLoadProject()
                is HomeScreenEvent.ShowSnackBar -> TODO()
                HomeScreenEvent.StartNewProject -> onLoadProject()
            }
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(R.string.projects))
                },
                actions = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(R.string.settings)
                            )
                        },
                        onClick = {
                            navigateToSettingsScreen()
                        },
                        colors = IconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.error,
                            disabledContentColor = MaterialTheme.colorScheme.error
                        )
                    )
                }


            )
        },
        floatingActionButton = {
            FloatingActionButton( onClick = {
                viewmodel.startNewProject()
            }){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }


        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            , verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(projects.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_projects_yet),
                        fontSize = 24.sp
                    )
                    Spacer(Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            viewmodel.startNewProject()
                        },
                        content = {
                            Text(
                                text = "Start new project now!",
                                fontSize = 24.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    )

                }
            }

            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(4.dp),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(projects) { project ->
                        ProjectCard(
                            summary = project,
                            onClick = { viewmodel.loadProject(project.id) },
                        )


                    }
                }
            )

        }
    }


}