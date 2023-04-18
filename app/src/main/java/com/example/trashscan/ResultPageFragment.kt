package com.example.trashscan

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.trashscan.databinding.FragmentResultPageBinding

class ResultPageFragment : Fragment() {
    lateinit var binding: FragmentResultPageBinding
    private val args: ResultPageFragmentArgs by navArgs<ResultPageFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResultPageBinding.inflate(layoutInflater,container,false)
        val myData = args.myData
        binding.ivWaste.setImageBitmap(myData.bitmap)
        binding.tvWaste.setText(myData.result)
        if(myData.result == "Organic"){
            binding.tvWasteDetails.text = "Your waste is biodegradable and had come from either a plant or animal. It can be converted using useful fertilizer by composting. Dumb it into green or organic bin. "
        }
        else{
            binding.tvWasteDetails.text = "Your waste can be converted into new material. Recycling it will recover the energy from your waste material. Dumb it into recycling bin."
        }
        return binding.root
    }

}