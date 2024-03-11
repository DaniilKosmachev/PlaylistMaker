package com.example.playlistmaker.ui.edit_playlist.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.library.playlists.model.Playlist
import com.example.playlistmaker.ui.create_playlist.fragment.CreatePlaylistFragment
import com.example.playlistmaker.ui.edit_playlist.view_model.EditPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class EditPlaylistFragment: CreatePlaylistFragment() {

    private var imageUri: Uri? = null

    private var receivedPlaylist: Playlist? = null

    private val viewModel by viewModel<EditPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receivedPlaylist = arguments?.getParcelable("RECEIVED_PLAYLIST")

        binding.backButtonIB.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.nameET.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.createB.isEnabled = !s.isNullOrEmpty()
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

        binding.createB.setOnClickListener {
            var name = binding.nameET.text.toString()
            var description = binding.descriptionET.text.toString()
            if (imageUri != null) {
                saveImageToPrivateStorage(imageUri!!, name)
            }
            if (!name.isNullOrEmpty()) {
                viewModel.editNewPlaylist(receivedPlaylist!!.id!!, name, description, imageUri.toString())
            }
            findNavController().navigateUp()
        }


        if (!receivedPlaylist!!.uri.equals("null")) binding.playlistImageIV.apply {
            setImageURI(receivedPlaylist!!.uri!!.toUri())
            scaleType = ImageView.ScaleType.CENTER_CROP
            imageUri = receivedPlaylist!!.uri!!.toUri()
        }
        else {
            binding.playlistImageIV.apply {
                setImageResource(R.drawable.add_image)
            }
        }


        binding.createB.text = "Сохранить"
        binding.newPlaylistTV.text = "Редактировать"
        binding.nameET.setText(receivedPlaylist!!.name)
        if (receivedPlaylist?.description.isNullOrEmpty()) binding.descriptionET.setText("") else binding.descriptionET.setText(receivedPlaylist!!.description)

    }

    override fun saveImageToPrivateStorage(uri: Uri, nameOfPlaylist: String) {

        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            CreatePlaylistFragment.NAME_OF_DIR
        )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath,nameOfPlaylist + System.currentTimeMillis().toString() + ".jpg")

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
                    findNavController().navigateUp()
            }
        })
    }

    companion object {
        const val NAME_OF_DIR = "playlist_pick"
    }
}