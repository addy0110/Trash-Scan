package com.example.trashscan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.trashscan.databinding.FragmentIntroScreen1Binding
import com.example.trashscan.databinding.FragmentIntroScreen2Binding

class IntroScreen2Fragment : Fragment() {
    lateinit var binding : FragmentIntroScreen2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIntroScreen2Binding.inflate(layoutInflater,container,false)
        binding.btReuse.setOnClickListener{
            findNavController().navigate(R.id.action_introScreen2Fragment_to_introScreen3Fragment)
        }
        return binding.root
    }
}