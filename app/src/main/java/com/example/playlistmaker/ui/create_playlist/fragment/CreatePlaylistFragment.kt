package com.example.playlistmaker.ui.create_playlist.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.ui.create_playlist.view_model.CreatePlaylistFragmentViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreatePlaylistFragment: Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<CreatePlaylistFragmentViewModel>()

    private var informationDialog: MaterialAlertDialogBuilder? = null

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

        informationDialog = MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Завершить создание плейлиста?")
            setMessage("Все несохраненные данные будут потеряны")
            setPositiveButton("Завершить") { _, _ ->
                findNavController().navigateUp()
            }
            setNegativeButton("Отмена") { _, _ ->
            }
        }

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

        binding.backButtonIB.setOnClickListener {
            if (checkUnsavedData()) {
                informationDialog?.show()
            } else {
                findNavController().navigateUp()
            }
        }

        binding.createB.setOnClickListener {
            var name = binding.nameET.text.toString()
            var description = binding.descriptionET.text.toString()
            if (imageUri != null) {
                saveImageToPrivateStorage(imageUri!!, name)
            }
            if (!name.isNullOrEmpty()) {
                viewModel.addNewPlaylist(
                    Playlist(
                        null,
                        name = name,
                        description = description,
                        uri = imageUri.toString(),
                        "",
                        0
                        )
                )
            }
            showToast(name)
            findNavController().navigateUp()
        }

    }

    fun checkUnsavedData(): Boolean {
        return imageUri != null || !binding.nameET.text.isNullOrEmpty() || !binding.descriptionET.text.isNullOrEmpty()
    }

    fun showToast(nameOfPlaylist: String) {
        Toast.makeText(requireContext(), "Плейлист $nameOfPlaylist создан", Toast.LENGTH_SHORT).show()
    }

    fun saveImageToPrivateStorage(uri: Uri, nameOfPlaylist: String) {

        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            NAME_OF_DIR)

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath,nameOfPlaylist + ".jpg")

        val inputStream = requireActivity().contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG,30,outputStream)

        imageUri = file.toUri()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (checkUnsavedData()) {
                    informationDialog?.show()
                } else {
                    findNavController().navigateUp()
                }
            }
        })
    }

    companion object {
        const val NAME_OF_DIR = "playlist_pick"
    }
}