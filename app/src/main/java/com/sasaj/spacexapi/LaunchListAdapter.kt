package com.sasaj.spacexapi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sasaj.spacexapi.databinding.LaunchItemBinding

class LaunchListAdapter(private val launches: List<LaunchListQuery.Launch?>) :
    RecyclerView.Adapter<LaunchListAdapter.ViewHolder>() {

    class ViewHolder(val binding: LaunchItemBinding) : RecyclerView.ViewHolder(binding.root)

    var onEndOfListReached: (() -> Unit)? = null

    override fun getItemCount(): Int {
        return launches.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LaunchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val launch = launches[position]
        holder.binding.site.text = launch?.rocket?.rocket?.name ?: ""
        holder.binding.missionName.text = launch?.mission_name
        holder.binding.missionPatch.load(launch?.links?.mission_patch_small) {
            placeholder(R.drawable.ic_placeholder)
        }
        if (position == launches.size - 1) {
            onEndOfListReached?.invoke()
        }
    }


}