package com.example.trashscan

import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trashscan.databinding.FragmentMainPageBinding
import com.example.trashscan.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer

class MainPageFragment : Fragment() {
    lateinit var binding: FragmentMainPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainPageBinding.inflate(layoutInflater,container,false)
        binding.ivPlus.setOnClickListener{
            pickImage()
        }
        return binding.root
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageBitmap = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageBitmap)
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,150,150)
            classifyImage(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun classifyImage(bitmap: Bitmap?) {
        try {
            val model = Model.newInstance(requireContext())
            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            val byteBuffer: ByteBuffer = tensorImage.buffer
            inputFeature0.loadBuffer(byteBuffer)
            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            Log.d("@@model res",outputFeature0.floatArray[0].toString())
            // Releases model resources if no longer used.
            model.close()
            if(outputFeature0.floatArray[0].toInt() > .5) {
                Toast.makeText(requireContext(),"recyclable", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(requireContext(),"organic", Toast.LENGTH_SHORT).show()
        }catch (e: IOException){
            Log.d("@@model error",e.toString())
        }

    }
}