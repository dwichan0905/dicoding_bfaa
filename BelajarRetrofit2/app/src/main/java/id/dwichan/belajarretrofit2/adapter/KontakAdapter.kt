package id.dwichan.belajarretrofit2.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dwichan.belajarretrofit2.RincianActivity
import id.dwichan.belajarretrofit2.R
import id.dwichan.belajarretrofit2.model.Kontak
import kotlinx.android.synthetic.main.item_kontak.view.*

class KontakAdapter: RecyclerView.Adapter<KontakAdapter.KontakViewHolder>() {

    private var mData = ArrayList<Kontak>()

    fun setData(kontak: ArrayList<Kontak>) {
        mData = kontak
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontakViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kontak, parent, false)
        return KontakViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: KontakViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class KontakViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(kontak: Kontak) {
            with (itemView) {
                tvContactName.text = kontak.nama
                tvContactNumber.text = kontak.nomor

                setOnClickListener {
                    val i = Intent(context, RincianActivity::class.java)
                    i.putExtra(RincianActivity.EXTRA_ID, kontak.id)
                    i.putExtra(RincianActivity.EXTRA_NAMA, kontak.nama)
                    i.putExtra(RincianActivity.EXTRA_NOMOR, kontak.nomor)
                    (context as Activity).startActivity(i)
                }
            }
        }
    }
}