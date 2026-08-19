package com.example.playlistmaker.playlistCreate.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.example.playlistmaker.playlistCreate.domain.models.Playlist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistCreateFragment : Fragment() {
    private val viewModel: PlaylistCreateViewModel by viewModel()
    private var _binding: FragmentPlaylistCreateBinding? = null
    private val binding get() = _binding!!
    private var isClickAllowed = true

    private var bottomPadding = 0
    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistCreateBinding.inflate(inflater, container, false)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentPlaylistCreate) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            bottomPadding = systemBars.bottom
            view.setPadding(0, 0, 0, bottomPadding)
            insets
        }

        viewModel.observeShowMessage().observe(viewLifecycleOwner){
            showMessage(it)
        }

        binding.toolbarCreatePlaylist.setNavigationOnClickListener {
            viewModel.onClickBack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            viewModel.onClickBack()
        }

        binding.etPlaylistName.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isEmpty()) {
                    binding.btnCreatePlaylist.isEnabled = false
                    binding.btnCreatePlaylist.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yp_text_gray))
                } else {
                    binding.btnCreatePlaylist.isEnabled = true
                    binding.btnCreatePlaylist.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yp_blue))
                }
                viewModel.updateName(text.toString())
            }
        }

        binding.etPlaylistDescription.doOnTextChanged { text, _, _, _ ->
            viewModel.updateDescription(text.toString())
        }

        binding.btnCreatePlaylist.setOnClickListener {
            if (clickDebounce()) {
                viewModel.createPlaylist()
                findNavController().navigateUp()
            }
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.imgCreatePlaylistAddPhoto.background = null
                    binding.imgCreatePlaylistAddPhoto.setImageURI(uri)
                    binding.imgCreatePlaylistAddPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                    viewModel.updateUri(uri)
                }
            }

        binding.imgCreatePlaylistAddPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.CustomMaterialAlertDialog)
            .setTitle(getString(R.string.text_finish_creating_playlist))
            .setMessage(getString(R.string.text_all_unsaved_data_will_be_lost))
            .setNeutralButton(getString(R.string.text_cancel)) { _, _ ->
            }.setPositiveButton(getString(R.string.text_complete)) { _, _ ->
                close()
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun render(state: PlaylistCreateState) {
        when (state) {
            is PlaylistCreateState.Content -> showContent(state.playlist)
            PlaylistCreateState.Empty -> showEmpty()
            is PlaylistCreateState.ShowDialog -> showDialog()
            PlaylistCreateState.Close -> close()
        }
    }
    fun showContent(playlist: Playlist) {
        val file = File(playlist.pathImageCover)
        binding.imgCreatePlaylistAddPhoto.background = null
        Glide.with(binding.root)
            .load(file)
            .centerCrop()
            .into(binding.imgCreatePlaylistAddPhoto)
    }

    fun showMessage(message: String) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.toast_custom, null)
        val textView = view.findViewById<TextView>(R.id.toastText)
        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_LONG
        textView.text = message
        toast.view = view
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM,0,bottomPadding)
        toast.show()
    }

    fun showDialog() {
        confirmDialog.show()
    }

    fun close(){
        findNavController().navigateUp()
    }

    fun showEmpty() {

    }


    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}