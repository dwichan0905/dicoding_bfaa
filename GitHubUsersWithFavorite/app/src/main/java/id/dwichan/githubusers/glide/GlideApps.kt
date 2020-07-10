package id.dwichan.githubusers.glide

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import id.dwichan.githubusers.R

@GlideModule
class GlideApps : AppGlideModule() {
    companion object {
        fun loadBitmapFromUrl(context: Context, url: String?): Bitmap {
            return Glide.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .submit()
                .get()
        }
    }
}