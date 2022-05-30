package com.example.android.goalchaser.background

import android.app.Application
import com.example.android.goalchaser.BuildConfig
import com.example.android.goalchaser.localdatasource.GoalsDao
import com.example.android.goalchaser.localdatasource.ImageDataDao
import com.example.android.goalchaser.localdatasource.getGoalChaserDatabase
import com.example.android.goalchaser.remotedatasource.ImageDataApiService
import com.example.android.goalchaser.remotedatasource.ImageOfTheDayDataApiService
import com.example.android.goalchaser.repository.GoalsRepository
import com.example.android.goalchaser.repository.ImageDataRepository
import com.example.android.goalchaser.ui.activecompletedgoals.ActiveCompletedGoalsViewModel
import com.example.android.goalchaser.ui.createeditgoal.CreateEditGoalViewModel
import com.example.android.goalchaser.ui.statistics.StatisticsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class GoalChaserApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // use Timber for more effective logging during debug process
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        //use Koin for dependency injection
        val koinModule = module {

              viewModel {
                  CreateEditGoalViewModel(
                      get() as GoalsRepository
                  )
              }
            viewModel {
                StatisticsViewModel(
                    get() as GoalsRepository
                )
            }

            single {
                ActiveCompletedGoalsViewModel(
                    get() as ImageDataRepository,
                    get() as GoalsRepository
                )
            }

            single {
                GoalsRepository(
                    get() as GoalsDao
                )
            }
            single {
                ImageDataRepository(
                    get() as ImageDataApiService,
                    get() as ImageDataDao
                )
            }
            single { getGoalChaserDatabase(this@GoalChaserApplication).goalsDao }
            single { getGoalChaserDatabase(this@GoalChaserApplication).imageDao }
            single { ImageOfTheDayDataApiService.retrofitService }
        }

        startKoin {
            androidContext(this@GoalChaserApplication)
            modules(listOf(koinModule))
        }
    }
}
