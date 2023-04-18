package com.example.trashscan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.trashscan.databinding.FragmentIntroScreen1Binding
import com.example.trashscan.databinding.FragmentIntroScreen3Binding

class IntroScreen3Fragment : Fragment() {
    lateinit var binding: FragmentIntroScreen3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentIntroScreen3Binding.inflate(layoutInflater,container,false)
        binding.btRecycle.setOnClickListener{
            findNavController().navigate(R.id.action_introScreen3Fragment_to_mainPageFragment)
        }
        return binding.root
    }

}