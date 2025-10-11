package com.example.remind.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.remind.R
import com.example.remind.data.repository.HabitRepository
import com.example.remind.ui.activity.MainActivity

class HabitWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Get habit data
            val habitRepository = HabitRepository(context)
            val habits = habitRepository.getTodayHabits()
            
            val totalHabits = habits.size
            val completedHabits = habits.count { it.isCompleted }
            val percentage = if (totalHabits > 0) {
                (completedHabits * 100) / totalHabits
            } else {
                0
            }

            // Create RemoteViews
            val views = RemoteViews(context.packageName, R.layout.widget_habit_progress)

            // Update text views
            views.setTextViewText(
                R.id.widget_percentage,
                "$percentage%"
            )
            views.setTextViewText(
                R.id.widget_habit_count,
                "$completedHabits of $totalHabits completed"
            )

            // Update progress
            views.setProgressBar(R.id.progress_circle, 100, percentage, false)

            // Set up click intent to open app
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_title, pendingIntent)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }


        fun updateAllWidgets(context: Context) {
            val intent = Intent(context, HabitWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, HabitWidgetProvider::class.java)
            )
            
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            context.sendBroadcast(intent)
        }
    }
}

