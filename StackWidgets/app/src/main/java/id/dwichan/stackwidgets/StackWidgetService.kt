package id.dwichan.stackwidgets

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = StackRemoteViewsFactory(this.applicationContext)

}
