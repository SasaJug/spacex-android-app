package com.sasaj.spacexapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sasaj.spacexapi.databinding.LaunchDetailsFragmentBinding

class LaunchDetailsFragment : Fragment() {

    private lateinit var binding: LaunchDetailsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = LaunchDetailsFragmentBinding.inflate(inflater)

        return binding.root
    }
}
