package com.example.trashscan

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.trashscan.databinding.FragmentMainPageBinding
import com.example.trashscan.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer


class MainPageFragment : Fragment() {
    private var imageUri: Uri? = null
    lateinit var binding: FragmentMainPageBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("@@camera","main")

        // Inflate the layout for this fragment
        binding = FragmentMainPageBinding.inflate(layoutInflater,container,false)
        binding.pbProgressBar.visibility = View.GONE
        binding.tvRunningModel.visibility = View.GONE
        binding.ivPlus.setOnClickListener{
            pickImage()
        }
        return binding.root
    }

    private fun pickImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
//        val builder: android.support.v7.app.AlertDialog.Builder = Builder(activity)
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Option")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                dialog.dismiss()
                Log.d("@@camera","take photo")
                val fileName = "new-photo-name.jpg"
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, fileName)
                values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera")
                imageUri = requireContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 100)
            } else if (options[item] == "Choose From Gallery") {
                dialog.dismiss()
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 200)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 200 && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val imageBitmap = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageBitmap)
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,150,150)
            classifyImage(bitmap)
        }
        else if(requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK){
            Log.d("@@camera","into")
                Log.d("@@camera","here")
                var bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri)
                bitmap = ThumbnailUtils.extractThumbnail(bitmap,150,150)
                classifyImage(bitmap)
        }
        else{
            Log.d("@@camera","data null")
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
            binding.ivPlus.visibility = View.GONE
            binding.tvTap.visibility = View.GONE
            binding.pbProgressBar.visibility = View.VISIBLE
            binding.tvRunningModel.visibility = View.VISIBLE

            if(outputFeature0.floatArray[0].toInt() > .5) {
                val myData = MyData(bitmap!!,"Recyclable")
                val action = MainPageFragmentDirections.actionMainPageFragmentToResultPageFragment(myData)
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    findNavController().navigate(action)
                }, 1000)
//                Toast.makeText(requireContext(),"Recyclable", Toast.LENGTH_SHORT).show()
            }
            else {
                val myData = MyData(bitmap!!,"Organic")
                val action = MainPageFragmentDirections.actionMainPageFragmentToResultPageFragment(myData)
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    findNavController().navigate(action)
                }, 1000)
//                Toast.makeText(requireContext(), "Organic", Toast.LENGTH_SHORT).show()
            }

        }catch (e: IOException){
            Log.d("@@model error",e.toString())
        }

    }
}