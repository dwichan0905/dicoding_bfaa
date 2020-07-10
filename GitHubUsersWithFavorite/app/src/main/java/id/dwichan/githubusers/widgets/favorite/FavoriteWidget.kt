package id.dwichan.githubusers.widgets.favorite

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import id.dwichan.githubusers.FavoriteActivity
import id.dwichan.githubusers.R

/**
 * Implementation of App Widget functionality.
 */
class FavoriteWidget : AppWidgetProvider() {

    companion object {

        const val FAVORITE_ACTION = "id.dwichan.githubusers.FAVORITE_ACTION"
        const val UPDATE_ACTION = "id.dwichan.githubusers.UPDATE_ACTION"
        const val EXTRA_ITEM = "id.dwichan.githubusers.EXTRA_ITEM"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, FavoriteWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri() // butuh Java 1.8

            val views = RemoteViews(context.packageName, R.layout.favorite_widget)
            views.setRemoteAdapter(R.id.stackView, intent)
            views.setEmptyView(R.id.stackView, R.id.empty_view)

            val clickActionIntent = Intent(context, FavoriteWidget::class.java)
            clickActionIntent.action = FAVORITE_ACTION
            clickActionIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri() // butuh Java 1.8
            val clickActionPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                clickActionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stackView, clickActionPendingIntent)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != null) {
            when (intent.action) {
                UPDATE_ACTION -> {
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val id = appWidgetManager.getAppWidgetIds(
                        ComponentName(
                            context!!,
                            FavoriteWidget::class.java
                        )
                    )
                    appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.stackView)
                    this.onUpdate(context, appWidgetManager, id)
                }
                FAVORITE_ACTION -> context?.startActivity(
                    Intent(context, FavoriteActivity::class.java)
                )
            }
        }
        super.onReceive(context, intent)
    }
}

