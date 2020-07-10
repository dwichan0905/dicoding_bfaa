package id.dwichan.githubusers.widgets.favorite

import android.content.Intent
import android.widget.RemoteViewsService

class FavoriteWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        FavoriteWidgetRemoteViewsFactory(this.applicationContext)

}
