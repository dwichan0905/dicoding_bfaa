package id.dwichan.githubusers.widgets.favorite

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import id.dwichan.githubusers.R
import id.dwichan.githubusers.database.FavoriteHelper
import id.dwichan.githubusers.glide.GlideApps
import id.dwichan.githubusers.helper.MappingHelper

class FavoriteWidgetRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItem = ArrayList<Bitmap>()
    private lateinit var favoriteHelper: FavoriteHelper

    override fun onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(context)
        if (!favoriteHelper.isDatabaseOpened()) favoriteHelper.openDatabase()
    }

    override fun onDataSetChanged() {
        favoriteHelper = FavoriteHelper.getInstance(context)
        if (!favoriteHelper.isDatabaseOpened()) favoriteHelper.openDatabase()

        val identityToken = Binder.clearCallingIdentity()
        Binder.restoreCallingIdentity(identityToken)

        try {
            val query = favoriteHelper.queryShowAll()
            val favoriteArray = MappingHelper.mapCursorToArrayList(query)
            if (favoriteArray.size > 0) {
                mWidgetItem.clear()
                for (i in 0 until favoriteArray.size) {
                    try {
                        val avatar = favoriteArray[i].avatar
                        val avatarToBitmap = GlideApps.loadBitmapFromUrl(context, avatar)
                        mWidgetItem.add(avatarToBitmap)
                    } catch (e: Exception) {
                        mWidgetItem.add(
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.ic_baseline_error_24
                            )
                        )
                    }
                }
            }
        } catch (e: IllegalStateException) {
            Log.e("WidgetError", "IllegalStateException: ${e.message}")
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)
        remoteViews.setImageViewBitmap(R.id.imageView, mWidgetItem[position])

        val extras = bundleOf(
            FavoriteWidget.EXTRA_ITEM to position
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        remoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent)

        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0L

    override fun hasStableIds(): Boolean = false

    override fun getCount(): Int = mWidgetItem.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}
}