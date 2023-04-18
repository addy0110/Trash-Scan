package com.example.trashscan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.example.trashscan.databinding.FragmentIntroScreen1Binding

class IntroScreen1Fragment : Fragment() {
    lateinit var binding: FragmentIntroScreen1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIntroScreen1Binding.inflate(layoutInflater,container,false)
        binding.btReduce.setOnClickListener{
            findNavController().navigate(R.id.action_introScreen1Fragment_to_introScreen2Fragment)
        }
        return binding.root
    }
}