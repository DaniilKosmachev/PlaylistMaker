package com.example.playlistmaker.ui.create_playlist.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment: Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            uri ->
            if (uri != null) {
                binding.playlistImageIV.setImageURI(uri)
                binding.playlistImageIV.background = null
                imageUri = uri
            }
        }

        binding.playlistImageIV.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        
        binding.nameET.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.createB.isEnabled = !s.isNullOrEmpty()
        }

    }

    fun saveImageToPrivateStorage(uri: Uri) {

        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            NAME_OF_DIR)

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath,uri.toString() + ".jpg")

        val inputStream = requireActivity().contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG,30,outputStream)
    }

    companion object {
        const val NAME_OF_DIR = "playlist_pick"
    }
}