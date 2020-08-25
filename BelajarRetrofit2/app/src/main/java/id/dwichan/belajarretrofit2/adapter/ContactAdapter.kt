package id.dwichan.belajarretrofit2.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.dwichan.belajarretrofit2.DetailsActivity
import id.dwichan.belajarretrofit2.R
import id.dwichan.belajarretrofit2.model.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var mData = ArrayList<Contact>()

    fun setData(contact: ArrayList<Contact>) {
        mData = contact
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            with(itemView) {
                tvContactName.text = contact.nama
                tvContactNumber.text = contact.nomor

                setOnClickListener {
                    val i = Intent(context, DetailsActivity::class.java)
                    i.putExtra(DetailsActivity.EXTRA_ID, contact.id)
                    i.putExtra(DetailsActivity.EXTRA_NAMA, contact.nama)
                    i.putExtra(DetailsActivity.EXTRA_NOMOR, contact.nomor)
                    (context as Activity).startActivityForResult(i, 99)
                }
            }
        }
    }
}