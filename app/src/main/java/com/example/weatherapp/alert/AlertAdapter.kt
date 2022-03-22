package com.example.weatherapp.alert

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.databinding.AlertListItemBinding
import com.example.weatherapp.models.Alert
import com.example.weatherapp.util.dateFromLongToStr
import com.example.weatherapp.util.getDate

class AlertAdapter(val deleteLocationCallback:(alert:Alert)->Unit): RecyclerView.Adapter<AlertAdapter.AlertViewHolder>(){
    lateinit var context:Context
    var alert = listOf<Alert>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    class AlertViewHolder(val binding: AlertListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        context = parent.context
        return AlertViewHolder(
            AlertListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.binding.from.text = getDate(alert[position].date,"MM/dd/yyyy hh:mm")
        holder.binding.Event.text = alert[position].event
        holder.binding.cancel.setOnClickListener {
            AlertDialog.Builder(context!!)
                .setTitle(R.string.cancelAlarm)
                .setMessage(R.string.TitleDeleteAlarm)
                .setPositiveButton(
                    android.R.string.yes
                ) { _, _ ->
                    deleteLocationCallback(alert[position])
                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }
    }

    override fun getItemCount(): Int = alert.size
}